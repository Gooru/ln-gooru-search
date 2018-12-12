/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ContentUserPreferenceProcessor extends SearchProcessor<SearchData, Object> {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		if (searchData.getParameters() == null || !(searchData.getParameters() != null && (searchData.getParameters().containsKey(FLT_LANGUAGE) || searchData.getParameters().containsKey(FLT_LANGUAGE_ID)))) {
			String langPreference = searchData.getUserLanguagePreference();
			if (langPreference != null) {
				String[] preferredLanguages = langPreference.split(COMMA);
				if (preferredLanguages.length > 0) {
					searchData.putFilter(LANGUAGE_ID, preferredLanguages);
					if (preferredLanguages.length > 1) {
						float langWeight = preferredLanguages.length;
						for (int i = 0; i < preferredLanguages.length; i++) {
							searchData.getCustomFilters().add(IndexFields.PRIMARY_LANG_ID + CARET_SYMBOL + langWeight + COLON + preferredLanguages[i]);
							langWeight--;
						}
					}
				}
			} else if (searchData.getUser().getGooruUId().equalsIgnoreCase(ANONYMOUS) && (searchData.getType().equalsIgnoreCase(TYPE_COURSE) 
					   && searchData.getFilters() != null && searchData.getFilters().containsKey(FLT_COURSE_TYPE) && searchData.getFilters().get(FLT_COURSE_TYPE).toString().equalsIgnoreCase(PublishedStatus.FEATURED.getStatus()))) {
				searchData.putFilter(LANGUAGE_ID, SearchSettingService.getSettingAsInteger(DEFAULT_GOORU_LANG_ID, 1));
			}
		}
	}

	protected String getSearchUserGrades(String profileGrades) {
		String[] grades = profileGrades.split(",");
		String gradeString = "0";
		for (String grade : grades) {
			grade = grade.trim();
			if (grade.length() == 0 || grade.equals("null") || grade.equals("-")) {
				continue;
			}
			gradeString += "," + grade;
			if (StringUtils.isNumeric(grade)) {
				int gradeInt = Integer.parseInt(grade);
				if (gradeInt - 1 > 0) {
					gradeString += "," + (gradeInt - 1);
				}
				if (gradeInt - 2 > 0) {
					gradeString += "," + (gradeInt - 2);
				}
				if (gradeInt - 3 > 0) {
					gradeString += "," + (gradeInt - 3);
				}
			}
		}
		return gradeString;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ContentUserPreference;
	}

}
