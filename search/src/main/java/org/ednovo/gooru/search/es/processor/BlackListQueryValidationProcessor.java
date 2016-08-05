/**
 * 
 */
package org.ednovo.gooru.search.es.processor;



import java.util.Arrays;
import java.util.List;

import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 *
 */
@Component
public class BlackListQueryValidationProcessor extends SearchProcessor<SearchData, Object> {
	
    protected static final String BLACK_LIST_WORD_RESPONSE_MESSAGE = "Remember:This is a search engine for learning. To ensure a safe search experience, Gooru does not provide any results for the search term you entered.";
    
    private static final String BLACK_LISTED_WORDS = "search.black.listed.words";

    private static final String WILD_CARD = "*";

    private static List<String> blackListedWordsList = null;
    
	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
    String blackListedWords = getSearchSetting(BLACK_LISTED_WORDS, null);
    if(blackListedWords != null){
      blackListedWordsList = Arrays.asList(blackListedWords.split(","));
    }
    if(blackListedWordsList != null && searchData.getOriginalQuery() != null){
      if (!searchData.getOriginalQuery().equals(WILD_CARD)) {
        String[] blackWords = searchData.getOriginalQuery().split(" ");
        for (String blackWord : blackWords) {
          blackWord = blackWord.trim();
          if (blackListedWordsList.contains(blackWord.toLowerCase())) {
            throw new SearchException(HttpStatus.BAD_REQUEST, BLACK_LIST_WORD_RESPONSE_MESSAGE);
          }
        }
      }
    }
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.BlackListQueryValidation;
	}

}
