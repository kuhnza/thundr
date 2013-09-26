package com.threewks.thundr.action.method.bind.http;

public class MultipartFile {

    private byte[] data;
    private String name;
    private String contentType;

    public MultipartFile(String name, byte[] data, String contentType) {
        this.data = data;
        this.name = name;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }
}
