package org.test.utils.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.irri.genotype.loader.object.LoaderSnpFeature;
import org.irri.genotype.loader.object.LoaderSnpFeatureLoc;
import org.irri.genotype.mapping.SnpFeatureLocMapping;
import org.irri.genotype.mapping.SnpfeatureMapping;
import org.junit.Test;

import chado.loader.AppContext;
import chado.loader.service.SnpFeatureService;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;

public class TestUtil {
	
	
	public void testGetSequence() {
		AppContext.createEntityManager();
		SnpFeatureService ds = new SnpFeatureService();
		
		System.out.println(ds.getSnpFeatureCurrentSeqNumber());
	}
	
	@Test
	public void testSnpFeatureBulkInsert() {
		AppContext.createEntityManager();
		SnpFeatureService ds = new SnpFeatureService();
		Connection conn = null;
		
		
		
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://172.29.4.215:5432/iric_copy", "iric", "iric-dev");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		List<LoaderSnpFeature> snpFeatureList = new ArrayList<>();
		
		LoaderSnpFeature loaderSnpFeature = new LoaderSnpFeature();
		for(int i = 0 ; i< 10 ; i++) {
			loaderSnpFeature.setSnpFeatureId(i);
			loaderSnpFeature.setVariantSetId(21);

		}

		
		snpFeatureList.add(loaderSnpFeature);
		
		PgBulkInsert<LoaderSnpFeature> bulkInsert = new PgBulkInsert<LoaderSnpFeature>(
				new SnpfeatureMapping("public", "snp_feature"));

	

		try {
			bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(conn), snpFeatureList.stream());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
