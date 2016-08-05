/**
 * 
 */
package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.SearchSettingType;
import org.ednovo.gooru.search.es.filter.AndFilter;
import org.ednovo.gooru.search.es.filter.TermsFilter;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.FilterBuilderUtils;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public final class LibraryEsDslQueryBuildProcessor extends SearchProcessor<SearchData, Object> {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		searchData.getQueryDsl().put("size", searchData.getSize()).put("fields", getSetting(SearchSettingType.S_LIBRARY_FIELDS).split(","));
		String codeId = (String) (searchData.getParameters().getObject("flt.course") != null ? searchData.getParameters().getObject("flt.course") : searchData.getParameters().getObject("flt.unit"));
		searchData.getQueryDsl().put("query", new TermsFilter("id", codeId.split(",")));
		searchData.getFilters().remove("&^course");
		searchData.getFilters().remove("&^unit");
		if (searchData.getFilters() != null && searchData.getFilters().size() > 0) {
			Object boolQuery = FilterBuilderUtils.buildFilters(searchData.getFilters());
			Map<String, Object> boolAsMap = new HashMap<String, Object>(1);
			boolAsMap.put("bool", boolQuery);
			if (boolQuery != null) {
				searchData.getQueryDsl().put("filter", boolAsMap);
			}
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LibraryEsDslQueryBuild;
	}

}