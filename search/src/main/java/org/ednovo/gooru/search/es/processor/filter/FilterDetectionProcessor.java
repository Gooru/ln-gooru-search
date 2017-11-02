/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.GradeUtils;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class FilterDetectionProcessor extends SearchProcessor<SearchData, Object> {

	
	protected static void validatePattern(SearchData searchData,
			Map<String, String> patterns,
			String key) {

		String query = searchData.getQueryString();

		for (Entry<String, String> category : patterns.entrySet()) {
			Matcher matcher = Pattern.compile(category.getValue()).matcher(query);
			if (matcher.find()) {
				query = query.replaceAll(matcher.group().replaceAll(FIND_SPECIAL_CHARACTERS_REGEX, REPLACE_CHARACTERS), " ");
				String filterValue = category.getKey();
				if (key.equalsIgnoreCase(SEARCH_URL)) {
					filterValue = matcher.group();
				}
				searchData.putFilter("&^" + key, filterValue);
				if (StringUtils.isBlank(query)) {
					query = "*:*";
				}
				searchData.setQueryString(query);
				return;
			}
		}
	}
	private void applyFilterColonQuery(SearchData searchData){
		long start = System.currentTimeMillis();
		String orgQuery = searchData.getQueryString();
		List<String> filterValueList = new ArrayList<String>();
		if(orgQuery.contains(":")){
			String[] queryArr = orgQuery.split(":");
			if(queryArr == null || queryArr.length != 2){
				return;
			}
			String filterName = queryArr[0];
			String queryFilterValue = queryArr[1];
			boolean skipColonFilter = true;

			Map<String, String> filterPatternMap = SearchSettingService.getFilterDetection(SEARCH_FILTER_NAME);
			for(Entry<String, String> filterPatternSet : filterPatternMap.entrySet()){
				Matcher matcher = Pattern.compile(filterPatternSet.getValue()).matcher(filterName);
				if(matcher.find()){
					orgQuery = filterName.replaceAll(matcher.group().replaceAll(FIND_SPECIAL_CHARACTERS_REGEX, REPLACE_CHARACTERS), " ");
					filterName = filterPatternSet.getKey();
					skipColonFilter = false;
					break;
				}
			}	
			if(!skipColonFilter){
				String filterDetectionKey = filterName;
				if(filterDetectionKey.equalsIgnoreCase("grade")){
					filterDetectionKey = filterDetectionKey+UNDERSCORE_COLON;
				}
				Map<String, String> patternMap = SearchSettingService.getFilterDetection(filterDetectionKey);
				
				if(queryFilterValue != null){
					String [] filterValues = queryFilterValue.split(",");
					for(String filterValue : filterValues){
						if(patternMap != null && patternMap.size() > 0){
							for(Entry<String, String> patternSet : patternMap.entrySet()){
								Matcher matcher = Pattern.compile(patternSet.getValue()).matcher(filterValue);
								if(matcher.find()){
									if(filterValueList.contains(filterValue)){
										filterValueList.remove(filterValue);
									}
									filterValueList.add(patternSet.getKey());
									break;
								}
								else {
									if(!filterValueList.contains(filterValue)){
										filterValueList.add(filterValue);
									}
								}
							}
						}
						else {
							filterValueList.add(filterValue);
						}
					}
				}
				
				if(filterValueList.size() > 0){
					if(filterName.equalsIgnoreCase(SEARCH_GRADE)){
						filterValueList = GradeUtils.parseGrade(StringUtils.join(filterValueList, ","));
					}
					StringBuilder filterValueBuilder = new StringBuilder();
					filterValueList.forEach(filterValue -> {
						if (filterValueBuilder.length() > 0) {
							filterValueBuilder.append(COMMA);
						}
						filterValueBuilder.append(filterValue);
					});
					searchData.putFilter("&^"+filterName, filterValueBuilder.toString().split(COMMA));
					if(StringUtils.trim(orgQuery).length() == 0){
						orgQuery = "*";
					}
					searchData.setQueryString(orgQuery);
				}
			}
			}
		if(logger.isDebugEnabled()){	
			logger.debug("Elapsed time to compelete colon filter process :" +(System.currentTimeMillis() - start) + " ms");
		}
	}

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		applyFilterColonQuery(searchData);

		
		List<String> keys = SearchSettingService.getListByName("search." + searchData.getType().toLowerCase() + ".filter_detection");
		if (keys != null) {
			for (String key : keys) {
				Map<String, String> pattern = SearchSettingService.getFilterDetection(key);
				validatePattern(searchData, pattern, key);
			}
		}
		
		String orgQuery = searchData.getQueryString().toLowerCase().trim();
		/*if(libraryName.get(orgQuery) != null){
			searchData.setQueryString("*");
			searchData.putFilter("&^libraryName", libraryName.get(orgQuery));
		}*/
		
		
		/*String orgQuery = searchData.getQueryString().toLowerCase();
		String fltSource = null;
		if (searchData.getParameters().containsKey("flt.source")) {
			fltSource = searchData.getParameters().getString("flt.source");
		}
		if (fltSource == null || fltSource.trim().length() == 0) {
			for (String map : ElasticsearchFactory.mapResourceContentProvider.keySet()) {
				if (map.length() > 0 &&  orgQuery.contains(map)){
					String type = ElasticsearchFactory.mapResourceContentProvider.get(map);
					if (type.equalsIgnoreCase("publisher")){
						searchData.putFilter("&^publisher", map);
					} else {
						searchData.putFilter("&^aggregator", map);
					}
					orgQuery = orgQuery.replace(map, "").trim();
					if (orgQuery.length() == 0 ) {
						orgQuery = map;
					}
					searchData.setQueryString(orgQuery);
					break;
				}
			}
		}*/
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.FilterDetection;
	}
	
	public static void main(String agrs []){
		String query = "cells grade:1st std.";
		String regex ="(1|1st|first)(std.|(s|S)tandard|(s|S)tandards)";

		Matcher matcher = Pattern.compile(regex).matcher(query);
		if (matcher.find()) {
			query = query.replaceAll(matcher.group().replaceAll(FIND_SPECIAL_CHARACTERS_REGEX, REPLACE_CHARACTERS), " ");
		
		}
	}
	
}
