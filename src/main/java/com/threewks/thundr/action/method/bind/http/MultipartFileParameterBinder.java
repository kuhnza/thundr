package com.threewks.thundr.action.method.bind.http;

import com.threewks.thundr.introspection.ParameterDescription;

public class MultipartFileParameterBinder implements BinaryParameterBinder<MultipartFile> {

    @Override
    public boolean willBind(ParameterDescription parameterDescription) {
        return parameterDescription.isA(MultipartFile.class);
    }

    @Override
    public MultipartFile bind(ParameterDescription parameterDescription, MultipartFile file) {
        return file;
    }
}
