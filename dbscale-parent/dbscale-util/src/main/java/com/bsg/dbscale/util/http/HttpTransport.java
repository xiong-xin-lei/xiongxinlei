package com.bsg.dbscale.util.http;

import java.io.IOException;
import java.util.Map;

public interface HttpTransport {

    public HttpResp sendGetRequest(String url, Map<String, String> headerMap) throws IOException;

    public HttpResp sendPostRequest(String url, Map<String, String> headerMap, String content) throws IOException;

    public HttpResp sendPutRequest(String url, Map<String, String> headerMap, String content) throws IOException;

    public HttpResp sendDeleteRequest(String url, Map<String, String> headerMap) throws IOException;
}
