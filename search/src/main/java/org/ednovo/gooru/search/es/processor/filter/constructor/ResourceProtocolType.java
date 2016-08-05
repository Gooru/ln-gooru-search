package org.ednovo.gooru.search.es.processor.filter.constructor;

public enum ResourceProtocolType {

	HTTP(1),
	HTTPS(2),
	BOTH(3);
	
	private Integer protocolType;
	
	public Integer getProtocolType() {
		return protocolType;
	}

	private ResourceProtocolType(Integer protocolType) {
		this.protocolType = protocolType;
	}
	
}
