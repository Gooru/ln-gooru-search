package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class EsSuggestDslQueryBuildProcessor extends SearchProcessor<SearchData, Object> {

	protected void buildQuery(SearchData searchData) {
		String activationFlag = getSearchSetting("search." + searchData.getType().toLowerCase() + ".query.suggest.active","false");
		if(!Boolean.parseBoolean(activationFlag)){
			return;
		}
		
		String query = searchData.getOriginalQuery();
		if(query.equalsIgnoreCase("*:*") || query.equalsIgnoreCase("*") || query.trim().length() == 0){
			return;
		}
		
		Map<String, Object> suggestQuery = constructPhraseQuery(searchData);
		searchData.getQueryDsl().put("suggest", suggestQuery);
	}

	private Map<String, Object> constructPhraseQuery(SearchData searchData) {
		Map<String, Object> gooruSuggest = new HashMap<String, Object>(1);
		Map<String, Object> phrase = new HashMap<String, Object>(1);
		String queryField = getSearchSetting("search." + searchData.getType().toLowerCase() + ".query.suggest.field","_all");
		phrase.put("field", queryField);
		phrase.put("size", 3);
		phrase.put("real_word_error_likelihood", 0.95);
		phrase.put("max_errors", 0.9);
		phrase.put("gram_size", 2);
		HashMap<String, Object> directGenerator = new HashMap<String, Object>(1);
		directGenerator.put("field", queryField);
		directGenerator.put("suggest_mode", "always");
		directGenerator.put("min_word_len", 1);
		ArrayList<Map<String, Object>> directGenerators = new ArrayList<Map<String, Object>>();
		directGenerators.add(directGenerator);
		Map<String, Object> highlight = new HashMap<String, Object>(1);
		highlight.put("pre_tag", "<b>");
		highlight.put("post_tag", "</b>");
		phrase.put("direct_generator", directGenerators);
		phrase.put("highlight", highlight);
		Map<String, Object> phraseQuery = new HashMap<String, Object>(1);
		phraseQuery.put("phrase",phrase);
		phraseQuery.put("text", searchData.getOriginalQuery());
		gooruSuggest.put("gooru_suggest", phraseQuery);
		return gooruSuggest;
	}

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		buildQuery(searchData);
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.EsSuggestDslQueryBuild;
	}
}
