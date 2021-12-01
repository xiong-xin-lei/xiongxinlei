package com.bsg.dbscale.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil {

    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static Integer SOCKETTIMEOUT_GET = 60;

    public static Integer SOCKETTIMEOUT_POST = 60;

    public static Integer SOCKETTIMEOUT_PUT = 60;

    public static Integer SOCKETTIMEOUT_DELETE = 300;

    public static HttpResp sendGetRequest(String url) throws IOException {
        return sendGetRequest(url, SOCKETTIMEOUT_GET);
    }

    public static HttpResp sendGetRequest(String url, String certificateContent) throws IOException {
        return sendGetRequest(url, SOCKETTIMEOUT_GET, certificateContent);
    }

    public static HttpResp sendGetRequest(String url, Integer socketTimeout) throws IOException {
        return sendGetRequest(url, null, socketTimeout);
    }

    public static HttpResp sendGetRequest(String url, Integer socketTimeout, String certificateContent)
            throws IOException {
        return sendGetRequest(url, null, socketTimeout, certificateContent);
    }

    public static HttpResp sendGetRequest(String url, Map<String, String> headMap) throws IOException {
        return sendGetRequest(url, headMap, SOCKETTIMEOUT_GET);
    }

    public static HttpResp sendGetRequest(String url, Map<String, String> headMap, String certificateContent)
            throws IOException {
        return sendGetRequest(url, headMap, SOCKETTIMEOUT_GET, certificateContent);
    }

    public static HttpResp sendGetRequest(String url, Map<String, String> headMap, Integer socketTimeout)
            throws IOException {
        return sendGetRequest(url, headMap, socketTimeout, null);
    }

    public static HttpResp sendGetRequest(String url, Map<String, String> headMap, Integer socketTimeout,
            String certificateContent) throws IOException {
        if (socketTimeout == null) {
            socketTimeout = SOCKETTIMEOUT_GET;
        }
        return sendRequest(url, HttpMethodEnum.GET, headMap, null, socketTimeout, certificateContent);
    }

    public static HttpResp sendGetRequest(String url, HttpClientUtil httpClientUtil) throws IOException {
        return sendGetRequest(url, null, httpClientUtil);
    }

    public static HttpResp sendGetRequest(String url, Map<String, String> headMap, HttpClientUtil httpClientUtil)
            throws IOException {
        return sendRequest(url, HttpMethodEnum.GET, headMap, null, httpClientUtil);
    }

    public static HttpResp sendPostRequest(String url, String bodyContent) throws IOException {
        return sendPostRequest(url, bodyContent, SOCKETTIMEOUT_POST, null);
    }

    public static HttpResp sendPostRequest(String url, String bodyContent, String certificateContent)
            throws IOException {
        return sendPostRequest(url, bodyContent, SOCKETTIMEOUT_POST, certificateContent);
    }

    public static HttpResp sendPostRequest(String url, String bodyContent, Integer socketTimeout) throws IOException {
        return sendPostRequest(url, bodyContent, socketTimeout, null);
    }

    public static HttpResp sendPostRequest(String url, String bodyContent, Integer socketTimeout,
            String certificateContent) throws IOException {
        return sendPostRequest(url, null, bodyContent, socketTimeout, certificateContent);
    }

    public static HttpResp sendPostRequest(String url, Map<String, String> headMap, String bodyContent)
            throws IOException {
        return sendPostRequest(url, headMap, bodyContent, SOCKETTIMEOUT_POST, null);
    }

    public static HttpResp sendPostRequest(String url, Map<String, String> headMap, String bodyContent,
            String certificateContent) throws IOException {
        return sendPostRequest(url, headMap, bodyContent, SOCKETTIMEOUT_POST, certificateContent);
    }

    public static HttpResp sendPostRequest(String url, Map<String, String> headMap, String bodyContent,
            Integer socketTimeout) throws IOException {
        return sendPostRequest(url, headMap, bodyContent, socketTimeout, null);
    }

    public static HttpResp sendPostRequest(String url, Map<String, String> headMap, String bodyContent,
            Integer socketTimeout, String certificateContent) throws IOException {
        if (socketTimeout == null) {
            socketTimeout = SOCKETTIMEOUT_POST;
        }
        return sendRequest(url, HttpMethodEnum.POST, headMap, bodyContent, socketTimeout, certificateContent);
    }

    public static HttpResp sendPostRequest(String url, String bodyContent, HttpClientUtil httpClientUtil)
            throws IOException {
        return sendPostRequest(url, null, bodyContent, httpClientUtil);
    }

    public static HttpResp sendPostRequest(String url, Map<String, String> headMap, String bodyContent,
            HttpClientUtil httpClientUtil) throws IOException {
        return sendRequest(url, HttpMethodEnum.POST, headMap, bodyContent, httpClientUtil);
    }

    public static HttpResp sendPutRequest(String url, String bodyContent) throws IOException {
        return sendPutRequest(url, bodyContent, SOCKETTIMEOUT_PUT, null);
    }

    public static HttpResp sendPutRequest(String url, String bodyContent, String certificateContent)
            throws IOException {
        return sendPutRequest(url, bodyContent, SOCKETTIMEOUT_PUT, certificateContent);
    }

    public static HttpResp sendPutRequest(String url, String bodyContent, Integer socketTimeout) throws IOException {
        return sendPutRequest(url, bodyContent, socketTimeout, null);
    }

    public static HttpResp sendPutRequest(String url, String bodyContent, Integer socketTimeout,
            String certificateContent) throws IOException {
        return sendPutRequest(url, null, bodyContent, socketTimeout, certificateContent);
    }

    public static HttpResp sendPutRequest(String url, Map<String, String> headMap, String bodyContent)
            throws IOException {
        return sendPutRequest(url, headMap, bodyContent, SOCKETTIMEOUT_PUT, null);
    }

    public static HttpResp sendPutRequest(String url, Map<String, String> headMap, String bodyContent,
            String certificateContent) throws IOException {
        return sendPutRequest(url, headMap, bodyContent, SOCKETTIMEOUT_PUT, certificateContent);
    }

    public static HttpResp sendPutRequest(String url, Map<String, String> headMap, String bodyContent,
            Integer socketTimeout) throws IOException {
        return sendPutRequest(url, headMap, bodyContent, socketTimeout, null);
    }

    public static HttpResp sendPutRequest(String url, Map<String, String> headMap, String bodyContent,
            Integer socketTimeout, String certificateContent) throws IOException {
        if (socketTimeout == null) {
            socketTimeout = SOCKETTIMEOUT_PUT;
        }
        return sendRequest(url, HttpMethodEnum.PUT, headMap, bodyContent, socketTimeout, certificateContent);
    }

    public static HttpResp sendPutRequest(String url, String bodyContent, HttpClientUtil httpClientUtil)
            throws IOException {
        return sendPutRequest(url, null, bodyContent, httpClientUtil);
    }

    public static HttpResp sendPutRequest(String url, Map<String, String> headMap, String bodyContent,
            HttpClientUtil httpClientUtil) throws IOException {
        return sendRequest(url, HttpMethodEnum.PUT, headMap, bodyContent, httpClientUtil);
    }

    public static HttpResp sendDeleteRequest(String url) throws IOException {
        return sendDeleteRequest(url, SOCKETTIMEOUT_DELETE);
    }

    public static HttpResp sendDeleteRequest(String url, String certificateContent) throws IOException {
        return sendDeleteRequest(url, SOCKETTIMEOUT_DELETE, certificateContent);
    }

    public static HttpResp sendDeleteRequest(String url, Integer socketTimeout) throws IOException {
        return sendDeleteRequest(url, null, socketTimeout);
    }

    public static HttpResp sendDeleteRequest(String url, Integer socketTimeout, String certificateContent)
            throws IOException {
        return sendDeleteRequest(url, null, socketTimeout, certificateContent);
    }

    public static HttpResp sendDeleteRequest(String url, Map<String, String> headMap) throws IOException {
        return sendDeleteRequest(url, headMap, SOCKETTIMEOUT_DELETE);
    }

    public static HttpResp sendDeleteRequest(String url, Map<String, String> headMap, String certificateContent)
            throws IOException {
        return sendDeleteRequest(url, headMap, SOCKETTIMEOUT_DELETE, certificateContent);
    }

    public static HttpResp sendDeleteRequest(String url, Map<String, String> headMap, Integer socketTimeout)
            throws IOException {
        return sendDeleteRequest(url, headMap, socketTimeout, null);
    }

    public static HttpResp sendDeleteRequest(String url, Map<String, String> headMap, Integer socketTimeout,
            String certificateContent) throws IOException {
        if (socketTimeout == null) {
            socketTimeout = SOCKETTIMEOUT_DELETE;
        }
        return sendRequest(url, HttpMethodEnum.DELETE, headMap, null, socketTimeout, certificateContent);
    }

    public static HttpResp sendDeleteRequest(String url, HttpClientUtil httpClientUtil) throws IOException {
        return sendDeleteRequest(url, null, httpClientUtil);
    }

    public static HttpResp sendDeleteRequest(String url, Map<String, String> headMap, HttpClientUtil httpClientUtil)
            throws IOException {
        return sendRequest(url, HttpMethodEnum.DELETE, headMap, null, httpClientUtil);
    }

    private static HttpResp sendRequest(String url, HttpMethodEnum httpMethodEnum, Map<String, String> headMap,
            String content, int socketTimeout, String certificateContent) throws IOException {
        boolean isHttps = false;
        if (url.startsWith("https")) {
            isHttps = true;
        }
        HttpClientUtil httpClientUtil = new HttpClientUtil(isHttps, certificateContent, socketTimeout);
        return sendRequest(url, httpMethodEnum, headMap, content, httpClientUtil);
    }

    private static HttpResp sendRequest(String url, HttpMethodEnum httpMethodEnum, Map<String, String> headMap,
            String content, HttpClientUtil httpClientUtil) throws IOException {
        logger.info("Http Request URL: {}", url);
        logger.info("Http Request Method: {}", httpMethodEnum);

        if (StringUtils.isNoneBlank(content)) {
            logger.info("Http Request Data: {}", content);
            content = JSONObject.toJSONString(JSONObject.parse(content));
        }

        Long now = System.currentTimeMillis();
        HttpResp httpResp = null;

        switch (httpMethodEnum) {
        case GET:
            httpResp = httpClientUtil.sendGetRequest(url, headMap);
            break;
        case POST:
            httpResp = httpClientUtil.sendPostRequest(url, headMap, content);
            break;
        case PUT:
            httpResp = httpClientUtil.sendPutRequest(url, headMap, content);
            break;
        case DELETE:
            httpResp = httpClientUtil.sendDeleteRequest(url, headMap);
            break;
        default:
            break;
        }

        logger.info("Http Response Time: {}ms", System.currentTimeMillis() - now);
        if (httpResp.getStatusCode() != HttpStatus.SC_OK && httpResp.getStatusCode() != HttpStatus.SC_CREATED
                && httpResp.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
            logger.error("Http Response Status: {}", httpResp.getStatusCode());
            logger.error("Http Response Content: {}", httpResp.getResponseContent());
        } else {
            logger.info("Http Response Status: {}", httpResp.getStatusCode());
            logger.info("Http Response Content: {}", httpResp.getResponseContent());
        }

        return httpResp;
    }

    public static void main(String[] args) throws IOException {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("version", "1.0");

        HttpResp httpResp = sendGetRequest("http://192.168.49.21:8088/dbscale/sites", headMap, httpClientUtil);
        System.out.println(httpResp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "root");
        jsonObject.put("password", "root000000");
        httpResp = sendPostRequest("http://192.168.49.21:8088/dbscale/login", headMap, jsonObject.toJSONString(),
                httpClientUtil);
        System.out.println(httpResp);

        httpResp = sendGetRequest("http://192.168.49.21:8088/dbscale/sites", headMap, httpClientUtil);
        System.out.println(httpResp);
    }
}
