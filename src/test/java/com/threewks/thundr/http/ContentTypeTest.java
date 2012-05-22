package com.threewks.thundr.http;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ContentTypeTest {

	@Test
	public void shouldReturnCleanContentType() {
		assertThat(ContentType.cleanContentType(null), is(nullValue()));
		assertThat(ContentType.cleanContentType(""), is(""));
		assertThat(ContentType.cleanContentType("  "), is("  "));
		assertThat(ContentType.cleanContentType(" junk "), is(" junk "));

		assertThat(ContentType.cleanContentType("text/plain"), is("text/plain"));
		assertThat(ContentType.cleanContentType("text/plain; charset=UTF-8"), is("text/plain"));
		assertThat(ContentType.cleanContentType("text/plain ; charset=UTF-8"), is("text/plain"));

		assertThat(ContentType.cleanContentType("application/x-www-form-urlencoded"), is("application/x-www-form-urlencoded"));
		assertThat(ContentType.cleanContentType("application/x-www-form-urlencoded;"), is("application/x-www-form-urlencoded"));
		assertThat(ContentType.cleanContentType("application/x-www-form-urlencoded; charset=UTF-8"), is("application/x-www-form-urlencoded"));
		assertThat(ContentType.cleanContentType("application/x-www-form-urlencoded ; charset=UTF-8"), is("application/x-www-form-urlencoded"));

		for (ContentType contentType : ContentType.values()) {
			assertThat(ContentType.cleanContentType(contentType.value()), is(contentType.value()));
			assertThat(ContentType.cleanContentType(contentType.value().toUpperCase()), is(contentType.value()));
			assertThat(ContentType.cleanContentType(contentType.value() + "; charset=UTF-8"), is(contentType.value()));
		}
	}

	@Test
	public void shouldReturnTrueIfMatches() {
		for (ContentType contentType : ContentType.values()) {
			assertThat(contentType.matches(contentType.value()), is(true));
			assertThat(contentType.matches(contentType.value().toUpperCase()), is(true));
			assertThat(contentType.matches(contentType.value() + "; charset=UTF-8"), is(true));
			assertThat(contentType.matches("a" + contentType.value() + "; charset=UTF-8"), is(false));
		}
	}

	@Test
	public void shouldReturnContentTypeFromString() {
		for (ContentType contentType : ContentType.values()) {
			assertThat(ContentType.from(contentType.value()), is(contentType));
			assertThat(ContentType.from(contentType.value().toUpperCase()), is(contentType));
			assertThat(ContentType.from(contentType.value() + "; charset=UTF-8"), is(contentType));
			assertThat(ContentType.from("a" + contentType.value() + "; charset=UTF-8"), is(nullValue()));
		}
	}
}
