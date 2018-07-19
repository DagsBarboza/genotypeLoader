package org.irri.genotype.mapping;



import org.irri.genotype.loader.object.LoaderVariantVariantSet;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class VariantVariantSetMapping extends AbstractMapping<LoaderVariantVariantSet> {

	public VariantVariantSetMapping(String schemaName, String tableName) {
		super(schemaName, tableName);

		mapInteger("variant_variantset_id", LoaderVariantVariantSet::getVariantVariantsetId);
		mapInteger("variantset_id", LoaderVariantVariantSet::getVariantset);
		mapInteger("variant_feature_id", LoaderVariantVariantSet::getVariantFeatureId);
		mapInteger("variant_type_id", LoaderVariantVariantSet::getCvtermId);
		mapInteger("hdf5_index", LoaderVariantVariantSet::getHdf5Index);

	}

}
