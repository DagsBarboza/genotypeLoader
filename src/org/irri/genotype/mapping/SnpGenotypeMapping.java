package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderSnpGenotype;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class SnpGenotypeMapping extends AbstractMapping<LoaderSnpGenotype> {

	public SnpGenotypeMapping(String schemaName, String tableName) {
		super(schemaName, tableName);

		mapInteger("snp_feature_id", LoaderSnpGenotype::getSnpFeatureId);
		mapInteger("stock_sample_id", LoaderSnpGenotype::getStockSampleId);
		mapInteger("genotype_run_id", LoaderSnpGenotype::getGenotypeRunId);
		mapString("allele1", LoaderSnpGenotype::getAllele1);
		mapString("allele2", LoaderSnpGenotype::getAllele2);

	}

}
