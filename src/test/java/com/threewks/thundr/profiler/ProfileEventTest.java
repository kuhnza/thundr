package com.threewks.thundr.profiler;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ProfileEventTest {
	@Test
	public void shouldRetainCategoryDataAndInitialiseStart() {
		ProfileEvent profileEvent = new ProfileEvent("cat", "data");
		assertThat(profileEvent.getCategory(), is("cat"));
		assertThat(profileEvent.getData(), is("data"));
		assertThat(profileEvent.getStart(), greaterThan(0L));
		assertThat(profileEvent.getEnd(), is(-1L));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Unknown));
		assertThat(profileEvent.getKey(), is(notNullValue()));
	}

	@Test
	public void shouldUpdateEndValueAndStatus() {
		ProfileEvent profileEvent = new ProfileEvent("cat", "data");
		profileEvent.complete(ProfileEventStatus.Failed);

		assertThat(profileEvent.getEnd(), is(greaterThan(0L)));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Failed));
	}

	@Test
	public void shouldNotUpdateEndOrStatusIfAlreadyComplete() {
		ProfileEvent profileEvent = spy(new ProfileEvent("cat", "data"));
		when(profileEvent.now()).thenReturn(1234L, 5678L);
		profileEvent.complete(ProfileEventStatus.Success);

		assertThat(profileEvent.getEnd(), is(1234L));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Success));

		profileEvent.complete(ProfileEventStatus.Failed);
		assertThat(profileEvent.getEnd(), is(1234L));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Success));
	}

	@Test
	public void shouldAbbreviateProfileSessionDataTo255CharsIfLonger() {
		assertThat(new ProfileEvent(null, longString(255)).getData().length(), is(255));
		assertThat(new ProfileEvent(null, longString(255)).getData().contains("..."), is(false));
		assertThat(new ProfileEvent(null, longString(256)).getData().length(), is(255));
		assertThat(new ProfileEvent(null, longString(256)).getData().contains("..."), is(true));
		assertThat(new ProfileEvent(null, longString(256)).getData().endsWith("..."), is(false));
		assertThat(new ProfileEvent(null, longString(256)).getData().startsWith("..."), is(false));
	}

	private String longString(int len) {
		return StringUtils.rightPad("", len, 'a');
	}
}
