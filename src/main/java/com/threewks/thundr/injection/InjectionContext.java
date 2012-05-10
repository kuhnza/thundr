package com.threewks.thundr.injection;

public interface InjectionContext {
	/**
	 * Get an named instance of the specified type from this {@link InjectionContext}.
	 * A type exists if either an instance or class type was registered previously into this context using the specified name,
	 * or if an instance or class type was registered without a specific name.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws NullPointerException
	 */
	public <T> T get(Class<T> type, String name) throws NullPointerException;

	/**
	 * Get an instance of the specified type from this {@link InjectionContext}.
	 * A type exists if either an instance or class type was registered previously into this context.
	 * 
	 * @param type
	 * @return
	 * @throws NullPointerException
	 */
	public <T> T get(Class<T> type) throws NullPointerException;

	/**
	 * Returns true if this {@link InjectionContext} can return an object instance from {@link #get(Class)} based on the given type.
	 * 
	 * @param type
	 * @return
	 */
	public <T> boolean contains(Class<T> type);

	/**
	 * Returns true if this {@link InjectionContext} can return an object instance from {@link #get(Class, String)} based on the given type and name.
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public <T> boolean contains(Class<T> type, String name);
}
