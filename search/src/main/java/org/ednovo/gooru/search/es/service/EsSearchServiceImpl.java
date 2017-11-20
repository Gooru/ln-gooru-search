/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.springframework.stereotype.Service;

/**
 * @author SearchTeam
 * 
 */
@Service
public class EsSearchServiceImpl implements SearchService, Constants {
	
	public static final String ADD = "add";
	public static final String REMOVE = "remove";

	static Map<String, String> GRADE_LEVELS = new LinkedHashMap<String, String>();
	static Map<String, String> GRADES = new LinkedHashMap<String, String>();
	static Map<String, String> RESOURCE = new LinkedHashMap<String, String>();
	static Map<String, String> SCOLLECTION = new LinkedHashMap<String, String>();
	static List<Map<String, String>> RESOURCE_FORMAT = new ArrayList<Map<String,String>>();
	static List<String> SUBJECT_LIST = new ArrayList<String>(6);
	
	static {
		GRADE_LEVELS.put("K-4", "Elementary School");
		GRADE_LEVELS.put("5-8", "Middle School");
		GRADE_LEVELS.put("9-12", "High School");
		GRADE_LEVELS.put("H", "Higher Education");
		
		RESOURCE.put("Video", "Videos");
		RESOURCE.put("Website", "Websites");		
		RESOURCE.put("Interactive", "Interactives");
		RESOURCE.put("Question", "Questions");		
		RESOURCE.put("Slide", "Slides");		
		RESOURCE.put("Textbook", "Textbooks");
		RESOURCE.put("Handout", "Handouts");		
		RESOURCE.put("Lesson", "Lessons");		
		RESOURCE.put("Exam", "Exams");
		
		SCOLLECTION.put("onlyQuestion", "Only Questions");
		SCOLLECTION.put("noQuestion", "No Questions");
		SCOLLECTION.put("someQuestion", "Some Questions");
		
		GRADES.put("Pre-k", "Pre-K");
		GRADES.put("Kindergarten", "K");
		GRADES.put("1", "1");
		GRADES.put("2", "2");
		GRADES.put("3", "3");
		GRADES.put("4", "4");
		GRADES.put("5", "5");
		GRADES.put("6", "6");
		GRADES.put("7", "7");
		GRADES.put("8", "8");
		GRADES.put("9", "9");
		GRADES.put("10", "10");
		GRADES.put("11", "11");
		GRADES.put("12", "12");
		GRADES.put("13gte", "13+");
		
		SUBJECT_LIST.add("Science");
		SUBJECT_LIST.add("Math");
		SUBJECT_LIST.add("Social Sciences");
		SUBJECT_LIST.add("Language Arts");
		SUBJECT_LIST.add("Arts & Humanities");
		SUBJECT_LIST.add("Technology & Engineering");
	
	}

	
	@Override
	public SearchFilters getSearchFilters(Integer codeId, String type) {
		SearchFilters searchFilters = new SearchFilters();

		searchFilters.setSubjects(SUBJECT_LIST);

		if (type.equalsIgnoreCase(SearchType.RESOURCE.getType())) {
			searchFilters.setCategories(RESOURCE);
		} else if (type.equalsIgnoreCase(SearchType.SIMPLE_COLLECTION.getType())) {
			searchFilters.setCategories(SCOLLECTION);
		}
		searchFilters.setResourceFormat(SearchProcessor.RESOURCE_FORMAT_VALUE);

		searchFilters.setGradeLevels(GRADE_LEVELS);
		return searchFilters;
	}
	
	@Override
	public void trimInvalidExpression(SearchData searchData) {
		String query = searchData.getQueryString();
		String[] start = new String[] { "AND NOT ", "OR NOT ", "NOT AND ", "NOT OR ", "OR ", "AND " };
		String[] end = new String[] { " AND NOT", " OR NOT", " NOT AND", " NOT OR", " OR", " AND" };
		query = cutOffStart(query, start);
		query = cutOffEnd(query, end);
		searchData.setQueryString(query);
		if ((StringUtils.startsWithAny(query, start)) || (StringUtils.endsWithAny(query, end))) {
			trimInvalidExpression(searchData);
		}
	}

	private String cutOffStart(String query, String[] termsToTrim) {
		for (String termToTrim : termsToTrim) {
			query = StringUtils.removeStart(query, termToTrim);
		}
		return query;
	}
	
	private String cutOffEnd(String query, String[] termsToTrim) {
		for (String termToTrim : termsToTrim) {
			query = StringUtils.removeEnd(query, termToTrim);
		}
		return query;
	}
	
	@Override
	public void refreshGlobalTenantsInCache() {
		SearchSettingService.refreshTenants();
	}
	

}
