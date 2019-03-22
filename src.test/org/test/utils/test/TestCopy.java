package org.test.utils.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.irri.genotype.loader.object.LoaderSnpFeature;
import org.irri.genotype.mapping.SnpfeatureMapping;
import org.junit.Test;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import chado.loader.AppContext;
import chado.loader.model.SnpFeature;
import chado.loader.model.Variantset;
import chado.loader.service.VariantSetService;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;

public class TestCopy {

	public void testCopy() {
		long startTime = System.nanoTime();

		File f = new File("D:\\Sorghum.positions");
		BufferedReader sampleFileReader = getBufferedReader(f);
		String line;
		try {
			while ((line = sampleFileReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long difference = System.nanoTime() - startTime;
		System.out.println("Total execution time: " + String.format("%d min, %d sec",
				TimeUnit.NANOSECONDS.toHours(difference), TimeUnit.NANOSECONDS.toSeconds(difference)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));
		System.out.println("DONE... ");

	}

	
	public void testCopyProc() {

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://172.29.4.215:5432/iita_new", "iric", "iric-dev");

			System.err.println("Copying text data rows from stdin");

			CopyManager copyManager = new CopyManager((BaseConnection) conn);

			FileReader fileReader = new FileReader("D:\\sorghum_rawfiles\\Sorghum.samples");
//			copyManager.copyIn("COPY position FROM STDIN", fileReader);
			copyManager.copyIn("copy sample from  'D:\\sorghum_rawfiles\\Sorghum.samples'");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("DONE... ");

	}
	
	
	public void testInsertStockSample() {

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://172.29.4.215:5432/iita_new", "iric", "iric-dev");

			System.err.println("Copying text data rows from stdin");

			CopyManager copyManager = new CopyManager((BaseConnection) conn);

			FileReader fileReader = new FileReader("D:\\sorghum_rawfiles\\Sorghum.samples");
//			copyManager.copyIn("COPY position FROM STDIN", fileReader);
			copyManager.copyIn("copy sample from  'D:\\sorghum_rawfiles\\Sorghum.samples'");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("DONE... ");

	}

	
	public void testGenerateSeries() {
		long startTime = System.nanoTime();

		List<LoaderSnpFeature> snpFeatureList = new ArrayList<>();

		AppContext.createEntityManager();
		VariantSetService ds = new VariantSetService();
		Variantset vset = ds.getVariantSetById(1).get(0);

		File f = new File("D:\\Sorghum.positions");
		// BufferedReader sampleFileReader = getBufferedReader(f);
		int lines = getlineNumber(f);
		// String line;
		LoaderSnpFeature e;
		for (int i = 1; i <= lines; i++) {
			e = new LoaderSnpFeature();
			e.setSnpFeatureId(i);
			e.setVariantSetId(vset.getVariantsetId());

			snpFeatureList.add(e);

			e = null;
		}

		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://172.29.4.215:5432/iric", "iric",
					"iric-dev");
			// // Create the BulkInserter:
			PgBulkInsert<LoaderSnpFeature> bulkInsert = new PgBulkInsert<LoaderSnpFeature>(
					new SnpfeatureMapping("public", "snp_feature"));

			bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(conn), snpFeatureList.stream());
			// // Now save all entities of a given stream:
			// bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection),
			// persons.stream());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long difference = System.nanoTime() - startTime;
		System.out.println("Total execution time: " + String.format("%d min, %d sec",
				TimeUnit.NANOSECONDS.toHours(difference), TimeUnit.NANOSECONDS.toSeconds(difference)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));

		System.out.println("SIZE: " + snpFeatureList.size());
	}

	private int getlineNumber(File file) {
		int numberOflines = 0;
		if (file != null) {
			try {
				LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
				lineNumberReader.skip(Long.MAX_VALUE);

				numberOflines = lineNumberReader.getLineNumber();

				lineNumberReader.close();

				return numberOflines;

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return numberOflines;
	}

	private BufferedReader getBufferedReader(File file) {

		if (file != null)
			try {
				return new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return null;
	}

}
