package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderSnpFeature;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class SnpfeatureMapping extends AbstractMapping<LoaderSnpFeature> {

	public SnpfeatureMapping(String schemaName, String tableName) {
		super(schemaName, tableName);
		
		
		mapInteger("snp_feature_id", LoaderSnpFeature::getSnpFeatureId);
		mapInteger("variantset_id", LoaderSnpFeature::getVariantSetId);
		
		
	}

}
