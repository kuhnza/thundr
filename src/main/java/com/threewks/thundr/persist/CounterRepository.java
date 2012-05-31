package com.threewks.thundr.persist;

/**
 * The app-engine datastore is not good at incremental counters. To surmount this, we need a customer counter implementation.
 * @see DatastoreCounterRepository
 */
public interface CounterRepository {

	public long getCount(String counterType);

	public long incrementCount(String counterType);

	public long decrementCount(String counterType);

	public long getCount(String counterType, String association);

	public long incrementCount(String counterType, String association);

	long incrementCountBy(String counterType, String association, long by);

	public long decrementCount(String counterType, String association);

	public long decrementCountBy(String counterType, String association, long by);
	
	public long setCount(String counterType, String association, long value);
}
