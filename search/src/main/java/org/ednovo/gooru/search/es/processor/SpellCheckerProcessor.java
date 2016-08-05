/**
 *
 */
package org.ednovo.gooru.search.es.processor;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.springframework.stereotype.Component;


/**
 * @author SearchTeam
 */
@Component
public class SpellCheckerProcessor extends SearchProcessor<SearchData, Object> {

  @Override
  public void process(SearchData searchData, SearchResponse<Object> response) {

    try {

      String searchDataType =  EsIndex.RESOURCE.toString(); //searchData.getIndexType().toString();

      Boolean spellCheckerActive = getSettingAsBoolean("S_" + searchDataType + "_QUERY_SPELLCHECKER_ACTIVE");
      if (!spellCheckerActive) {
        return;
      }
      if(logger.isDebugEnabled()){
    	  logger.debug("SpellChecker Processor attempting spell check for: " + searchData.getQueryString());
      }

      MapWrapper<Object> parameters = searchData.getParameters();

      //verify if Spell Check needs to be performed or not
      if (parameters.getBoolean(DISABLE_SPELLCHECK) == null || parameters.getBoolean(DISABLE_SPELLCHECK)) {
    	  if(logger.isDebugEnabled()){  
    		  logger.debug("Skipping SpellChecker Processor disableSpellCheck parameter is null or true!");
    	  }
        return;
      }

      //do standard validation first, as it seems to be modifying the query string.
      if (standardValidateLite(searchData)) {
    	  if(logger.isDebugEnabled()){  
    		  logger.debug("SpellChecker Processor detected a possible standard in query string...!");
    	  }
        try {
        	//disabled while removing core-api jar
          //check whether the code exists in the db, if not, there is a possibility of a spell mistake in the standard query
          /*Code foundCode = taxonomyRepository.findCodeByTaxCode(searchData.getQueryString());
          if(foundCode!=null){
        	 if(logger.isDebugEnabled()){  
        		 logger.debug("SpellChecker Processor found a matching standard code from the taxonomy repository, returning as is!");
        	 }
            return;
          }*/
        } catch (Exception e) {
          logger.error("SpellChecker Processor encountered an error while checking for taxonomy code!", e);
        }
      }

      //TODO:also check for chem reps?
      if (searchData.getQueryString().equals("*") || searchData.getQueryString().equals("*:*") ||
          StringUtils.trimToEmpty(searchData.getQueryString()).length() == 0 || emailValidate(searchData.getQueryString().toLowerCase()) ||
          uuidValidate(searchData.getQueryString().toLowerCase()) || chemicalFormulaValidate(searchData) || libararyNameValidate(searchData.getQueryString().toLowerCase())) {
    	  if(logger.isDebugEnabled()){  
    		  logger.debug("Skipping SpellChecker Processor due to special cases for query string!");
    	  }
        return;
      }

      String originalQueryString = searchData.getQueryString();
      String[] originalQueryStringTokens = originalQueryString.split("[^a-zA-Z0-9-.\\']");


      //check multi-word spell check active flag
      Boolean multiWordSpellCheckActive = getSettingAsBoolean("S_" + searchDataType + "_QUERY_SPELLCHECKER_MULTI_WORDS_ACTIVE");
      if (!multiWordSpellCheckActive && originalQueryStringTokens.length > 1) {
    	  if(logger.isDebugEnabled()){  
    		  logger.debug("Skipping SpellChecker Processor due to multiple words or phrase check inactive!");
    	  }
        return;
      }
      SearchData spellCheckerRequest = new SearchData();
      spellCheckerRequest.setPretty(searchData.getPretty());
      spellCheckerRequest.setIndexType(EsIndex.RESOURCE);//set to resource index explicitly
      spellCheckerRequest.setType(searchDataType);
      spellCheckerRequest.setQueryString(originalQueryString);
      if(logger.isDebugEnabled()){
    	  logger.debug("Calling SpellChecker with query: " + originalQueryString + " against index: " + searchDataType);
      }
      Map<String, String> searchResponse =
          (Map<String, String>) SearchHandler.getSearcher(SearchHandlerType.SPELLCHECKER.name()).search(spellCheckerRequest).getSearchResults();

      //parse response
      boolean hasCorrectedSpelling = false;
      StringBuilder resultStringBuilder = new StringBuilder();
      String result = "";

      if (searchResponse != null && !searchResponse.isEmpty()) {
        int count = 1;
        for (String token : originalQueryStringTokens) {
          String correctedSpelling = searchResponse.get(token.toLowerCase());
          if (correctedSpelling != null && (correctedSpelling = correctedSpelling.trim()).length() > 0) {
            resultStringBuilder.append(correctedSpelling);
          } else {
            resultStringBuilder.append(token);
          }
          count++;
          if (count > originalQueryStringTokens.length)
            break;
          resultStringBuilder.append(" ");
        }
        result = resultStringBuilder.toString();
        if ((result != null && (result = result.trim()).length() > 0) &&
            (!originalQueryString.equalsIgnoreCase(result.trim()))) {
          hasCorrectedSpelling = true;
        }
      }
      //overwrite only if it has a corrected spelling
      if (hasCorrectedSpelling) {
        searchData.setQueryString(result);
        searchData.setSpellCheckQueryString(result);
        if(logger.isDebugEnabled()){
        	logger.debug("SpellChecker, original query: " + originalQueryString + ", spell corrected query: " + result);
        }
      }else {
    	if(logger.isDebugEnabled()){  
    		logger.debug("SpellChecker no spelling corrected for original query: " + originalQueryString);
    	}
      }
    } catch (Exception e) {
      logger.error("There was an error performing spell check: ", e);
    }

  }

  @Override
  protected SearchProcessorType getType() {
    return SearchProcessorType.SpellChecker;
  }

}
