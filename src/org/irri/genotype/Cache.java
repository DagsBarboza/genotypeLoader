package org.irri.genotype;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Cache {

	private DB db;
	private File dbFileCache;
	private ConcurrentNavigableMap<String, LoaderProperties> cacheList;
	private boolean isDirty;

	public Cache() {
		dbFileCache = new File(this.getClass().getResource("/").getPath() + CacheConstants.DB);
		open();
		cacheList = getDB().getTreeMap("cacheMap");
		isDirty = false;
	}

	public void open() {
		db = DBMaker.newFileDB(dbFileCache).closeOnJvmShutdown().asyncWriteDisable().make();
	}

	public void commit() {
		if (isDirty)
			db.commit();
	}

	public void close() {
		db.close();
	}

	public DB getDB() {
		return db;
	}

	public ConcurrentNavigableMap<String, LoaderProperties> getCacheList() {
		return cacheList;
	}

	public ConcurrentNavigableMap<String, LoaderProperties> putProperty(String name, LoaderProperties prop) {
		cacheList.put(name, prop);
		isDirty = true;
		return cacheList;
	}

}
