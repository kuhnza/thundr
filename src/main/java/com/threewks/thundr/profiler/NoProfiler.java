package com.threewks.thundr.profiler;

import java.util.UUID;

public class NoProfiler implements Profiler {

	public NoProfiler() {
	}

	@Override
	public void beginProfileSession(String data) {
	}

	@Override
	public void endProfileSession() {
	}

	@Override
	public UUID start(String category, String data) {
		return null;
	}

	@Override
	public void end(UUID eventKey) {
	}

	@Override
	public void end(UUID eventKey, ProfileEventStatus status) {
	}

	@Override
	public ProfileSession getCurrent() {
		return new ProfileSession("");
	}

	@Override
	public <T> T profile(String category, String data, Profilable<T> profilable) {
		return profilable.profile();
	}
}
