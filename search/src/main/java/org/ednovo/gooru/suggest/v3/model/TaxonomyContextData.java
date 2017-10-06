package org.ednovo.gooru.suggest.v3.model;

import java.util.List;

public class TaxonomyContextData {
	
	private List<String> slInternalCodes;

	private List<String> gutStdCodes;
	
	private List<String> gutLtCodes;
	
	private List<String> gutLtParentCodes;
	
	public List<String> getSlInternalCodes() {
		return slInternalCodes;
	}

	public void setSlInternalCodes(List<String> slInternalCodes) {
		this.slInternalCodes = slInternalCodes;
	}

	public List<String> getGutStdCodes() {
		return gutStdCodes;
	}

	public void setGutStdCodes(List<String> gutStdCodes) {
		this.gutStdCodes = gutStdCodes;
	}

	public List<String> getGutLtCodes() {
		return gutLtCodes;
	}

	public void setGutLtCodes(List<String> gutLtCodes) {
		this.gutLtCodes = gutLtCodes;
	}

	public List<String> getGutLtParentCodes() {
		return gutLtParentCodes;
	}

	public void setGutLtParentCodes(List<String> gutLtParentCodes) {
		this.gutLtParentCodes = gutLtParentCodes;
	}

}
