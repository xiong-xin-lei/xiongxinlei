package com.bsg.dbscale.util.http;

import java.io.IOException;
import java.util.Map;

/**
 * HttpClient tool
 * 
 * @author HCK
 *
 */
public class HttpClientUtil {

    private final HttpTransport httpTransport;

    public HttpClientUtil() {
        this(new DefaultHttpTransport());
    }

    public HttpClientUtil(int socketTimeout) {
        this(new DefaultHttpTransport(socketTimeout));
    }

    public HttpClientUtil(boolean isHttps, String certificateContent) {
        this(new DefaultHttpTransport(isHttps, certificateContent));
    }

    public HttpClientUtil(boolean isHttps, String certificateContent, int socketTimeout) {
        this(new DefaultHttpTransport(isHttps, certificateContent, socketTimeout));
    }

    public HttpClientUtil(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    /**
     * send get request
     * 
     * @param url
     * @param headMap
     * @return
     * @throws IOException
     */
    public HttpResp sendGetRequest(String url, Map<String, String> headMap) throws IOException {
        return httpTransport.sendGetRequest(url, headMap);
    }

    /**
     * send post request
     * 
     * @param url
     * @param headMap
     * @param content
     * @return
     * @throws IOException
     */
    public HttpResp sendPostRequest(String url, Map<String, String> headMap, String content) throws IOException {
        return httpTransport.sendPostRequest(url, headMap, content);
    }

    /**
     * send put request
     * 
     * @param url
     * @param headMap
     * @param content
     * @return
     * @throws IOException
     */
    public HttpResp sendPutRequest(String url, Map<String, String> headMap, String content) throws IOException {
        return httpTransport.sendPutRequest(url, headMap, content);
    }

    /**
     * send delete request
     * 
     * @param url
     * @param headMap
     * @return
     * @throws IOException
     */
    public HttpResp sendDeleteRequest(String url, Map<String, String> headMap) throws IOException {
        return httpTransport.sendDeleteRequest(url, headMap);
    }

}
