package org.irri.genotype.loader.object;

import java.sql.Date;
import java.time.LocalDate;

public class LoaderFeature {

	private Integer organismId;
	private String uniqueName;
	private String name;
	private Integer typeId;
	private boolean isAnalysis;
	private boolean isObsolete;
	private LocalDate timeAccession;
	private LocalDate timeModified;

	public Integer getOrganismId() {
		return organismId;
	}

	public void setOrganismId(Integer organismId) {
		this.organismId = organismId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public boolean isAnalysis() {
		return isAnalysis;
	}

	public void setAnalysis(boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public boolean isObsolete() {
		return isObsolete;
	}

	public void setObsolete(boolean isObsolete) {
		this.isObsolete = isObsolete;
	}

	public LocalDate getTimeAccession() {
		return timeAccession;
	}

	public void setTimeAccession(LocalDate timeAccession) {
		this.timeAccession = timeAccession;
	}

	public LocalDate getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(LocalDate timeModified) {
		this.timeModified = timeModified;
	}

}
