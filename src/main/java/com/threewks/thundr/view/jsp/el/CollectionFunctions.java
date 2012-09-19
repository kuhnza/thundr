package com.threewks.thundr.view.jsp.el;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.atomicleopard.expressive.Cast;

public class CollectionFunctions {

	/**
	 * 
	 * @param collectionOrArray a {@link Collection} or array of object
	 * @param objectCollectionOrArray a single object, and array of objects or a {@link Collection} of objects
	 * @return true if the first argument contains all of the elements in the second argument. If the second argument is an empty collection, returns true. if the second element is null, returns true if a null is contained in the first argument.
	 */
	@SuppressWarnings("unchecked")
	public static boolean contains(Object collectionOrArray, Object objectCollectionOrArray) {
		Collection<Object> collection = toCollection(collectionOrArray);
		Collection<Object> asCollection = Cast.as(objectCollectionOrArray, Collection.class);
		if (asCollection == null) {
			asCollection = isArray(objectCollectionOrArray) ? Arrays.asList((Object[]) objectCollectionOrArray) : Collections.singleton(objectCollectionOrArray);
		}

		return collection.containsAll(asCollection);
	}

	/**
	 * @param collectionOrArray a {@link Collection} or array of object
	 * @param objectCollectionOrArray a single object, and array of objects or a {@link Collection} of objects
	 * @return true if the first argument contains any of the elements in the second argument. If the second argument is an empty collection, returns true. if the second element is null, returns true if a null is contained in the first argument.
	 */
	@SuppressWarnings("unchecked")
	public static boolean containsAny(Object collectionOrArray, Object objectCollectionOrArray) {
		Collection<Object> collection = toCollection(collectionOrArray);
		Collection<Object> asCollection = Cast.as(objectCollectionOrArray, Collection.class);
		if (asCollection == null) {
			asCollection = isArray(objectCollectionOrArray) ? Arrays.asList((Object[]) objectCollectionOrArray) : Collections.singleton(objectCollectionOrArray);
		}
		for (Object o : asCollection) {
			if (collection.contains(o)) {
				return true;
			}
		}
		return asCollection.isEmpty();
	}

	private static List<Object> toCollection(Object collectionOrArray) {
		boolean isArray = isArray(collectionOrArray);
		return isArray ? Arrays.asList((Object[]) collectionOrArray) : new ArrayList<Object>((Collection<?>) collectionOrArray);
	}

	private static boolean isArray(Object object) {
		return object == null ? false : object.getClass().isArray();
	}
}
