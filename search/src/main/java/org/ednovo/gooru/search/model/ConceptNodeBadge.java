package org.ednovo.gooru.search.model;

public class ConceptNodeBadge {

	private String badge_id;
	private Integer min_performance_score;
	private Integer max_performance_score;
	private Integer min_breadth_value;
	private Integer max_breadth_value;

	public String getBadge_id() {
		return badge_id;
	}

	public void setBadge_id(String badge_id) {
		this.badge_id = badge_id;
	}

	public Integer getMin_performance_score() {
		return min_performance_score;
	}

	public void setMin_performance_score(Integer min_performance_score) {
		this.min_performance_score = min_performance_score;
	}

	public Integer getMax_performance_score() {
		return max_performance_score;
	}

	public void setMax_performance_score(Integer max_performance_score) {
		this.max_performance_score = max_performance_score;
	}

	public Integer getMin_breadth_value() {
		return min_breadth_value;
	}

	public void setMin_breadth_value(Integer min_breadth_value) {
		this.min_breadth_value = min_breadth_value;
	}

	public Integer getMax_breadth_value() {
		return max_breadth_value;
	}

	public void setMax_breadth_value(Integer max_breadth_value) {
		this.max_breadth_value = max_breadth_value;
	}

}
