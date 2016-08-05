package org.ednovo.gooru.search.es.model;

import java.io.Serializable;

public class Answer implements Serializable {

	private static final long serialVersionUID = -3000256378255711993L;

	private Integer answerId;

	private String answerText;
	
	private String answerType;

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
}
