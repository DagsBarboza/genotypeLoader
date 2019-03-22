package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderFeature;
import org.irri.genotype.loader.object.LoaderSnpFeature;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class FeatureMapping extends AbstractMapping<LoaderFeature> {

	public FeatureMapping(String schemaName, String tableName) {
		super(schemaName, tableName);
		
		
		
		mapInteger("organism_id", LoaderFeature::getOrganismId);
		mapText("uniquename", LoaderFeature::getUniqueName);
		mapText("name", LoaderFeature::getName);
		mapInteger("type_id", LoaderFeature::getTypeId);
		mapBoolean("is_analysis", LoaderFeature::isAnalysis);
		mapBoolean("is_obsolete", LoaderFeature::isObsolete);
		//mapDate("timeaccessioned", LoaderFeature::getTimeAccession);
		//mapDate("timelastmodified", LoaderFeature::getTimeModified);
		
		
	}

}
