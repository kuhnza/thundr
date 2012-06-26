package com.threewks.thundr.profiler;

import java.util.Date;
import java.util.UUID;

import com.threewks.thundr.logger.Logger;

public class BasicProfiler implements Profiler {
	private ThreadLocal<ProfileSession> profileSession = new ThreadLocal<ProfileSession>();
	private BasicProfilerCompletionStrategy completionStrategy;

	public BasicProfiler(BasicProfilerCompletionStrategy completionStrategy) {
		this.completionStrategy = completionStrategy;
	}

	@Override
	public void beginProfileSession(String data) {
		profileSession.remove();
		profileSession.set(new ProfileSession(data));
	}

	@Override
	public void endProfileSession() {
		ProfileSession stack = profileSession.get();
		profileSession.remove();
		stack.end();
		completionStrategy.complete(stack);
	}

	@Override
	public UUID start(String category, String data) {
		ProfileSession stack = profileSession.get();
		ProfileEvent profileEvent = new ProfileEvent(category, data);
		stack.start(profileEvent);
		return profileEvent.getKey();
	}

	@Override
	public ProfileSession getCurrent() {
		return profileSession.get();
	}

	@Override
	public void end(UUID eventKey) {
		profileSession.get().end(eventKey, ProfileEventStatus.Success);
	}

	@Override
	public void end(UUID eventKey, ProfileEventStatus status) {
		profileSession.get().end(eventKey, status);
	}

	@Override
	public <T> T profile(String category, String data, Profilable<T> profilable) {
		UUID profileKey = this.start(category, data);
		try {
			T result = profilable.profile();
			this.end(profileKey, ProfileEventStatus.Success);
			return result;
		} catch (RuntimeException e) {
			this.end(profileKey, ProfileEventStatus.Failed);
			throw e;
		}
	}

	public interface BasicProfilerCompletionStrategy {
		public void complete(ProfileSession stack);
	}

	public static class StringDumpCompletionStrategy implements BasicProfilerCompletionStrategy {
		@Override
		public void complete(ProfileSession stack) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Profile results for %s (%s - %s, %sms)\n", stack.getData(), new Date(stack.getStart()), new Date(stack.getEnd()), stack.getEnd() - stack.getStart()));
			for (ProfileEvent event : stack.getEvents()) {
				sb.append(String.format("Event %s - %s, %s: %s - %s (%sms)\n", event.getStatus(), event.getCategory(), event.getData(), new Date(event.getStart()), new Date(event.getEnd()), event.getEnd() - event.getStart()));
			}
			Logger.info(sb.toString());
		}
	}

	public static class NoCompletionStrategy implements BasicProfilerCompletionStrategy {
		@Override
		public void complete(ProfileSession stack) {
		}
	}
}
