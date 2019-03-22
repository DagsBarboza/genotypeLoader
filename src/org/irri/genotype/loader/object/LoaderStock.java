package org.irri.genotype.loader.object;

public class LoaderStock {

	private Integer cvtermId;
	private Integer organismId;
	private boolean isObsolete;
	private String name;
	private String uniquename;
	
	public Integer getCvtermId() {
		return cvtermId;
	}
	public void setCvtermId(Integer cvtermId) {
		this.cvtermId = cvtermId;
	}
	public Integer getOrganismId() {
		return organismId;
	}
	public void setOrganismId(Integer organismId) {
		this.organismId = organismId;
	}
	public boolean isObsolete() {
		return isObsolete;
	}
	public void setObsolete(boolean isObsolete) {
		this.isObsolete = isObsolete;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUniquename() {
		return uniquename;
	}
	public void setUniquename(String uniquename) {
		this.uniquename = uniquename;
	}

	
}
