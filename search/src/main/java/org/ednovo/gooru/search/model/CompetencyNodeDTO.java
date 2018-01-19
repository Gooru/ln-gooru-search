package org.ednovo.gooru.search.model;

import java.util.List;

public class CompetencyNodeDTO {

	private String id;
	private String gut_subject_id;
	private String gut_course_id;
	private String gut_domain_id;
	private String title;
	private String display_code;
	private String competency_level;
	private List<Competency> prerequisites;
	private List<SignatureItems> signature_collections;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGut_subject_id() {
		return gut_subject_id;
	}

	public void setGut_subject_id(String gut_subject_id) {
		this.gut_subject_id = gut_subject_id;
	}

	public String getGut_course_id() {
		return gut_course_id;
	}

	public void setGut_course_id(String gut_course_id) {
		this.gut_course_id = gut_course_id;
	}

	public String getGut_domain_id() {
		return gut_domain_id;
	}

	public void setGut_domain_id(String gut_domain_id) {
		this.gut_domain_id = gut_domain_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDisplay_code() {
		return display_code;
	}

	public void setDisplay_code(String display_code) {
		this.display_code = display_code;
	}

	public String getCompetency_level() {
		return competency_level;
	}

	public void setCompetency_level(String competency_level) {
		this.competency_level = competency_level;
	}

	public List<Competency> getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(List<Competency> prerequisites) {
		this.prerequisites = prerequisites;
	}
	
	public List<SignatureItems> getSignature_collections() {
		return signature_collections;
	}

	public void setSignature_collections(List<SignatureItems> signature_collections) {
		this.signature_collections = signature_collections;
	}
}
