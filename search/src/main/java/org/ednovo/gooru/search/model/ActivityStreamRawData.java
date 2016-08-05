package org.ednovo.gooru.search.model;

public class ActivityStreamRawData {

	private String userUid;
	
	private String username;
	
	private String eventName;
	
	private String collectionGooruOid;
	
	private String resourceGooruOid;
	
	private String score;
	
	private String sessionId;
	
	private String firstAttemptStatus;
	
	private String answerStatus;

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setCollectionGooruOid(String collectionGooruOid) {
		this.collectionGooruOid = collectionGooruOid;
	}

	public String getCollectionGooruOid() {
		return collectionGooruOid;
	}

	public void setResourceGooruOid(String resourceGooruOid) {
		this.resourceGooruOid = resourceGooruOid;
	}

	public String getResourceGooruOid() {
		return resourceGooruOid;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {
		return score;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setFirstAttemptStatus(String firstAttemptStatus) {
		this.firstAttemptStatus = firstAttemptStatus;
	}

	public String getFirstAttemptStatus() {
		return firstAttemptStatus;
	}

	public void setAnswerStatus(String answerStatus) {
		this.answerStatus = answerStatus;
	}

	public String getAnswerStatus() {
		return answerStatus;
	}
}
