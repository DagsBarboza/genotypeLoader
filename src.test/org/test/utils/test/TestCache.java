package org.test.utils.test;

import java.util.concurrent.ConcurrentNavigableMap;

import javax.persistence.EntityManager;

import org.irri.genotype.Cache;
import org.junit.Test;

import chado.loader.AppContext;

public class TestCache {

	
	public void testCache() {
		Cache cache = new Cache();
		
		ConcurrentNavigableMap<String, String> cacheList = cache.getDB().getTreeMap("cacheMap");
		
		System.out.println(cacheList.size());
		
		
		cacheList.clear();

		cache.commit();
		 
		System.out.println(cacheList.size());
		
		
	}
	
	@Test
	public void testConnection() {
		AppContext.createEntityManager();
		
		EntityManager em = AppContext.getEntityManager();
		
		
		
	}

}
