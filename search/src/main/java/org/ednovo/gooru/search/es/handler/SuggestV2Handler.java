package org.ednovo.gooru.search.es.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.service.SuggestBaseHandler;
import org.springframework.http.HttpStatus;

public abstract class SuggestV2Handler<O> implements SuggestBaseHandler<Object>{
	
	
	/**
	 *  Contains the list of all existing registered suggest handlers. There can be a one or more suggest handlers for single suggest handler type.
	 */
	private static final Map<String, List<SuggestV2Handler<Object>>> handlers = new HashMap<String, List<SuggestV2Handler<Object>>>();

	/**
	 * Registration of suggest handlers.
	 * This is done automatically as a internal process. Should not be called explicitly.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected final void register() {
		List<SuggestV2Handler<Object>> suggestHandlers ;
		if (handlers.containsKey(getName())) {
			suggestHandlers = (List<SuggestV2Handler<Object>>) handlers.get(getName());
			if(suggestHandlers.contains((SuggestV2Handler<Object>) this)){
				throw new RuntimeException("Duplicate Suggest Handler with name : " + getType());
			}
			else{
				suggestHandlers.add((SuggestV2Handler<Object>) this);
				handlers.put(getName(), suggestHandlers);
			}
		} else {
			suggestHandlers = new ArrayList<SuggestV2Handler<Object>>();
			suggestHandlers.add((SuggestV2Handler<Object>) this);
			handlers.put(getName(), suggestHandlers);
		}
		
		//System.out.println("REgistered suggest handlers :" + handlers.toString());
	}
	
	/**
	 * Mention the search handler type which will be used for registration.
	 * The type must be unique for each and every suggest handler.
	 * The type name must be used to fetch suggest handler
	 * @return
	 */
	protected abstract SearchHandlerType getType();
	
	protected abstract String getName();

	
	/**
	 * Suggest Handler of prefix Suggest can be fetched by passing the suggest handler name
	 * @param name
	 * @returnO
	 */
	public static List<SuggestV2Handler<Object>> getSuggester(String name) {
		List<SuggestV2Handler<Object>> suggesters;
		suggesters = handlers.get(name);
		if(suggesters == null || suggesters.size() <= 0) {
			throw new SearchException(HttpStatus.BAD_REQUEST, "Invalid Type");
		}
		return suggesters;
	}
	
}
