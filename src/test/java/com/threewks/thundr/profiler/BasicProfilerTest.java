package com.threewks.thundr.profiler;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Test;

import com.threewks.thundr.profiler.BasicProfiler.BasicProfilerCompletionStrategy;

public class BasicProfilerTest {
	private BasicProfilerCompletionStrategy completionStrategy = mock(BasicProfilerCompletionStrategy.class);
	private BasicProfiler basicProfiler = new BasicProfiler().withCompletionStrategy(completionStrategy);

	@Test
	public void shouldInvokeCompletionStrategyOnEnd() {
		basicProfiler.beginProfileSession("data");
		ProfileSession session = basicProfiler.getCurrent();
		basicProfiler.endProfileSession();

		verify(completionStrategy, times(1)).complete(session);
	}

	@Test
	public void shouldReturnCurrentSession() {
		basicProfiler.beginProfileSession("data");
		ProfileSession session = basicProfiler.getCurrent();
		assertThat(session, is(notNullValue()));
		ProfileSession second = basicProfiler.getCurrent();

		assertThat(session, sameInstance(second));
	}

	@Test
	public void shouldReturnNullForCurrentIfNoSessionStarted() {
		assertThat(basicProfiler.getCurrent(), is(nullValue()));
	}

	@Test
	public void shouldBeginANewProfileSession() {
		assertThat(basicProfiler.getCurrent(), is(nullValue()));
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent(), is(notNullValue()));
	}

	@Test
	public void shouldRemoveExistingProfileSessionAndBeginANewOne() {
		basicProfiler.beginProfileSession("data");
		ProfileSession first = basicProfiler.getCurrent();
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent(), is(not(sameInstance(first))));
	}

	@Test
	public void shouldRemoveCurrentSessionWhenEnded() {
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent(), is(notNullValue()));
		basicProfiler.endProfileSession();
		assertThat(basicProfiler.getCurrent(), is(nullValue()));
	}

	@Test
	public void shouldAddANewEventToTheCurrentProfileSessionAndReturnItsKey() {
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent().getEvents().isEmpty(), is(true));

		UUID start = basicProfiler.start("category", "event-data");
		assertThat(start, is(notNullValue()));
		ProfileEvent event = basicProfiler.getCurrent().getEvents().get(0);
		assertThat(event.getKey(), is(start));
		assertThat(event.getCategory(), is("category"));
		assertThat(event.getData(), is("event-data"));
	}

	@Test
	public void shouldEndEventWithSuccess() {
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent().getEvents().isEmpty(), is(true));

		UUID start = basicProfiler.start("category", "event-data");
		basicProfiler.end(start);

		ProfileEvent event = basicProfiler.getCurrent().getEvents().get(0);
		assertThat(event.getKey(), is(start));
		assertThat(event.getCategory(), is("category"));
		assertThat(event.getData(), is("event-data"));
		assertThat(event.getEnd(), is(greaterThan(0L)));
		assertThat(event.getStatus(), is(ProfileEventStatus.Success));
	}

	@Test
	public void shouldEndEventWithSpecifiedStatus() {
		basicProfiler.beginProfileSession("data");
		assertThat(basicProfiler.getCurrent().getEvents().isEmpty(), is(true));

		UUID start = basicProfiler.start("category", "event-data");
		basicProfiler.end(start, ProfileEventStatus.Timeout);

		ProfileEvent event = basicProfiler.getCurrent().getEvents().get(0);
		assertThat(event.getKey(), is(start));
		assertThat(event.getCategory(), is("category"));
		assertThat(event.getData(), is("event-data"));
		assertThat(event.getEnd(), is(greaterThan(0L)));
		assertThat(event.getStatus(), is(ProfileEventStatus.Timeout));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldInvokeProfilableRecordingASuccessEvent() {
		basicProfiler.beginProfileSession("data");
		Profilable<Object> profilable = mock(Profilable.class);
		when(profilable.profile()).thenReturn("ok!");
		Object result = basicProfiler.profile("cat", "event-data", profilable);
		assertThat(result, is((Object) "ok!"));
		verify(profilable, times(1)).profile();
		ProfileEvent event = basicProfiler.getCurrent().getEvents().get(0);
		assertThat(event.getStatus(), is(ProfileEventStatus.Success));
		assertThat(event.getCategory(), is("cat"));
		assertThat(event.getData(), is("event-data"));
		assertThat(event.getEnd(), is(greaterThan(0L)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldInvokeProfilableRecordingAFailedEventOnException() {
		basicProfiler.beginProfileSession("data");
		Profilable<Object> profilable = mock(Profilable.class);
		when(profilable.profile()).thenThrow(new RuntimeException("expected"));
		try {
			basicProfiler.profile("cat", "event-data", profilable);
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("expected"));
		}
		verify(profilable, times(1)).profile();
		ProfileEvent event = basicProfiler.getCurrent().getEvents().get(0);
		assertThat(event.getStatus(), is(ProfileEventStatus.Failed));
		assertThat(event.getCategory(), is("cat"));
		assertThat(event.getData(), is("event-data"));
		assertThat(event.getEnd(), is(greaterThan(0L)));
	}

	@Test
	public void shouldBeAbleToEndProfileWithoutFailingAfterProfileSessionIsClosed() {
		basicProfiler.beginProfileSession("data");
		UUID key = basicProfiler.start("category", "data");
		basicProfiler.endProfileSession();
		basicProfiler.end(key);
	}

	@Test
	public void shouldBeAbleToEndProfileWithStatusWithoutFailingAfterProfileSessionIsClosed() {
		basicProfiler.beginProfileSession("data");
		UUID key = basicProfiler.start("category", "data");
		basicProfiler.endProfileSession();
		basicProfiler.end(key, ProfileEventStatus.Timeout);
	}
}
