package org.ednovo.gooru.search.model;

import java.util.List;

public class ConceptNodePerformanceDTO {

	private List<ConceptNodePreTest> pre_test;
	private List<ConceptNodePostTest> post_test;
	private List<ConceptNodeBenchmark> benchmark;
	private List<ConceptNodeBadge> badges;

	public List<ConceptNodePreTest> getPre_test() {
		return pre_test;
	}

	public void setPre_test(List<ConceptNodePreTest> pre_test) {
		this.pre_test = pre_test;
	}

	public List<ConceptNodePostTest> getPost_test() {
		return post_test;
	}

	public void setPost_test(List<ConceptNodePostTest> post_test) {
		this.post_test = post_test;
	}

	public List<ConceptNodeBenchmark> getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(List<ConceptNodeBenchmark> benchmark) {
		this.benchmark = benchmark;
	}

	public List<ConceptNodeBadge> getBadges() {
		return badges;
	}

	public void setBadges(List<ConceptNodeBadge> badges) {
		this.badges = badges;
	}
}
