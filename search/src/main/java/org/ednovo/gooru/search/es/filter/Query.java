/**
 * 
 */
package org.ednovo.gooru.search.es.filter;


public class Query {

	private String query;

	private String[] fields;

	private Float boost;

	private Integer tie_breaker;

	private String default_operator;

	private Boolean allow_leading_wildcard;

	private String analyzer;
	
	private String minimum_should_match;
	
	private Float cutoff_frequency;

	public Query() {

	}

	public Query(String query,
			String[] fields,
			Float boost) {
		setQuery(query);
		setFields(fields);
		setBoost(boost);
		
	}
	
	public Query(String query, String[] fields, Float boost, String minimumShouldMatch){
		setQuery(query);
		setFields(fields);
		setMinimum_should_match(minimumShouldMatch);
		setBoost(boost);
	}
	
	public Query(String query, String[] fields, Float boost, String minimumShouldMatch, Float cutoffFrequency){
		setQuery(query);
		setFields(fields);
		setMinimum_should_match(minimumShouldMatch);
		setBoost(boost);
		setCutoff_frequency(cutoffFrequency);
	}

	public Query(String query,
			String[] fields,
			Integer tie_breaker,
			String default_operator,
			Boolean allow_leading_wildcard) {
		setQuery(query);
		setFields(fields);
		setTie_breaker(tie_breaker);
		setDefault_operator(default_operator);
		setAllow_leading_wildcard(allow_leading_wildcard);
	}

	public Query(String query,
			String[] fields,
			Integer tie_breaker,
			String default_operator,
			Boolean allow_leading_wildcard,
			String analyzer,
			Float boost) {
		setQuery(query);
		setFields(fields);
		setTie_breaker(tie_breaker);
		setDefault_operator(default_operator);
		setAllow_leading_wildcard(allow_leading_wildcard);
		setAnalyzer(analyzer);
		setBoost(boost);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public Float getBoost() {
		return boost;
	}

	public void setBoost(Float boost) {
		this.boost = boost;
	}

	public Integer getTie_breaker() {
		return tie_breaker;
	}

	public void setTie_breaker(Integer tie_breaker) {
		this.tie_breaker = tie_breaker;
	}

	public String getDefault_operator() {
		return default_operator;
	}

	public void setDefault_operator(String default_operator) {
		this.default_operator = default_operator;
	}

	public Boolean getAllow_leading_wildcard() {
		return allow_leading_wildcard;
	}

	public void setAllow_leading_wildcard(Boolean allow_leading_wildcard) {
		this.allow_leading_wildcard = allow_leading_wildcard;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public void setMinimum_should_match(String minimum_should_match) {
		this.minimum_should_match = minimum_should_match;
	}

	public String getMinimum_should_match() {
		return minimum_should_match;
	}

	public void setCutoff_frequency(Float cutoff_frequency) {
		this.cutoff_frequency = cutoff_frequency;
	}

	public Float getCutoff_frequency() {
		return cutoff_frequency;
	}

}