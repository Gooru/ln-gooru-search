package org.ednovo.gooru.search.model;

import java.util.Map;
import java.util.HashMap;

public class ResourceUsageData {

	private String questionType;
	private String userName;
	private String gooruUId;
	private Long status;
	private Long views;
	private Long avgTimeSpent;
	private Long timeSpent;
	private Long reaction;
	private Long avgReaction;
	private Long score;
	private Long attempts;
	private Long attemptStatus;
	private Long skip;
	private Long totalInCorrectCount;
	private Long totalCorrectCount;
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGooruUId() {
		return gooruUId;
	}
	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public Long getAvgTimeSpent() {
		return avgTimeSpent;
	}
	public void setAvgTimeSpent(Long avgTimeSpent) {
		this.avgTimeSpent = avgTimeSpent;
	}
	public Long getTimeSpent() {
		return timeSpent;
	}
	public void setTimeSpent(Long timeSpent) {
		this.timeSpent = timeSpent;
	}
	public Long getReaction() {
		return reaction;
	}
	public void setReaction(Long reaction) {
		this.reaction = reaction;
	}
	public Long getAvgReaction() {
		return avgReaction;
	}
	public void setAvgReaction(Long avgReaction) {
		this.avgReaction = avgReaction;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public Long getAttempts() {
		return attempts;
	}
	public void setAttempts(Long attempts) {
		this.attempts = attempts;
	}
	public Long getAttemptStatus() {
		return attemptStatus;
	}
	public void setAttemptStatus(Long attemptStatus) {
		this.attemptStatus = attemptStatus;
	}
	public Long getSkip() {
		return skip;
	}
	public void setSkip(Long skip) {
		this.skip = skip;
	}
	public Long getTotalInCorrectCount() {
		return totalInCorrectCount;
	}
	public void setTotalInCorrectCount(Long totalInCorrectCount) {
		this.totalInCorrectCount = totalInCorrectCount;
	}
	public Long getTotalCorrectCount() {
		return totalCorrectCount;
	}
	public void setTotalCorrectCount(Long totalCorrectCount) {
		this.totalCorrectCount = totalCorrectCount;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getStatus() {
		return status;
	}

}
