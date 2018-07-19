package org.test.utils.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.ole.win32.Variant;
import org.irri.genotype.LoaderProperties;
import org.irri.genotype.loader.object.LoaderSnpFeatureLoc;
import org.irri.genotype.loader.object.LoaderVariantVariantSet;
import org.irri.genotype.mapping.SnpFeatureLocMapping;
import org.irri.genotype.mapping.VariantVariantSetMapping;
import org.junit.Test;

import chado.loader.AppContext;
import chado.loader.model.SnpFeature;
import chado.loader.model.VariantVariantset;
import chado.loader.model.Variantset;
import chado.loader.service.SnpFeatureService;
import chado.loader.service.VariantSetService;
import chado.loader.service.VariantVariantSetService;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;

public class TestVariantVariant_set {
	
	@Test
	public void testVariant_variant_set() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://172.29.4.215:5432/iric_copy");
		properties.put("javax.persistence.jdbc.user", "iric");
		properties.put("javax.persistence.jdbc.password", "iric-dev");
		
		
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://172.29.4.215:5432/iric_copy", "iric",
					"iric-dev");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		AppContext.createEntityManager(properties);
		
		VariantSetService v_ds = new VariantSetService();
		
		SnpFeatureService ds = new SnpFeatureService();
		
		VariantVariantSetService vvs_ds = new VariantVariantSetService();

		Variantset variantSet = v_ds.getVariantSetById(19).get(0);
		
		
		System.out.println("NAME "+variantSet.getName());
		
		LoaderVariantVariantSet vvs;
		Integer id = vvs_ds.getVariantVariantSetCurrentSeqNumber() + 1;
		
		List<LoaderVariantVariantSet> v_list = new ArrayList<>();
		for (SnpFeature sf: ds.getSnpFeatureByVariantSetId(variantSet)) {
			vvs = new LoaderVariantVariantSet();
			vvs.setVariantVariantsetId(id++);
			vvs.setVariantFeatureId(sf.getSnpFeatureId());
			vvs.setVariantset(variantSet.getVariantsetId());
			vvs.setHdf5Index(sf.getSnpFeatureId() - 1);
			
			System.out.println("INSERT: "+ vvs.getVariantFeatureId());
			System.out.println(vvs.getVariantset());
			System.out.println(vvs.getHdf5Index());
			
			v_list.add(vvs);
			vvs = null;
			
			
		}
		
		
		PgBulkInsert<LoaderVariantVariantSet> bulkInsertVariantVariantSet= new PgBulkInsert<LoaderVariantVariantSet>(
				new VariantVariantSetMapping("public", "variant_variantset"));

		try {
			bulkInsertVariantVariantSet.saveAll(PostgreSqlUtils.getPGConnection(conn), v_list.stream());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("END>>");
		
		
		
		
	}

}
