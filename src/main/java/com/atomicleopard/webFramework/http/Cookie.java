package com.atomicleopard.webFramework.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Cookie {
	private final String domain;
	private final String name;
	private final String value;
	private final String path;
	private final int maxAge;
	private final boolean secure;
	private final int version;
	private Set<Integer> ports = new TreeSet<Integer>();

	public Cookie(String domain, String name, String value, String path, int maxAge, boolean secure) {
		this(domain, name, value, path, maxAge, secure, 1);
	}

	public Cookie(String domain, String name, String value, String path, int maxAge, boolean secure, int version) {
		this.domain = domain;
		this.name = name;
		this.value = value;
		this.path = path;
		this.maxAge = maxAge;
		this.secure = secure;
		this.version = version;
	}

	
	public String getDomain() {
		return domain;
	}
	
	public String getName() {
		return name == null ? "" : name;
	}

	public String getValue() {
		return value == null ? "" : value;
	}

	public String getPath() {
		return path;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public boolean isSecure() {
		return secure;
	}

	public int getVersion() {
		return version;
	}

	public Set<Integer> getPorts() {
		return Collections.unmodifiableSet(ports);
	}

	public void setPorts(Integer... ports) {
		setPorts(Arrays.asList(ports));
	}

	public void setPorts(Iterable<Integer> ports) {
		this.ports.clear();
		for (Integer port : ports) {
			if (port == null || port <= 0 || port > 65535) {
				throw new IllegalArgumentException(String.format("Invalid port: %s" + port));
			}
			this.ports.add(port);
		}
	}

	@Override
	public String toString() {
		return String.format("Cookie: domain=%s, name=%s, value=%s, path=%s, maxAge=%d, secure=%s", domain, name, value, path, maxAge, secure);
	}
}
