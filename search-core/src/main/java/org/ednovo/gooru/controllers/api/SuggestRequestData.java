package org.ednovo.gooru.controllers.api;

public class SuggestRequestData {
	
	private String userUid;
	
	private String contentGooruOid;
	
	private String event;
	
	private String keyword;

	public void setContentGooruOid(String contentGooruOid) {
		this.contentGooruOid = contentGooruOid;
	}

	public String getContentGooruOid() {
		return contentGooruOid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUserUid() {
		return userUid;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
