package edu.ruhhosp.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MockDataStore
{
	private static MockDataStore singletonMockDataStore = new MockDataStore();
	public static MockDataStore getInstance()
	{
		return singletonMockDataStore;
	}

	protected MockDataStore() {}
	
	AtomicInteger id = new AtomicInteger(1);
	Map<Integer, Object> dataStore = new ConcurrentHashMap<Integer, Object>(); 
	
	public int createObject(Object value)
	{
		int newID = id.incrementAndGet();
		dataStore.put(newID, value);
		return newID;
	}
	
	public Object readObject(int id)
	{
		return dataStore.get(id);
	}
	
	public Object updateObject(int id, Object newValue)
	{
		Object oldValue = dataStore.get(id);
		if (oldValue == null)
			throw new IllegalArgumentException("Unable to find (to update) object with id " + id);
		dataStore.put(id, newValue);
		return oldValue;
	}

	public Object deleteObject(int id)
	{
		Object oldValue = dataStore.get(id);
		if (oldValue == null)
			throw new IllegalArgumentException("Unable to find (to delete) object with id " + id);
		dataStore.remove(id);
		return oldValue;
	}
}
