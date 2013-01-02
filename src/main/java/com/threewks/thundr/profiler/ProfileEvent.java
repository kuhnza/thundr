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
