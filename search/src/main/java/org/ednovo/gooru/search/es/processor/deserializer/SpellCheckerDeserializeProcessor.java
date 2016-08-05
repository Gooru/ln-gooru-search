package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.SearchSettingType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class SpellCheckerDeserializeProcessor extends SearchProcessor<SearchData, Map<String, String>> {



  @SuppressWarnings("unchecked")
  @Override
  public void process(SearchData searchData, SearchResponse<Map<String, String>> response) {

    Map<String, Object> result = null;

    try {
      Double scoreThreshold = getScoreThreshold();
      Integer frequencyThreshold = getFrequencyThreshold();
      LOG.info("SpellChecker deserializer processor, score threshold=" + scoreThreshold.toString() + ", frequency threshold=" + frequencyThreshold.toString());
      result = SERIAILIZER.readValue(searchData.getSearchResultText(), new TypeReference<Map<String, Object>>() { });
      Map suggest = (Map) result.get(SUGGEST);
      if (suggest != null) {
        List checkSuggestionList = (List) suggest.get(CHECK_SUGGEST);
        if (checkSuggestionList != null) {

          Map<String, String> correctedSpelling = new HashMap<String, String>();

          for (int i = 0; i < checkSuggestionList.size(); i++) {
            Map checkSuggestionResponse = (Map) checkSuggestionList.get(i);
            if (checkSuggestionResponse == null) {
              continue;//skip to next
            }
            List optionsList = (List) checkSuggestionResponse.get(OPTIONS);
            if (optionsList == null || optionsList.size() == 0) {//use original
              correctedSpelling.put((String) checkSuggestionResponse.get(TEXT), (String) checkSuggestionResponse.get(TEXT));
            } else {//update with suggestion
              Map topOption = (Map) optionsList.get(0);
              Double score = (Double) topOption.get(SCORE);
              Integer freq = (Integer) topOption.get(FREQ);
              //replace the text only if score and freq meet a certain threshold
              if (score > scoreThreshold && freq > frequencyThreshold) {
                String topOptionText = (String) topOption.get(TEXT);
                topOptionText = removeSpecialChars(topOptionText); //remove special chars
                correctedSpelling.put((String) checkSuggestionResponse.get(TEXT), topOptionText);
              } else {//keep original text
                correctedSpelling.put((String) checkSuggestionResponse.get(TEXT), (String) checkSuggestionResponse.get(TEXT));
              }
            }
          }
          response.setSearchResults(correctedSpelling);
        }
      }
    } catch (Exception e) {
      LOG.error("SpellChecker deserializer processor encountered an exception ", e);
    }

  }

  private Double getScoreThreshold() {
    Double scoreThreshold = 0.0;
    try {
      String scoreThresholdString = getSetting(SearchSettingType.S_RESOURCE_QUERY_SPELLCHECKER_SCORE_THRESHOLD);
      scoreThreshold = Double.valueOf(scoreThresholdString);
    } catch (Exception e) {
      LOG.error("SpellChecker deserializer processor encountered an exception while parsing score threshold ", e);
    }
    return scoreThreshold;
  }

  private Integer getFrequencyThreshold() {
    Integer frequencyThreshold = 0;
    try {
      String frequencyThresholdString = getSetting(SearchSettingType.S_RESOURCE_QUERY_SPELLCHECKER_FREQ_THRESHOLD);
      frequencyThreshold = Integer.valueOf(frequencyThresholdString);
    } catch (Exception e) {
      LOG.error("SpellChecker deserializer processor encountered an exception while parsing frequency threshold ", e);
    }
    return frequencyThreshold;
  }

  private String removeSpecialChars(String topOptionText) {
    boolean possibleStandardsFound = false;
    try {
      possibleStandardsFound = matchesStandardsPattern(topOptionText);
      if (!possibleStandardsFound) {
        topOptionText = topOptionText.replaceAll("[^a-zA-Z0-9]", "");
      }
    } catch (Exception e) {
      LOG.error("SpellChecker deserializer processor encountered an exception while removing special chars", e);
    }
    return topOptionText;
  }

  @Override
  protected SearchProcessorType getType() {
    return SearchProcessorType.SpellCheckerDeserializer;
  }
}
