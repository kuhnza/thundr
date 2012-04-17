package com.atomicleopard.webFramework.view.string;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringViewTest {
	@Test
	public void shouldRetainSpecifiedContent() {
		assertThat(new StringView("Content").content().toString(), is("Content"));
	}

	@Test
	public void shouldHaveToStringShowingContents() {
		assertThat(new StringView("Content\r\r\n\tIs here ").toString(), is("Content\r\r\n\tIs here "));
	}
}
