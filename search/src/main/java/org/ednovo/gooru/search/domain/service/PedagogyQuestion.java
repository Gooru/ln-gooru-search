package org.ednovo.gooru.search.domain.service;

import java.util.Set;

import org.ednovo.gooru.search.es.model.Answer;
import org.ednovo.gooru.search.es.model.Hint;
/**
 * @author Renuka
 * 
 */
public class PedagogyQuestion extends PedagogyContentSearchResult {
	
	private static final long serialVersionUID = -5780952748437855414L;

	private static final String INDEX_TYPE = "question";
		
	private String questionText;

	private String explanation;

	private String description;
	
	private Set<Answer> answers;

	private Set<Hint> hints;

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	public Set<Hint> getHints() {
		return hints;
	}

	public void setHints(Set<Hint> hints) {
		this.hints = hints;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getIndexType() {
		return INDEX_TYPE;
	}

}
