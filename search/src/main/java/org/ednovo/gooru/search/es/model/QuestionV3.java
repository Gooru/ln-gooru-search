package org.ednovo.gooru.search.es.model;

import java.util.Set;

import org.ednovo.gooru.search.responses.v3.ContentSearchResult;

public class QuestionV3 extends ContentSearchResult {
	
	private static final long serialVersionUID = -5780952748437855414L;
		
	private String explanation;
	
	private Set<String> answer;

	private Set<Hint> hints;

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public Set<String> getAnswer() {
		return answer;
	}

	public void setAnswer(Set<String> answer) {
		this.answer = answer;
	}

	public Set<Hint> getHints() {
		return hints;
	}

	public void setHints(Set<Hint> hints) {
		this.hints = hints;
	}

}
