package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.List;

public class Scope implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2962322595275238671L;
    private String key;
    private List<String> idList;
    private List<String> targetNames;
    private List<String> titles;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public List<String> getIdList() {
        return idList;
    }
    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
	public List<String> getTargetNames() {
		return targetNames;
	}
	public void setTargetNames(List<String> targetNames) {
		this.targetNames = targetNames;
	}
	public List<String> getTitles() {
		return titles;
	}
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	@Override
	public String toString() {
		return "{key='" + getKey() + '\'' + ", targetNames='" + getTargetNames() + '\'' + '}';
	}
}