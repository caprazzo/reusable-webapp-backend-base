package net.caprazzi.skimpy.dao;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public abstract class Db4oDao {

	protected final ObjectContainer db;

	public Db4oDao(ObjectContainer db) {
		this.db = db;
	}
	
	public void queryForZero(Predicate<?> predicate) {
		ObjectSet result = db.query(predicate);
		if (result.size() > 0)
			throw new RuntimeException("Expected 0 objects, got " + result.size() +". Database corrupted or wrong operation?");
	}
	
	public <T> T queryForOne(Predicate<T> predicate) {
		ObjectSet<T> result = db.query(predicate);
		if (result.size() == 0)
			return null;
		
		if (result.size() == 1)
			return result.get(0);
		
		throw new RuntimeException("Expected at most 1 object, got " + result.size() +". Database corrupted?");		
	}

}
