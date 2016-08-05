package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.HashMap;
import java.util.Map;

public class FacetFilterAliasConstants {

	
	static Map<String, String> fields = new HashMap<String, String>();
	
	static {
		fields.put("taxonomyGrade", "grade");
		fields.put("attribution", "resourceSource.attribution");
		fields.put("license", "license.name");
		fields.put("standards", "taxonomy.standards");
		fields.put("taxonomySubject", "taxonomy.subject.label");
		fields.put("taxonomy.subject", "taxonomy.subject.label");
		fields.put("subject", "taxonomy.subject.label");
		fields.put("course", "taxonomy.course.label");
		fields.put("unit", "taxonomy.unit.label");
		fields.put("topic", "taxonomy.topic.label");
		fields.put("lesson", "taxonomy.lesson.label");
	}

	public static String getFields(String key) {
		return fields.get(key);
	}
	
}
