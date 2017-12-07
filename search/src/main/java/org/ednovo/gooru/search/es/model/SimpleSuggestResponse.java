package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.Map;

public class SimpleSuggestResponse implements Serializable {

	private static final long serialVersionUID = 658669415425318456L;

	private String id;
	private String title;
	private String format;
	private String subformat;
	private String thumbnail;
	private Map<String, Object> metadata;
	private Map<String, Object> taxonomy;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSubformat() {
		return subformat;
	}
	public void setSubformat(String subformat) {
		this.subformat = subformat;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}

}
