/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.text.TextUtils;

import com.k2.mobile.app.model.http.callback.HttpRedirectHandler;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.client.DefaultSSLSocketFactory;
import com.k2.mobile.app.model.http.client.HttpRequest;
import com.k2.mobile.app.model.http.client.RetryHandler;
import com.k2.mobile.app.model.http.client.entity.GZipDecompressingEntity;
import com.k2.mobile.app.model.task.PriorityExecutor;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.OtherUtil;

/**
 * @ClassName: HttpUtils
 * @Description: HTTP请求控制工具类
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
public class HttpUtils {

    public final static HttpCache sHttpCache = new HttpCache();

    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext = new BasicHttpContext();

    private HttpRedirectHandler httpRedirectHandler;

    public HttpUtils() {
        this(HttpUtils.DEFAULT_CONN_TIMEOUT, null);
    }

    public HttpUtils(int connTimeout) {
        this(connTimeout, null);
    }

    public HttpUtils(String userAgent) {
        this(HttpUtils.DEFAULT_CONN_TIMEOUT, userAgent);
    }

    public HttpUtils(int connTimeout, String userAgent) {
        HttpParams params = new BasicHttpParams();

        ConnManagerParams.setTimeout(params, connTimeout);
        HttpConnectionParams.setSoTimeout(params, connTimeout);
        HttpConnectionParams.setConnectionTimeout(params, connTimeout);

        if (TextUtils.isEmpty(userAgent)) {
            userAgent = OtherUtil.getUserAgent(null);
        }
        HttpProtocolParams.setUserAgent(params, userAgent);

        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(params, 10);

        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 1024 * 8);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", DefaultSSLSocketFactory.getSocketFactory(), 443));

        httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);

        httpClient.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_RETRY_TIMES));

        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(org.apache.http.HttpRequest httpRequest, HttpContext httpContext) throws org.apache.http.HttpException, IOException {
                LogUtil.i("HttpUtils - process()");
            	if (!httpRequest.containsHeader(HEADER_ACCEPT_ENCODING)) {
            		LogUtil.i("HttpUtils - addHeader()");
                    httpRequest.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext httpContext) throws org.apache.http.HttpException, IOException {
            	 LogUtil.i("HttpUtils - process()");
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                    	LogUtil.i("HttpUtils - element: "+element.getName());
                        if (element.getName().equalsIgnoreCase("gzip") || isGZipData(response)) {
                            response.setEntity(new GZipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
    }
    
    /**
     * @Title: isGZipData
     * @Description: 增强判断报头是否GZip格式的能力
     * @param @param header
     * @param     设定文件
     * @return boolean    返回类型
     * @throws
     */
	private static boolean isGZipData(HttpResponse response) {
		boolean isGZip = false;
		try {
			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(2);
			// 取前两个字节
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset输入流到开始位置
			bis.reset();
			// 判断是否是GZIP格式
			int headerData = getShort(header);
			// Gzip 流 的前两个字节是 0x1f8b
			if (result != -1 && headerData == 0x1f8b) {
				isGZip = true;
			} else {
				isGZip = false;
			}
			is.close();
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isGZip;
	}

	/**
	 * @Title: getShort
	 * @Description: 字节转int类型
	 * @param @param data
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
    private static int getShort(byte[] data) {
        return (data[0]<<8) | data[1]&0xFF;
    }

    // ************************************    default settings & fields ****************************

    private String responseTextCharset = HTTP.UTF_8;

    private long currentRequestExpiry = HttpCache.getDefaultExpiryTime();

    private final static int DEFAULT_CONN_TIMEOUT = 1000 * 15; // 15s

    private final static int DEFAULT_RETRY_TIMES = 3;

    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private final static int DEFAULT_POOL_SIZE = 3;
    private final static PriorityExecutor EXECUTOR = new PriorityExecutor(DEFAULT_POOL_SIZE);

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    // ***************************************** config *******************************************

    public HttpUtils configResponseTextCharset(String charSet) {
        if (!TextUtils.isEmpty(charSet)) {
            this.responseTextCharset = charSet;
        }
        return this;
    }

    public HttpUtils configHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        this.httpRedirectHandler = httpRedirectHandler;
        return this;
    }

    public HttpUtils configHttpCacheSize(int httpCacheSize) {
        sHttpCache.setCacheSize(httpCacheSize);
        return this;
    }

    public HttpUtils configDefaultHttpCacheExpiry(long defaultExpiry) {
        HttpCache.setDefaultExpiryTime(defaultExpiry);
        currentRequestExpiry = HttpCache.getDefaultExpiryTime();
        return this;
    }

    public HttpUtils configCurrentHttpCacheExpiry(long currRequestExpiry) {
        this.currentRequestExpiry = currRequestExpiry;
        return this;
    }

    public HttpUtils configCookieStore(CookieStore cookieStore) {
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        return this;
    }

    public HttpUtils configUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
        return this;
    }

    public HttpUtils configTimeout(int timeout) {
        final HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configSoTimeout(int timeout) {
        final HttpParams httpParams = this.httpClient.getParams();
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configRegisterScheme(Scheme scheme) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        Scheme scheme = new Scheme("https", sslSocketFactory, 443);
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configRequestRetryCount(int count) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
        return this;
    }

    public HttpUtils configRequestThreadPoolSize(int threadPoolSize) {
        HttpUtils.EXECUTOR.setPoolSize(threadPoolSize);
        return this;
    }

    // ***************************************** send request *******************************************

    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url, RequestCallBack<T> callBack) {
        return send(method, url, null, callBack);
    }

    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url, RequestParams params,
                                   RequestCallBack<T> callBack) {
        if (url == null) throw new IllegalArgumentException("url may not be null");

        HttpRequest request = new HttpRequest(method, url);
        return sendRequest(request, params, callBack);
    }

    public ResponseStream sendSync(HttpRequest.HttpMethod method, String url) throws HttpException {
        return sendSync(method, url, null);
    }

    public ResponseStream sendSync(HttpRequest.HttpMethod method, String url, RequestParams params) throws HttpException {
        if (url == null) throw new IllegalArgumentException("url may not be null");

        HttpRequest request = new HttpRequest(method, url);
        return sendSyncRequest(request, params);
    }

    // ***************************************** download *******************************************

    public HttpHandler<File> download(String url, String target,RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, null, false, false, callback);
    }

    public HttpHandler<File> download(String url, String target,
                                      boolean autoResume, RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, null, autoResume, false, callback);
    }

    public HttpHandler<File> download(String url, String target,
                                      boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, null, autoResume, autoRename, callback);
    }

    public HttpHandler<File> download(String url, String target,
                                      RequestParams params, RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, params, false, false, callback);
    }

    public HttpHandler<File> download(String url, String target,
                                      RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, params, autoResume, false, callback);
    }

    public HttpHandler<File> download(String url, String target,
                                      RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return download(HttpRequest.HttpMethod.GET, url, target, params, autoResume, autoRename, callback);
    }

    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target,
                                      RequestParams params, RequestCallBack<File> callback) {
        return download(method, url, target, params, false, false, callback);
    }

    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target,
                                      RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return download(method, url, target, params, autoResume, false, callback);
    }

    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target,
                                      RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {

        if (url == null) throw new IllegalArgumentException("url may not be null");
        if (target == null) throw new IllegalArgumentException("target may not be null");

        HttpRequest request = new HttpRequest(method, url);

        HttpHandler<File> handler = new HttpHandler<File>(httpClient, httpContext, responseTextCharset, callback);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);

        if (params != null) {
            request.setRequestParams(params, handler);
            handler.setPriority(params.getPriority());
        }
        handler.executeOnExecutor(EXECUTOR, request, target, autoResume, autoRename);
        return handler;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private <T> HttpHandler<T> sendRequest(HttpRequest request, RequestParams params, RequestCallBack<T> callBack) {

        HttpHandler<T> handler = new HttpHandler<T>(httpClient, httpContext, responseTextCharset, callBack);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);
        request.setRequestParams(params, handler);

        if (params != null) {
            handler.setPriority(params.getPriority());
        }
        handler.executeOnExecutor(EXECUTOR, request);
        return handler;
    }

    private ResponseStream sendSyncRequest(HttpRequest request, RequestParams params) throws HttpException {

        SyncHttpHandler handler = new SyncHttpHandler(httpClient, httpContext, responseTextCharset);

        handler.setExpiry(currentRequestExpiry);
        handler.setHttpRedirectHandler(httpRedirectHandler);
        request.setRequestParams(params);

        return handler.sendRequest(request);
    }
}
