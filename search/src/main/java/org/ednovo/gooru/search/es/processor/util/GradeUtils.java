/**
 * 
 */
package org.ednovo.gooru.search.es.processor.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ednovo.gooru.search.es.constant.Constants;

/**
 * @author SearchTeam
 *
 */
public class GradeUtils implements Constants {
	
	private static final String GRADE_MAX_VAL = "16";
	
	public static List<String> parseGrade(String value) {
		List<String> filter = new ArrayList<String>();
		if (value.contains(",")) {
			if (value.contains("Empty")) {
				filter.add(",null");
			}
			for (String filterData : value.split(",")) {
				if(filterData.matches("\\d*[a-z]{2,3}")){
					filterData = (String)filterData.substring(0, 2) + "-"+ GRADE_MAX_VAL;
				}
				if (filterData.contains("-")){
					 if(filterData.toLowerCase().contains("pre-k")) {
						filter.add("Pre-K");
					 } 
					 else {
					    String[] values = filterData.split("-");
					    int start = 1;
					    if (values[0].equalsIgnoreCase("K")) {
						   filter.add("Kindergarten");
					    } else {
						start = Integer.parseInt(values[0]);
					    }
					    int stop = Integer.parseInt(values[1]);
					    for (int i = start; i <= stop; i++) {
						   filter.add(i + "");
				    	}
			        }
				} else {
					if (filterData.equalsIgnoreCase("Empty")){
						continue;
					} else if (filterData.equalsIgnoreCase("H")) {
						filter.add("Higher Education");
					} else {
						filter.add(filterData);
					}
				}
			}
		} else if (value.contains("-")) {
			
			 if(value.toLowerCase().contains("pre-k")) {
					filter.add("Pre-K");
			 } 
			 
			 else {
				String[] values = value.split("-");
				int start = 1;
				if (values[0].equalsIgnoreCase("K")) {
					filter.add("Kindergarten");
				} else {
					start = Integer.parseInt(values[0]);
				}
				int stop = Integer.parseInt(values[1]);
				for (int i = start; i <= stop; i++) {
					filter.add(i + "");
				}
			 }
		} else {
			if (value.contains("Empty")) {
				filter.add(",null");
			} else if (value.equalsIgnoreCase("H")) {
				filter.add("Higher Education");
			} else {
				if(value.matches("\\d*[a-z]{2,3}")){
					value = (String)value.substring(0, 2) + "-"+ GRADE_MAX_VAL;
					if(value.contains("-")){
						String[] values = value.split("-");
						int start = Integer.parseInt(values[0]);
						int stop = Integer.parseInt(values[1]);
						for (int i = start; i <= stop; i++) {
							filter.add(i + "");
						}
					}
				}
				else{
					filter.add(value);
				}
		  }
		}

		return filter;
	}
	
	public static Set<String> extractGrades(String grades) {
		Set<String> gradeSet = new HashSet<String>();
		for (String grade : grades.split(COMMA)) {
			if (!(grade = (grade.trim())).equalsIgnoreCase(NULL_STRING)) {
				if (grade.contains(HYPHEN)) {
					if (grade.toLowerCase().contains(PRE_K_LOWER_CASE)) {
						gradeSet.add(PRE_K_UPPER_CASE);
					} else {
						String[] values = grade.split(HYPHEN);
						int start = 1;
						if (values[0].equalsIgnoreCase(K_UPPER_CASE)) {
							gradeSet.add(KINDERGARTEN);
						} else {
							start = Integer.parseInt(values[0]);
						}
						int stop = Integer.parseInt(GRADE_MAX_VAL);
						if(values[1].toLowerCase().matches(HIGHER_EDUCATION_MATCH)){
							gradeSet.add(HIGHER_EDUCATION);
						} else {
							stop = Integer.parseInt(values[1]);
						}
						for (int i = start; i <= stop; i++) {
							gradeSet.add(i + EMPTY_STRING);
						}
					}
				} else {
					if (grade.toLowerCase().matches(HIGHER_EDUCATION_MATCH)) {
						gradeSet.add(HIGHER_EDUCATION);
					} else if (grade.equalsIgnoreCase(K_UPPER_CASE)) {
						gradeSet.add(KINDERGARTEN);
					} else {
						gradeSet.add(grade);
					}
				}
			}
		}
		return gradeSet;
	}

}
