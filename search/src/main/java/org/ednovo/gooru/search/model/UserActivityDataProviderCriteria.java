package org.ednovo.gooru.search.model;


public class UserActivityDataProviderCriteria extends ContextDataProviderCriteria {

	private String eventName;
	
	private Integer totalEventsToRead;
	
	private Integer minutesToRead;
	
	private Integer eventsToRead;
	
	public Integer getEventsToRead() {
		return eventsToRead;
	}

	public void setEventsToRead(Integer eventsToRead) {
		this.eventsToRead = eventsToRead;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventName() {
		return eventName;
	}

	public Integer getTotalEventsToRead() {
		return totalEventsToRead;
	}

	public void setTotalEventsToRead(Integer totalEventsToRead) {
		this.totalEventsToRead = totalEventsToRead;
	}

	public Integer getMinutesToRead() {
		return minutesToRead;
	}

	public void setMinutesToRead(Integer minutesToRead) {
		this.minutesToRead = minutesToRead;
	}
}
