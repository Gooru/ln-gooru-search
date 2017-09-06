/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_PREFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_SUFFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_POINT;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author SearchTeam
 * 
 */
@Component
public class ElasticsearchProcessor extends SearchProcessor<SearchData, Object> {

	protected static final Logger LOG = LoggerFactory.getLogger(ElasticsearchProcessor.class);

	private static RestClient client = null;
	
	private static Sniffer sniffer = null;

	@PostConstruct
	public void onStartUp() {
		// Configuration
		LOG.info("Creating Elasticsearh Rest client...");
		HttpHost[] httpHosts = buildHosts(getSetting(S_ES_POINT));
		SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
		RestClient restClient = RestClient.builder(httpHosts).setFailureListener(sniffOnFailureListener).build();
		Sniffer sniffer = Sniffer.builder(restClient).setSniffAfterFailureDelayMillis(30000).build();
		sniffOnFailureListener.setSniffer(sniffer);

		setClient(restClient);
		setSniffer(sniffer);
		LOG.info("IP included to rest client: {} ", getSetting(S_ES_POINT));

	}

	@PreDestroy
	public void onStop() {
		try {
			getClient().close();
			getSniffer().close();
			LOG.info("Rest client shutdown");
		} catch (IOException e) {
			LOG.info("Rest Client is not shutdown : {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response)  {
		String indexName = searchData.getIndexType().getName();
		String indexType = searchData.getType().toLowerCase();
		if (indexType.equalsIgnoreCase(TYPE_SCOLLECTION)) {
			indexType = TYPE_COLLECTION;
		} else if (indexType.equalsIgnoreCase(KEYWORD_COMPETENCY)) {
			indexType = TYPE_TAXONOMY;
		} else if (indexType.equalsIgnoreCase(AUTOCOMPLETE_KEYWORD)) {
			indexType = KEYWORD;
		}

		try {
			String searchQuery = SERIAILIZER.writeValueAsString(searchData.getQueryDsl().getValues());
			if (searchData.getPretty().equals("1")) {
				ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.readValue(searchQuery, Map.class);
				LOG.info("IndexName:" + getSetting(S_ES_INDEX_PREFIX) + indexName + getSetting(S_ES_INDEX_SUFFIX) + SLASH_SEPARATOR + indexType);
				LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			}
			long start = System.currentTimeMillis();

			HttpEntity entity = new NStringEntity(searchQuery, ContentType.APPLICATION_JSON);
			Response indexResponse = getClient().performRequest(GET_METHOD,
					SLASH_SEPARATOR + getSetting(S_ES_INDEX_PREFIX) + indexName + getSetting(S_ES_INDEX_SUFFIX) + SLASH_SEPARATOR + indexType + SLASH_SEPARATOR + UNDERSCORE_SEARCH,
					Collections.<String, String>emptyMap(), entity);
			if (indexResponse.getEntity() != null) {
				if (indexResponse.getStatusLine().getStatusCode() == 400 || indexResponse.getStatusLine().getStatusCode() == 503) {
					throw new BadRequestException("Please check request param input values");
				}
			}
			searchData.setSearchResultText(EntityUtils.toString(indexResponse.getEntity()));
			if (LOG.isDebugEnabled()) {
				LOG.debug("Elapsed Time for " + indexType + " : " + (System.currentTimeMillis() - start) + " ms");
			}
		} catch (BadRequestException e) {
			throw new BadRequestException("Please check request input param values" + e);		
		} catch (Exception e) {
			LOG.error("Search Error"+ e); 
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.Elasticsearch;
	}

	public static RestClient getClient() {
		return client;
	}

	public static void setClient(RestClient client) {
		ElasticsearchProcessor.client = client;
	}
	
	public static Sniffer getSniffer() {
		return sniffer;
	}

	public static void setSniffer(Sniffer sniffer) {
		ElasticsearchProcessor.sniffer = sniffer;
	}
	
	private HttpHost[] buildHosts(String endpoints) {
		String[] hosts = endpoints.split(COMMA);
		HttpHost[] httpHosts = new HttpHost[0];
		for (String host : hosts) {
			String[] hostParams = host.split(COLON);
			if (hostParams.length == 2) {
				httpHosts = appendToArray(httpHosts , new HttpHost(hostParams[0], Integer.valueOf(hostParams[1]), HTTP));
			} else {
				LOG.debug("Oops! Could't initialize rest client with host : {}", host);
			}
		}
		return httpHosts;
	}
	
	private HttpHost[] appendToArray(HttpHost[] array, HttpHost x){
		HttpHost[] result = new HttpHost[array.length + 1];
	    for(int i = 0; i < array.length; i++)
	        result[i] = array[i];
	    result[result.length - 1] = x;
	    return result;
	}

}
