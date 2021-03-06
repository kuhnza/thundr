/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.profiler;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ProfileSessionTest {

	@Test
	public void shouldRetainDataAndRecordSessionStartTime() {
		ProfileSession profileSession = new ProfileSession("data");
		assertThat(profileSession.getData(), is("data"));
		assertThat(profileSession.getStart(), is(greaterThan(0L)));
		assertThat(profileSession.getEnd(), is(-1L));
		assertThat(profileSession.getEvents().isEmpty(), is(true));
	}

	@Test
	public void shouldSetEndTimeWhenEndingProfileSession() {
		ProfileSession profileSession = new ProfileSession("data");
		profileSession.end();
		assertThat(profileSession.getEnd(), is(greaterThan(0L)));
	}

	@Test
	public void shouldBeAbleToAddAProfileEventByRecordingItsStart() {
		ProfileSession profileSession = new ProfileSession("data");
		ProfileEvent profileEvent = new ProfileEvent("catgory", "event-data");
		profileSession.start(profileEvent);
		assertThat(profileSession.getEvents(), hasItem(profileEvent));
	}

	@Test
	public void shouldBeAbleToEndAnAddedEventByRecordingItsEnd() {
		ProfileSession profileSession = new ProfileSession("data");
		ProfileEvent profileEvent = new ProfileEvent("catgory", "event-data");
		profileSession.start(profileEvent);

		assertThat(profileEvent.getEnd(), is(-1L));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Unknown));

		profileSession.end(profileEvent.getKey(), ProfileEventStatus.Success);

		assertThat(profileEvent.getEnd(), is(greaterThan(0L)));
		assertThat(profileEvent.getStatus(), is(ProfileEventStatus.Success));
	}

	@Test
	public void shouldAbbreviateProfileSessionDataTo255CharsIfLonger() {
		assertThat(new ProfileSession(longString(255)).getData().length(), is(255));
		assertThat(new ProfileSession(longString(255)).getData().contains("..."), is(false));
		assertThat(new ProfileSession(longString(256)).getData().length(), is(255));
		assertThat(new ProfileSession(longString(256)).getData().contains("..."), is(true));
		assertThat(new ProfileSession(longString(256)).getData().endsWith("..."), is(false));
		assertThat(new ProfileSession(longString(256)).getData().startsWith("..."), is(false));
	}

	private String longString(int len) {
		return StringUtils.rightPad("", len, 'a');
	}
}
