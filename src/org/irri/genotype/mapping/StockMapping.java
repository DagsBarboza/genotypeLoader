package org.irri.genotype.mapping;

import org.irri.genotype.loader.object.LoaderFeature;
import org.irri.genotype.loader.object.LoaderStock;

import chado.loader.model.Stock;
import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class StockMapping extends AbstractMapping<LoaderStock> {

	public StockMapping(String schemaName, String tableName) {
		super(schemaName, tableName);
		
		
		
		mapInteger("cvtermId", LoaderStock::getCvtermId);
		mapText("uniquename", LoaderStock::getUniquename);
		mapText("name", LoaderStock::getName);
		mapInteger("organismId", LoaderStock::getOrganismId);
		mapBoolean("isObsolete", LoaderStock::isObsolete);
		
		
		
	}

}
