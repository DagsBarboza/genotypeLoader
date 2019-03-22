package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderSnpFeatureLoc;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class SnpFeatureLocMapping extends AbstractMapping<LoaderSnpFeatureLoc> {

	public SnpFeatureLocMapping(String schemaName, String tableName) {
		super(schemaName, tableName);

		mapInteger("snp_feature_id", LoaderSnpFeatureLoc::getSnpfeatureId);
		mapInteger("organism_id", LoaderSnpFeatureLoc::getOrganismId);
		mapInteger("srcfeature_id", LoaderSnpFeatureLoc::getSrcFeatureid);
		mapInteger("position", LoaderSnpFeatureLoc::getPosition);
		mapText("refcall", LoaderSnpFeatureLoc::getRefCall);

		// mapDate("timeaccessioned", LoaderFeature::getTimeAccession);
		// mapDate("timelastmodified", LoaderFeature::getTimeModified);

	}

}
