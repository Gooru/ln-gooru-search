package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.Map;

public class SearchRequestBody implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2962322595275238671L;

    private Integer pageSize = 8;
    private Integer length = 8;
    private Integer startAt = 0;
    private Integer pageNum = 1;
    private Integer start = 1;
    private String pretty = "0";
    private String query;
    private String q;
    private String excludeAttributes;
    private Boolean userDetails = false;
    private Boolean isCrosswalk = true;
    private Boolean disableSpellCheck = false;
    private Scope scope;
    private Map<String, Object> filters;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getPretty() {
        return pretty;
    }

    public void setPretty(String pretty) {
        this.pretty = pretty;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getExcludeAttributes() {
        return excludeAttributes;
    }

    public void setExcludeAttributes(String excludeAttributes) {
        this.excludeAttributes = excludeAttributes;
    }

    public Boolean getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Boolean userDetails) {
        this.userDetails = userDetails;
    }

    public Boolean getIsCrosswalk() {
        return isCrosswalk;
    }

    public void setIsCrosswalk(Boolean isCrosswalk) {
        this.isCrosswalk = isCrosswalk;
    }

    public Boolean getDisableSpellCheck() {
        return disableSpellCheck;
    }

    public void setDisableSpellCheck(Boolean disableSpellCheck) {
        this.disableSpellCheck = disableSpellCheck;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

}
