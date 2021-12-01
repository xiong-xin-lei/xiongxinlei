package com.bsg.dbscale.util.http;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class DefaultHttpTransport extends AbstractHttpTransport {

    private final HttpClient httpClient;

    public DefaultHttpTransport() {
        this(DEFAULT_SOCKET_TIMEOUT);
    }

    public DefaultHttpTransport(Integer socketTimeout) {
        this(false, null, socketTimeout);
    }

    public DefaultHttpTransport(boolean isHttps, String certificateContent) {
        this(isHttps, certificateContent, DEFAULT_SOCKET_TIMEOUT);
    }

    public DefaultHttpTransport(boolean isHttps, String certificateContent, Integer socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT * 1000)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT * 1000)
                .setSocketTimeout(socketTimeout * 1000).build();

        // HttpClientBuilder httpClientBuilder =
        // HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
        // .setRetryHandler(new
        // HttpRequestRetryHandlerImpl(retryCount)).useSystemProperties();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .useSystemProperties();

        if (isHttps) {
            if (certificateContent == null) {
                X509TrustManager x509mgr = new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };

                SSLConnectionSocketFactory sslsf = null;
                try {
                    SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
                    sslContext.init(null, new TrustManager[] { x509mgr }, new java.security.SecureRandom());
                    sslsf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.httpClient = httpClientBuilder.setSSLSocketFactory(sslsf).build();
            } else {
                SSLConnectionSocketFactory sslsf = null;
                try {
                    // Load Certificate
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    // 这里的路径为证书存放路径
                    Certificate certificate = certificateFactory
                            .generateCertificate(new ByteArrayInputStream(certificateContent.getBytes()));

                    // Create TrustStore
                    KeyStore trustStoreContainingTheCertificate = KeyStore.getInstance("JKS");
                    trustStoreContainingTheCertificate.load(null, null);

                    // AddCertificate 第一个参数为证书别名, 可以任取
                    trustStoreContainingTheCertificate.setCertificateEntry(UUID.randomUUID().toString(), certificate);
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory
                            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(trustStoreContainingTheCertificate);

                    // Create SSLContext 我这里协议为TLSv1.3
                    SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
                    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                    sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            }
        } else {
            this.httpClient = httpClientBuilder.build();
        }

    }

    public DefaultHttpTransport(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    protected HttpClient getHttpClient() {
        return this.httpClient;
    }

}
