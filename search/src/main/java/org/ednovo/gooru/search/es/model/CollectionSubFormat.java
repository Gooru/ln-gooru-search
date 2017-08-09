package org.ednovo.gooru.search.es.model;

public enum CollectionSubFormat {
	
	PRE_TEST("pre-test"), POST_TEST("post-test"), BENCHMARK("benchmark"), BACKFILL("backfill");

	private String subFormat;

	CollectionSubFormat(String subFormat) {
		this.subFormat = subFormat;
	}

	public String getStatus() {
		return subFormat;
	}}
