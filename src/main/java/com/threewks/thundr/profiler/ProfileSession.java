package com.threewks.thundr.profiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * A {@link ProfileSession} is a group of {@link ProfileEvent} instances.
 * A session allows us to view the events relative to eachother and the
 * overall session ( which usually parallels a request).
 */
public class ProfileSession {
	private String data;
	private long start = getNow();
	private long end = -1;
	private LinkedHashMap<UUID, ProfileEvent> events = new LinkedHashMap<UUID, ProfileEvent>();

	public ProfileSession(String data) {
		this.data = trim(data);
	}

	public void end() {
		end = getNow();
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public String getData() {
		return data;
	}

	public List<ProfileEvent> getEvents() {
		return new ArrayList<ProfileEvent>(events.values());
	}

	public void start(ProfileEvent profileEvent) {
		events.put(profileEvent.getKey(), profileEvent);
	}

	public void end(UUID eventKey, ProfileEventStatus status) {
		events.get(eventKey).complete(status);
	}

	public long getNow() {
		return System.currentTimeMillis();
	}

	public static String trim(String data) {
		return StringUtils.abbreviateMiddle(data, "...", 255);
	}
}
