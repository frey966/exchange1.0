package com.financia.system.crypto.bean;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * @author Leo
 * @ClassName: HttpHelper
 * @Description:
 */
public class HttpHelper {

    private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);
    private static final CloseableHttpAsyncClient httpClient;

    static {

        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();
        // 设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(5000);
        cm.setDefaultMaxPerRoute(1000);

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        // 客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        // 从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        // 连接建立后，request没有回应的timeout
        requestConfigBuilder.setSocketTimeout(30000);
        HttpAsyncClientBuilder clientBuilder = HttpAsyncClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        // 连接建立后，request没有回应的timeout
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        clientBuilder.setConnectionManager(cm);
        clientBuilder.disableCookieManagement();
        clientBuilder.disableAuthCaching();
        //clientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
        httpClient = clientBuilder.build();
        httpClient.start();
    }

    public static String getMethod(String url) {
        HttpGet httpGet = new HttpGet(url);
        final String ip = getRandomIp();
        httpGet.setHeader("x-forwarded-for", ip);
        httpGet.setHeader("client-ip", ip);
        httpGet.setHeader("Content-Type", "text/xml; charset=utf-8");
        httpGet.setHeader("User-Agent", "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36\"");
        HttpResponse response;
        try {
            final Future<HttpResponse> future = httpClient.execute(httpGet, null, null);
            response = future.get();
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }

    public static String getNoRandMethod(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        try {
            final Future<HttpResponse> future = httpClient.execute(httpGet, null, null);
            response = future.get();
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }


    public static String getMethodByHeader(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        try {
            if (headers != null && headers.size() > 0) {
                headers.forEach((k, v) -> {
                    httpGet.setHeader(k, v);
                });
            }
            final Future<HttpResponse> future = httpClient.execute(httpGet, null, null);
            response = future.get();
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }

    private static String getRandomIp() {

        // ip范围
        int[][] range = {
                {607649792, 608174079}, // 36.56.0.0-36.63.255.255
                {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
                {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
                {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
                {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
                {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
                {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
                {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
                {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
                {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
        };

        Random random = new Random();
        int index = random.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }


    private static String num2ip(int ip) {
        int[] b = new int[4];
        String ipStr = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        ipStr = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return ipStr;
    }

    public static String postMethod(String url, Map<String, Object> param) {
        log.info("HTTP请求URL:" + url);
        log.info("HTTP请求参数:" + JSON.toJSONString(param));
        HttpPost httpost = new HttpPost(url);
        HttpResponse response;
        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
            httpost.setEntity(entity);
            Future<HttpResponse> future = httpClient.execute(httpost, null, null);
            response = future.get();
            final int code = response.getStatusLine().getStatusCode();
            if (code == 301 || code == 302) {
                String url301 = response.getHeaders("Location")[0].toString().replaceFirst("Location:", "").trim();
                httpost.setURI(new URI(url301));
                future = httpClient.execute(httpost, null, null);
                response = future.get();
            }
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
            return ex.getMessage();
        } finally {
            httpost.releaseConnection();
        }
    }

    public static String postFormMethod(String url, Map<String, Object> param) {
        log.info("HTTP请求URL:" + url);
        log.info("HTTP请求参数:" + JSON.toJSONString(param));
        HttpPost httpost = new HttpPost(url);
        HttpResponse response;
        try {
            httpost.setHeader("Content-type", "application/x-www-form-urlencoded");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (param != null) {
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    if (entry != null && entry.getValue() != null) {
                        nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                    }
                }
            }
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            Future<HttpResponse> future = httpClient.execute(httpost, null, null);
            response = future.get();
            final int code = response.getStatusLine().getStatusCode();
            if (code == 301 || code == 302) {
                String url301 = response.getHeaders("Location")[0].toString().replaceFirst("Location:", "").trim();
                httpost.setURI(new URI(url301));
                future = httpClient.execute(httpost, null, null);
                response = future.get();
            }
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
            return ex.getMessage();
        } finally {
            httpost.releaseConnection();
        }
    }

    public static String postHeaderMethod(String url, Map<String, String> headers, Map<String, Object> param) throws IOException {
        log.info("HTTP请求URL:" + url);
        log.info("HTTP请求参数:" + JSON.toJSONString(param));
        HttpPost httpost = new HttpPost(url);
        HttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
            httpost.setEntity(entity);
            if (headers != null && headers.size() > 0) {
                headers.forEach((k, v) -> {
                    httpost.setHeader(k, v);
                });
            }
            Future<HttpResponse> future = httpClient.execute(httpost, null, null);
            response = future.get();
            final int code = response.getStatusLine().getStatusCode();
            if (code == 301 || code == 302) {
                String url301 = response.getHeaders("Location")[0].toString().replaceFirst("Location:", "").trim();
                httpost.setURI(new URI(url301));
                future = httpClient.execute(httpost, null, null);
                response = future.get();
            }
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
            return ex.getMessage();
        } finally {
            httpost.releaseConnection();
        }
    }

    public static String postHeaderMethod2(String url, Map<String, String> headers, Map<String, Object> param, int num) throws IOException {
        log.info("HTTP请求URL:" + url);
        HttpPost httpost = new HttpPost(url);
        HttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON);
            httpost.setEntity(entity);
            if (headers != null && headers.size() > 0) {
                headers.forEach((k, v) -> {
                    httpost.setHeader(k, v);
                });
            }
            Future<HttpResponse> future = httpClient.execute(httpost, null, null);
            response = future.get();
            final int code = response.getStatusLine().getStatusCode();
            if (code == 301 || code == 302) {
                String url301 = response.getHeaders("Location")[0].toString().replaceFirst("Location:", "").trim();
                httpost.setURI(new URI(url301));
                future = httpClient.execute(httpost, null, null);
                response = future.get();
            }
            if (response.getStatusLine().getStatusCode() != 200 && num < 3) {
                postHeaderMethod2(url, headers, param, num);
            }
            num++;
            return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("http error:", ex);
            return ex.getMessage();
        } finally {
            httpost.releaseConnection();
        }
    }

    public static String httpReq(String url, String contentType, String reqMethod, Map<String, Object> param) {
        if (reqMethod.equals("POST") && contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return postMethod(url, param);
        } else if (reqMethod.equals("POST") && contentType.equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
            return postFormMethod(url, param);
        } else {
            return "";
        }
    }

    public static String httpHeadersReq(String url, Map<String, String> headers, String contentType, String reqMethod, Map<String, Object> param) {
        if (reqMethod.equals("POST") && contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
            try {
                return postHeaderMethod(url, headers, param);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

}
