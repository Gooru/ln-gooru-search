/**
 * 
 */
package org.ednovo.gooru.suggest.v3.model;

import org.ednovo.gooru.search.es.model.SearchData;

/**
 * @author SearchTeam
 * 
 */

public class SuggestData extends SearchData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127315652940617808L;

	private SuggestContextData suggestContextData;
	
	private Boolean isInternalSuggest;

	private String suggestInputType;
	
	private Boolean inputTypeInternalCode = false;
	
	public SuggestContextData getSuggestContextData() {
		return suggestContextData;
	}

	public void setSuggestContextData(SuggestContextData suggestContextData) {
		this.suggestContextData = suggestContextData;
	}

	public Boolean getIsIntenralSuggest() {
		return isInternalSuggest;
	}

	public void setIsInternalSuggest(Boolean isInternalSuggest) {
		this.isInternalSuggest = isInternalSuggest;
	}

	public String getSuggestInputType() {
		return suggestInputType;
	}

	public void setSuggestInputType(String suggestInputType) {
		this.suggestInputType = suggestInputType;
	}
	
	public Boolean getInputTypeInternalCode() {
		return inputTypeInternalCode;
	}

	public void setInputTypeInternalCode(Boolean inputTypeInternalCode) {
		this.inputTypeInternalCode = inputTypeInternalCode;
	}

}
