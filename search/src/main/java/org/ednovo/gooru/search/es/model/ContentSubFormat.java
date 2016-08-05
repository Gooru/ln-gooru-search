package org.ednovo.gooru.search.es.model;

public enum ContentSubFormat {

	WEBPAGE("webpage"), 
	VIDEO("video"), 
	INTERACTIVE("interactive"), 
	IMAGE("image"),
	TEXT("text"), 
	AUDIO("audio"), 
	MULTIPLE_CHOICE("multiple_choice"), 
	MULTIPLE_ANSWER("multiple_answer"), 
	TRUE_FALSE("true_false"), 
	FILL_IN_THE_BLANKS("fill_in_the_blanks"), 
	OPEN_ENDED("open_ended"), 
	HOT_TEXT_REORDER("hot_text_reorder"), 
	HOT_TEXT_HIGHLIGHT("hot_text_highlight"), 
	HOT_SPOT_IMAGE("hot_spot_image"), 
	HOT_SPOT_TEXT("hot_spot_text");

	private String subFormat;

	ContentSubFormat(String subFormat) {
		this.subFormat = subFormat;
	}

	public String getSubFormat() {
		return this.subFormat;
	}

}
