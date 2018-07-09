package org.irri.genotype.loader.object;

public class LoaderSnpGenotype {

	private Integer snpFeatureId;
	private Integer stockSampleId;
	private Integer genotypeRunId;
	private String allele1;
	private String allele2;

	public Integer getSnpFeatureId() {
		return snpFeatureId;
	}

	public void setSnpFeatureId(Integer snpFeatureId) {
		this.snpFeatureId = snpFeatureId;
	}

	public Integer getStockSampleId() {
		return stockSampleId;
	}

	public void setStockSampleId(Integer stockSampleId) {
		this.stockSampleId = stockSampleId;
	}

	public Integer getGenotypeRunId() {
		return genotypeRunId;
	}

	public void setGenotypeRunId(Integer genotypeRunId) {
		this.genotypeRunId = genotypeRunId;
	}

	public String getAllele1() {
		return allele1;
	}

	public void setAllele1(String allele1) {
		this.allele1 = allele1;
	}

	public String getAllele2() {
		return allele2;
	}

	public void setAllele2(String allele2) {
		this.allele2 = allele2;
	}

}
