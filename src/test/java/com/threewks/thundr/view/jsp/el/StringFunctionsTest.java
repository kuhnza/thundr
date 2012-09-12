package com.threewks.thundr.view.jsp.el;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringFunctionsTest {

	@Test
	public void shouldUppercase() {
		assertThat(StringFunctions.upperCase(null), is(nullValue()));
		assertThat(StringFunctions.upperCase(""), is(""));
		assertThat(StringFunctions.upperCase(" "), is(" "));
		assertThat(StringFunctions.upperCase("something"), is("SOMETHING"));
		assertThat(StringFunctions.upperCase("SomeThing"), is("SOMETHING"));
		assertThat(StringFunctions.upperCase(new TestObject()), is("TEST OBJECT STRING"));
	}

	@Test
	public void shouldLowercase() {
		assertThat(StringFunctions.lowerCase(null), is(nullValue()));
		assertThat(StringFunctions.lowerCase(""), is(""));
		assertThat(StringFunctions.lowerCase(" "), is(" "));
		assertThat(StringFunctions.lowerCase("something"), is("something"));
		assertThat(StringFunctions.lowerCase("SomeThing"), is("something"));
		assertThat(StringFunctions.lowerCase(new TestObject()), is("test object string"));
	}

	@Test
	public void shouldSentencecase() {
		assertThat(StringFunctions.sentenceCase(null), is(nullValue()));
		assertThat(StringFunctions.sentenceCase(""), is(""));
		assertThat(StringFunctions.sentenceCase(" "), is(" "));
		assertThat(StringFunctions.sentenceCase("something"), is("Something"));
		assertThat(StringFunctions.sentenceCase("SomeThing"), is("SomeThing"));
		assertThat(StringFunctions.sentenceCase("Some Thing"), is("Some Thing"));
		assertThat(StringFunctions.sentenceCase("some thing"), is("Some thing"));
		assertThat(StringFunctions.sentenceCase("SoME thIng"), is("SoME thIng"));
		assertThat(StringFunctions.sentenceCase(new TestObject()), is("Test object string"));
	}

	@Test
	public void shouldCaptialise() {
		assertThat(StringFunctions.capitalise(null), is(nullValue()));
		assertThat(StringFunctions.capitalise(""), is(""));
		assertThat(StringFunctions.capitalise(" "), is(" "));
		assertThat(StringFunctions.capitalise("something"), is("Something"));
		assertThat(StringFunctions.capitalise("SomeThing"), is("Something"));
		assertThat(StringFunctions.capitalise("Some Thing"), is("Some Thing"));
		assertThat(StringFunctions.capitalise("some thing"), is("Some Thing"));
		assertThat(StringFunctions.capitalise("SOME thing"), is("Some Thing"));
		assertThat(StringFunctions.capitalise(new TestObject()), is("Test Object String"));
	}

	@Test
	public void shouldReplaceUsingRegex() {
		assertThat(StringFunctions.replace(null, null, null), is(""));
		assertThat(StringFunctions.replace(null, "", ""), is(""));
		assertThat(StringFunctions.replace(null, "(.*)", "$1"), is(""));
		assertThat(StringFunctions.replace("input", "in", "out"), is("output"));
		assertThat(StringFunctions.replace("input", "in", ""), is("put"));
		assertThat(StringFunctions.replace("input", "in", null), is("put"));
		assertThat(StringFunctions.replace("input", "", ""), is("input"));
		assertThat(StringFunctions.replace("input", null, ""), is("input"));
		assertThat(StringFunctions.replace("i1n2p3ut", "\\D", " "), is(" 1 2 3  "));
		assertThat(StringFunctions.replace("input", "(pu)", "$1"), is("input"));
	}

	private static class TestObject {
		@Override
		public String toString() {
			return "test object string";
		}
	}
}
