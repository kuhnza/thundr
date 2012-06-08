package com.threewks.thundr.action.method.bind.http;

import com.threewks.thundr.introspection.ParameterDescription;

public class ByteArrayBinaryParameterBinder implements BinaryParameterBinder<byte[]> {
	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.getArrayType() == byte.class;
	}

	@Override
	public byte[] bind(ParameterDescription parameterDescription, byte[] data) {
		return data;
	}
}
