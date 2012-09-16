package com.threewks.thundr.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Triplets<K1, K2, V> {
	private Map<Pair<K1, K2>, V> delegate = new HashMap<Pair<K1, K2>, V>();

	public void put(K1 k1, K2 k2, V v) {
		delegate.put(pair(k1, k2), v);
	}

	public void putAll(Triplets<K1, K2, V> triplets) {
		delegate.putAll(triplets.delegate);
	}

	public void remove(K1 k1, K2 k2) {
		delegate.remove(pair(k1, k2));
	}

	public V get(K1 k1, K2 k2) {
		return delegate.get(pair(k1, k2));
	}

	public void clear() {
		delegate.clear();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public int size() {
		return delegate.size();
	}

	public boolean containsKey(K1 k1, K2 k2) {
		return delegate.containsKey(pair(k1, k2));
	}

	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	public Collection<V> values() {
		return delegate.values();
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	public Set<Pair<K1, K2>> keySet() {
		return delegate.keySet();
	}

	public Set<Entry<Pair<K1, K2>, V>> entrySet() {
		return delegate.entrySet();
	}

	private Pair<K1, K2> pair(K1 k1, K2 k2) {
		return new Pair<K1, K2>(k1, k2);
	}
}
