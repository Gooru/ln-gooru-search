package org.ednovo.gooru.search.es.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;

public class RequestSupport {

	public RequestSupport() {
		log = new HashMap<String, Object>();
		moveContentMeta = new HashMap<String, Object>();
	}

	public static RequestSupport getSessionContext() {
		return (RequestSupport) ContextLoader.getCurrentWebApplicationContext().getBean("requestSupport");
	}

	private Map<String, Object> log;

	private Map<String, Object> moveContentMeta;

	public Map<String, Object> getLog() {
		return log;
	}

	public void setLog(Map<String, Object> log) {
		this.log = log;
	}

	public Map<String, Object> getMoveContentMeta() {
		return moveContentMeta;
	}

	public void setMoveContentMeta(Map<String, Object> moveContentMeta) {
		this.moveContentMeta = moveContentMeta;
	}
}
