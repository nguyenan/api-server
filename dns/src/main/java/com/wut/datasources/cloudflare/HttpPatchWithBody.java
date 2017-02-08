package com.wut.datasources.cloudflare;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpPatchWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "PATCH";

    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpPatchWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}
