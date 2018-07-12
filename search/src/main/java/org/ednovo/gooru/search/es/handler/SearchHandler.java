/**
 * 
 */
package org.ednovo.gooru.search.es.handler;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.ProcessorChain;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Each search handler must extend this abstract class.
 * The registration is done automatically as a post bean creation process.
 * Call the search method to perform the search operation
 * @author SearchTeam
 * 
 */
public abstract class SearchHandler<I extends SearchData, O extends Object> {

	/**
	 *  Contains the list of all existing registered search handlers.
	 */
	private static final Map<String, SearchHandler<SearchData, Object>> handlers = new HashMap<String, SearchHandler<SearchData, Object>>();

	protected static final Logger LOG = LoggerFactory.getLogger(SearchHandler.class);

	protected ProcessorChain<I, O> processorChain;
	
	protected static final String SEARCH_PREFIX = "SEARCH-";
	
	protected static final String SUGGEST_PREFIX = "SUGGEST-";
	
	@Autowired
	private HibernateTransactionManager transactionManager;
	
	protected TransactionTemplate transactionTemplate;

	public SearchHandler() {
		processorChain = new ProcessorChain<I, O>(getProcessorTypeChain());
	}
	
	@PostConstruct
	public void init() {
		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setReadOnly(true);
	}

	/**
	 * Call the method to perform the search operation
	 * @param searchData
	 * @return
	 */
	public SearchResponse<O> search(I searchData) {
		SearchSettingService.validateCache();
		SearchResponse<O> response = new SearchResponse<O>();
		searchData.setIndexType(getIndexType());
		searchData.setType(getType().name());
		processorChain.executeProcessorChain(searchData, response, transactionTemplate);
		return response;
	}

	/**
	 * Registration of search handlers.
	 * This is done automatically as a internal process. Should not be called explicitly.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected final void register() {
		if (handlers.containsKey(getType().name())) {
			throw new RuntimeException("Duplicate Search Handler with name : " + getType());
		} else {
			handlers.put(getName(), (SearchHandler<SearchData, Object>) this);
		}
	}
	
	/**
	 * Search Handler can be fetched by passing the search handler name
	 * @param name
	 * @returnO
	 */
	private static SearchHandler<SearchData, Object> get(String name) {
		SearchHandler<SearchData, Object> searcher = handlers.get(name);
		if(searcher == null) {
			throw new BadRequestException("Invalid Type");
		}
		return searcher;
	}

	/**
	 * Search Handler of prefix Search can be fetched by passing the search handler name
	 * @param name
	 * @returnO
	 */
	public static SearchHandler<SearchData, Object> getSearcher(String name) {
		return get(SEARCH_PREFIX + name);
	}
	
	/**
	 * Search Handler of prefix Suggest can be fetched by passing the search handler name
	 * @param name
	 * @returnO
	 */
	public static SearchHandler<SearchData, Object> getSuggester(String name) {
		return get(SUGGEST_PREFIX + name);
	}

	public ProcessorChain<I, O> getProcessorChain() {
		return processorChain;
	}

	/**
	 * Mention the list of processors which should be executed, when the search is done.
	 * The processors will be executed in the same order the processors types are mentioned
	 * The second dimension is used to run processors asynchronously. 
	 * @return
	 */
	protected abstract SearchProcessorType[][] getProcessorTypeChain();

	/**
	 * Mention the search handler type which will be used for registration.
	 * The type must be unique for each and every search handler.
	 * The type name must be used to fetch search handler
	 * @return
	 */
	protected abstract SearchHandlerType getType();
	
	protected String getName() {
		return SEARCH_PREFIX + getType().name();
	}
	
	protected abstract EsIndex getIndexType();

	public static Logger getLogger() {
		return LOG;
	}
}
