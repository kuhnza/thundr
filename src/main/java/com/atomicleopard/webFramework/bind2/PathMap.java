package com.atomicleopard.webFramework.bind2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Given a request map, alters the key to represent the path as components.
 * For example object[0].name should be a key of object, [0], name
 * 
 * This allows us to pop keys as we walk a path.
 * For the above example, when dealing with object, the path should then be [0], name
 */
public class PathMap {

	private Map<List<String>, String[]> delegate = new LinkedHashMap<List<String>, String[]>();

	private PathMap() {

	}

	public PathMap(Map<String, String[]> input) {
		this();
		for (Map.Entry<String, String[]> entry : input.entrySet()) {
			String key = entry.getKey();
			String expandedKey = key.replaceAll("\\.", "\r");
			expandedKey = expandedKey.replaceAll("\\[", "\r[");
			List<String> path = Collections.unmodifiableList(Arrays.asList(expandedKey.split("\r")));
			delegate.put(path, entry.getValue());
		}
	}

	public PathMap pathMapFor(String key) {
		PathMap pathMap = new PathMap();
		for (Map.Entry<List<String>, String[]> entry : delegate.entrySet()) {
			List<String> path = entry.getKey();
			if (path.size() > 1 && path.get(0).equals(key)) {
				List<String> newPath = path.subList(1, path.size());
				pathMap.delegate.put(newPath, entry.getValue());
			}
		}
		return pathMap;
	}

	public Set<String> uniqueChildren() {
		Set<String> results = new HashSet<String>();
		for (Map.Entry<List<String>, String[]> entry : delegate.entrySet()) {
			List<String> path = entry.getKey();
			String child = path.get(0);
			results.add(child);
		}
		return results;
	}

	public String[] get(List<String> arg0) {
		return delegate.get(arg0);
	}

	public String[] get(String arg0) {
		return delegate.get(Collections.singletonList(arg0));
	}

	public int size() {
		return delegate.size();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	public Map<String, String[]> toStringMap(String pathElement) {
		Map<String, String[]> stringMap = new HashMap<String, String[]>();
		for (Entry<List<String>, String[]> entry : delegate.entrySet()) {
			List<String> key = entry.getKey();

			if (pathElement.equals(key.get(0))) {
				StringBuilder joinedKey = new StringBuilder();
				boolean first = true;
				for (int i = 1; i < key.size(); i++) {
					String string = key.get(i);
					if (!first && !string.startsWith("[")) {
						joinedKey.append(".");
					}
					joinedKey.append(string);
					first = false;
				}
				String stringKey = joinedKey.toString();
				if (stringKey.length() > 0) {
					stringMap.put(stringKey, entry.getValue());
				}
			}
		}
		return stringMap;
	}
}
