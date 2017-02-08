package com.wut.datasources.cloudflare;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpPutWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "PUT";

    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpPutWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}
