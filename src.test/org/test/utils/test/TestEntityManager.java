package org.test.utils.test;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Test;

import chado.loader.AppContext;
import chado.loader.model.Db;
import chado.loader.model.Dbxref;
import chado.loader.service.DbService;
import chado.loader.service.DbXrefService;

public class TestEntityManager {

	
	public void testGetEntityManager() {

		AppContext.createEntityManager();

		EntityManager em = AppContext.getEntityManager();

		DbService ds1 = new DbService();
		System.out.println("EM: " + em.getProperties());
		System.out.println(ds1.getAllDb().size());

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://test:testport/iricsss");
		properties.put("javax.persistence.jdbc.user", "iric");
		properties.put("javax.persistence.jdbc.password", "iric-dev");

		try {
			AppContext.createEntityManager(properties);
		} catch (Exception e) {
			System.out.println("Loader Exception");
		}

		EntityManager em2 = AppContext.getEntityManager();

		DbService ds2 = new DbService();
		System.out.println("EM2: " + em2.getProperties());
		System.out.println(ds2.getAllDb().size());

		AppContext.createEntityManager();

		EntityManager em3 = AppContext.getEntityManager();

		DbService ds3 = new DbService();
		System.out.println("EM3: " + em3.getProperties());
		System.out.println(ds3.getAllDb().size());

	}

	@Test
	public void testStock_Sample() {

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://172.29.4.215:5432/iita_new");
		properties.put("javax.persistence.jdbc.user", "iric");
		properties.put("javax.persistence.jdbc.password", "iric-dev");
		
		AppContext.createEntityManager(properties);
		
		DbXrefService xrefDs = new DbXrefService();
		DbService dbDs = new DbService();
		
		
		Db db = new Db();
		db.setName("test Db");
		db.setDescription("test desc");
		db = (Db) dbDs.insertRecord(db);
		
		
		
		Dbxref dbXref = new Dbxref();
		dbXref.setAccession("Test Accession");
		dbXref.setDb(db);
		dbXref.setVersion("1");
		
		xrefDs.insertRecord(dbXref);
		
		
		
		
		
		
		
	}

}
