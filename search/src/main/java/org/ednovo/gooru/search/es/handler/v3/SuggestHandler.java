package org.ednovo.gooru.search.es.handler.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ednovo.gooru.search.es.exception.SearchException;
import org.springframework.http.HttpStatus;

public abstract class SuggestHandler<O> implements SuggestBaseHandler<Object>{
	
	
	/**
	 *  Contains the list of all existing registered suggest handlers. There can be a one or more suggest handlers for single suggest handler type.
	 */
	private static final Map<String, List<SuggestHandler<Object>>> handlers = new HashMap<String, List<SuggestHandler<Object>>>();

	/**
	 * Registration of suggest handlers.
	 * This is done automatically as a internal process. Should not be called explicitly.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected final void register() {
		List<SuggestHandler<Object>> suggestHandlers ;
		if (handlers.containsKey(getName())) {
			suggestHandlers = (List<SuggestHandler<Object>>) handlers.get(getName());
			if(suggestHandlers.contains((SuggestHandler<Object>) this)){
				throw new RuntimeException("Duplicate Suggest Handler with name : " + getType());
			}
			else{
				suggestHandlers.add((SuggestHandler<Object>) this);
				handlers.put(getName(), suggestHandlers);
			}
		} else {
			suggestHandlers = new ArrayList<SuggestHandler<Object>>();
			suggestHandlers.add((SuggestHandler<Object>) this);
			handlers.put(getName(), suggestHandlers);
		}
		
		//System.out.println("REgistered suggest handlers :" + handlers.toString());
	}
	
	/**
	 * Mention the suggest handler type which will be used for registration.
	 * The type must be unique for each and every suggest handler.
	 * The type name must be used to fetch suggest handler
	 * @return
	 */
	protected abstract SuggestHandlerType getType();
	
	protected abstract String getName();

	
	/**
	 * Suggest Handler of prefix Suggest can be fetched by passing the suggest handler name
	 * @param name
	 * @returnO
	 */
	public static List<SuggestHandler<Object>> getSuggester(String name) {
		List<SuggestHandler<Object>> suggesters;
		suggesters = handlers.get(name);
		if(suggesters == null || suggesters.size() <= 0) {
			throw new SearchException(HttpStatus.BAD_REQUEST, "Invalid Type");
		}
		return suggesters;
	}
	
}
