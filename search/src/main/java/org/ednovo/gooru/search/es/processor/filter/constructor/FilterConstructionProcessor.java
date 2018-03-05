/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.filter.ConstantScore;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class FilterConstructionProcessor extends SearchProcessor<SearchData, Object> {

	

	public FilterConstructionProcessor() {
		super();
		setTransactional(true);
	}

	@Override
	public void process(SearchData searchData,SearchResponse<Object> response) {
		if (searchData.getParameters() == null || searchData.getParameters().getValues().size() == 0) {
			return;
		}
		Map<String, Object> parameters = searchData.getParameters().getValues();
		if (parameters != null && parameters.size() > 0) {
			Set<String> filterKeys = parameters.keySet();
			for (String filterKey : filterKeys) {
				Object filterValues = parameters.get(filterKey);
				String[] keys = filterKey.split("\\.");
				if (filterValues.getClass().isArray() && ((String[]) filterValues).length == 1) {
					filterValues = ((String[]) filterValues)[0];
				}
				if (filterValues == null || (filterValues instanceof String && ((String) filterValues).trim().length() < 1)) {
					continue;
				}
				if (keys.length == 1) {
					processParam(searchData, response, keys[0], filterValues);
					continue;
				}
				String type = null;
				if (keys[0].equals(FLT)) {
					type = "&";
				} else if (keys[0].equals(FLT_NOT)) {
					type = "!";
				}else if(keys[0].equals(FLT_OR)) {
					type = "|";
				}else if(keys[0].equals(AGG_BY)) {
					type = "<>-";
				}
				else if (keys[0].equals(BOOST_FIELD)) {
					String[] fields = keys[1].split("\\^");
					fields[0] = SearchSettingService.getFilterAlias(fields[0]);
					Float boost = fields.length > 1 ? Float.valueOf(fields[1]) : SearchSettingService.getSettingAsFloat("search."+searchData.getType().toLowerCase()+".field."+fields[0].toLowerCase()+".custom.boost", 20.0F);
					searchData.putQuery(new ConstantScore("query",fields[0], filterValues, boost));
					continue;
				}
				if (type != null) {
					if(keys[1].startsWith(CF)) {
						String startingLetterOfKey = keys[1].substring(2, 3);
						keys[1] = INFO_DOT + startingLetterOfKey.toLowerCase() + keys[1].substring(3);
					}
					if (filterValues instanceof String && SearchSettingService.isSkipAllValueFilter(keys[1]) && ((String) filterValues).equalsIgnoreCase("all")) {
						continue;
					}
					if (processFilter(searchData, response, type, keys[1], filterValues)) {
						continue;
					}
					searchData.putFilter(type + "^" + keys[1], filterValues);
					
				}
			}
		}
	}

	protected boolean processFilter(SearchData searchData,
			SearchResponse<Object> response,
			String type,
			String key,
			Object values) {
		return false;
	}

	protected boolean processParam(SearchData searchData,
			SearchResponse<Object> response,
			String key,
			Object values) {
		return false;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.FilterConstruction;
	}

}
