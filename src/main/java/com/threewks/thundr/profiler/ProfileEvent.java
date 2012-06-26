package com.threewks.thundr.profiler;

import static com.threewks.thundr.profiler.ProfileSession.trim;

import java.util.UUID;

/**
 * A single even occurring during a profile session.
 * Events have a defined beginning and end, and complete with a status defined in {@link ProfileEventStatus}
 */
public class ProfileEvent {
	private UUID key = UUID.randomUUID();
	private String category;
	private String data;
	private long start;
	private long end = -1;
	private ProfileEventStatus status = ProfileEventStatus.Unknown;

	public ProfileEvent(String category, String data) {
		this.category = category;
		this.data = trim(data);
		this.start = now();
	}

	public UUID getKey() {
		return key;
	}

	public String getCategory() {
		return category;
	}

	public String getData() {
		return data;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public ProfileEventStatus getStatus() {
		return status;
	}

	public void complete(ProfileEventStatus status) {
		// ignore dual completion
		if (end < 0) {
			end = now();
			this.status = status;
		}
	}

	protected long now() {
		return System.currentTimeMillis();
	}

}
