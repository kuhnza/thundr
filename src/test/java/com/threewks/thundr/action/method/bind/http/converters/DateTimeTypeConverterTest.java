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
package com.threewks.thundr.action.method.bind.http.converters;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class DateTimeTypeConverterTest {
	@SuppressWarnings("deprecation")
	@Test
	public void shouldCreateDateFromVariousValues() {
		DateTimeTypeConverter convertor = new DateTimeTypeConverter();
		assertThat(convertor.convert(null), is(nullValue()));
		assertThat(convertor.convert("2014-01-14").compareTo(new DateTime(2014, 1, 14, 0, 0, 0, 0)), is(0));
		assertThat(convertor.convert("2014-01-14T11:22:33.444+11:00").compareTo(new DateTime(2014, 1, 14, 11, 22, 33, 444).withZoneRetainFields(DateTimeZone.forOffsetHours(11))), is(0));
		assertThat(convertor.convert(new Date(2014 - 1900, 0, 14)).compareTo(new DateTime(2014, 1, 14, 0, 0, 0, 0)), is(0));
		assertThat(convertor.convert(new DateTime(2014, 1, 2, 3, 4).getMillis()).compareTo(new DateTime(2014, 1, 2, 3, 4)), is(0));
		assertThat(convertor.convert(new DateTime(2014, 1, 2, 3, 4)).compareTo(new DateTime(2014, 1, 2, 3, 4)), is(0));
	}
}
