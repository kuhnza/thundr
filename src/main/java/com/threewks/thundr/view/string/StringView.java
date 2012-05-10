package com.threewks.thundr.view.string;

public class StringView {
	private CharSequence content;

	public StringView(String content) {
		this.content = content;
	}

	public StringView(String format, Object... args) {
		this.content = String.format(format, args);
	}

	public CharSequence content() {
		return content;
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
