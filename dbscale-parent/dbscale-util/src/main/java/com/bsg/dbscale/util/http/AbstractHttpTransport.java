package com.bsg.dbscale.util.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public abstract class AbstractHttpTransport implements HttpTransport {

    static final int DEFAULT_SOCKET_TIMEOUT = 60; // 60 sec
    static final int DEFAULT_CONNECTION_TIMEOUT = 2; // 2 sec
    static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 2; // 2 sec

    protected abstract HttpClient getHttpClient();

    @Override
    public HttpResp sendGetRequest(String url, Map<String, String> headerMap) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return sendRequest(httpGet, headerMap);
    }

    @Override
    public HttpResp sendPostRequest(String url, Map<String, String> headerMap, String content) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (content != null) {
            StringEntity stringEntity = new StringEntity(content, "UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
        }
        return sendRequest(httpPost, headerMap);
    }

    @Override
    public HttpResp sendPutRequest(String url, Map<String, String> headerMap, String content) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        if (content != null) {
            StringEntity stringEntity = new StringEntity(content, "UTF-8");
            stringEntity.setContentType("application/json");
            httpPut.setEntity(stringEntity);
        }
        return sendRequest(httpPut, headerMap);
    }

    @Override
    public HttpResp sendDeleteRequest(String url, Map<String, String> headerMap) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        return sendRequest(httpDelete, headerMap);
    }

    private HttpResp sendRequest(HttpUriRequest httpRequest, Map<String, String> headerMap) throws IOException {
        HttpClient httpClient = getHttpClient();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse response = httpClient.execute(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String responseContent = null;
        if (entity != null) {
            responseContent = EntityUtils.toString(entity, "UTF-8");
        }
        return new HttpResp(statusCode, responseContent);
    }

}
