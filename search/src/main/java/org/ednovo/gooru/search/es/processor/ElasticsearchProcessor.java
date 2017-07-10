/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_PREFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_SUFFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_POINT;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;


/**
 * @author SearchTeam
 * 
 */
@Component
public class ElasticsearchProcessor extends SearchProcessor<SearchData, Object> {

	protected static final Logger LOG = LoggerFactory.getLogger(ElasticsearchProcessor.class);

	private static JestClient client = null;

	@PostConstruct
	public void onStartUp() {
		// Configuration

		HttpClientConfig clientConfig = new HttpClientConfig.Builder(Arrays.asList(getSetting(S_ES_POINT).split(","))).multiThreaded(true)
				.maxTotalConnection(500).defaultMaxTotalConnectionPerRoute(100).readTimeout(30000).build();

		// Construct a new Jest client according to configuration via factory
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(clientConfig);
		setClient(factory.getObject());
		LOG.info("IP included to jest client: " + getSetting(S_ES_POINT));
		LOG.info("Creating Jest client...");

	}

	@PreDestroy
	public void onStop() {
		getClient().shutdownClient();
		LOG.info("Jest client shutdown");
	}

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response)  {
		String indexName = searchData.getIndexType().getName();
		String indexType = searchData.getType().toLowerCase();
		if (indexType.equalsIgnoreCase(TYPE_SCOLLECTION)) {
			indexType = TYPE_COLLECTION;
		} else if (indexType.equalsIgnoreCase(KEYWORD_COMPETENCY)) {
			indexType = TYPE_TAXONOMY;
		}

		try {
			String searchQuery = SERIAILIZER.writeValueAsString(searchData.getQueryDsl().getValues());
			if (searchData.getPretty().equals("1")) {
				ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.readValue(searchQuery, Map.class);
				LOG.info("IndexName:" + getSetting(S_ES_INDEX_PREFIX) + indexName + getSetting(S_ES_INDEX_SUFFIX) + "/" + indexType);
				LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			}

			Search search = new Search.Builder(searchQuery).addIndex(getSetting(S_ES_INDEX_PREFIX) + indexName + getSetting(S_ES_INDEX_SUFFIX)).addType(indexType).build();

			long start = System.currentTimeMillis();
			JestResult jestResult = getClient().execute(search);
			if (jestResult.getErrorMessage() != null) {
				JSONObject responseStatus = new JSONObject(jestResult.getJsonString());
				if ((Integer) responseStatus.get(SEARCH_STATUS) == 400 || (Integer) responseStatus.get(SEARCH_STATUS) == 503) {
					throw new BadRequestException("Please check request param input values");
				}
			}
			searchData.setSearchResultText(jestResult.getJsonString());
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

	public static JestClient getClient() {
		return client;
	}

	public static void setClient(JestClient client) {
		ElasticsearchProcessor.client = client;
	}
}
