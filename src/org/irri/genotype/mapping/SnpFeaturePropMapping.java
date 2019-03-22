package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderSnpFeatureProp;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class SnpFeaturePropMapping extends AbstractMapping<LoaderSnpFeatureProp> {

	public SnpFeaturePropMapping(String schemaName, String tableName) {
		super(schemaName, tableName);

		mapInteger("snp_feature_id", LoaderSnpFeatureProp::getSnpFeatureId);
		mapInteger("type_id", LoaderSnpFeatureProp::getTypeId);
		mapText("value", LoaderSnpFeatureProp::getValue);
		

	}

}
