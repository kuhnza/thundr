package com.atomicleopard.webFramework.view.string;

public class StringView {
	private CharSequence content;

	public StringView(String content) {
		this.content = content;
	}

	public CharSequence content() {
		return content;
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
