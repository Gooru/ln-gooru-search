package org.ednovo.gooru.search.domain.service;

public class IndexDataTerm {
	private String fieldName;
	private String fieldContent;
	private Object fieldContentObject;
	private boolean fieldStore;
	private boolean fieldAnalyzed;
	private float fieldBoost;
	private boolean fieldMapping = false;

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldContent() {
		return fieldContent;
	}
	public void setFieldContent(String fieldContent) {
		this.fieldContent = fieldContent;
	}
	public boolean isFieldStore() {
		return fieldStore;
	}
	public void setFieldStore(boolean fieldStore) {
		this.fieldStore = fieldStore;
	}
	public boolean isFieldAnalyzed() {
		return fieldAnalyzed;
	}
	public void setFieldAnalyzed(boolean fieldAnalyzed) {
		this.fieldAnalyzed = fieldAnalyzed;
	}
	public float getFieldBoost() {
		return fieldBoost;
	}
	public void setFieldBoost(float fieldBoost) {
		this.fieldBoost = fieldBoost;
	}
	public Object getFieldContentObject() {
		return fieldContentObject;
	}
	public void setFieldContentObject(Object fieldContentObject) {
		this.fieldContentObject = fieldContentObject;
	}

	public boolean isFieldMapping() {
		return fieldMapping;
	}
	public void setFieldMapping(boolean fieldMapping) {
		this.fieldMapping = fieldMapping;
	}	
}
