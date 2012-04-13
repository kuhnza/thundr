package com.atomicleopard.webFramework.http;

public class FileParameter {
	private String name;
	private byte[] data;

	public FileParameter(String name, byte[] data) {
		super();
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public byte[] getData() {
		return data;
	}
}
