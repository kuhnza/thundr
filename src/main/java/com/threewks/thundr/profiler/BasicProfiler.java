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

import java.util.Date;
import java.util.UUID;

import com.threewks.thundr.logger.Logger;

public class BasicProfiler implements Profiler {
	private ThreadLocal<ProfileSession> profileSession = new ThreadLocal<ProfileSession>();
	private BasicProfilerCompletionStrategy completionStrategy = new NoCompletionStrategy();

	public BasicProfiler() {
	}

	public void setCompletionStrategy(BasicProfilerCompletionStrategy completionStrategy) {
		this.completionStrategy = completionStrategy;
	}

	public BasicProfiler withCompletionStrategy(BasicProfilerCompletionStrategy completionStrategy) {
		setCompletionStrategy(completionStrategy);
		return this;
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
		if (stack != null) {
			stack.end();
			completionStrategy.complete(stack);
		}
	}

	@Override
	public UUID start(String category, String data) {
		ProfileSession stack = getCurrent();
		ProfileEvent profileEvent = new ProfileEvent(category, data);
		if (stack != null) {
			stack.start(profileEvent);
		}
		return profileEvent.getKey();
	}

	@Override
	public ProfileSession getCurrent() {
		return profileSession.get();
	}

	@Override
	public void end(UUID eventKey) {
		end(eventKey, ProfileEventStatus.Success);
		this.end(eventKey, ProfileEventStatus.Success);
	}

	@Override
	public void end(UUID eventKey, ProfileEventStatus status) {
		ProfileSession current = getCurrent();
		if (current != null) {
			current.end(eventKey, status);
		}
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
				sb.append(String.format("Event %s - %s, %s: %s - %s (%sms)\n", event.getStatus(), event.getCategory(), event.getData(), new Date(event.getStart()), new Date(event.getEnd()),
						event.getEnd() - event.getStart()));
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
