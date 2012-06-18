package com.threewks.thundr.action.method.bind.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.threewks.thundr.introspection.ParameterDescription;

public class InputStreamBinaryParameterBinder implements BinaryParameterBinder<InputStream> {
	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.isA(InputStream.class);
	}

	@Override
	public InputStream bind(ParameterDescription parameterDescription, byte[] data) {
		return new ByteArrayInputStream(data);
	}
}
