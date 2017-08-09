package org.ednovo.gooru.suggest.v3.handler;

import java.io.Serializable;

public class SuggestContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userUid;
	
	private String contentGooruOid;
	
	private String event;
	
	private String searchTerm;
	
	private String quizSessionId;
	
	private String parentGooruOid;
	
	private String category;
		
	private String studentId;
	
	private String classId;
	
	private String assignmentId;
	
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setContentGooruOid(String contentGooruOid) {
		this.contentGooruOid = contentGooruOid;
	}

	public String getContentGooruOid() {
		return contentGooruOid;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent() {
		return event;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setQuizSessionId(String quizSessionId) {
		this.quizSessionId = quizSessionId;
	}

	public String getQuizSessionId() {
		return quizSessionId;
	}

	public void setParentGooruOid(String parentGooruOid) {
		this.parentGooruOid = parentGooruOid;
	}

	public String getParentGooruOid() {
		return parentGooruOid;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassId() {
		return classId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

}
