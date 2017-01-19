/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.protocol.HTTP;

import com.k2.mobile.app.model.http.RequestParams;
import com.k2.mobile.app.model.http.callback.RequestCallBackHandler;
import com.k2.mobile.app.model.http.client.entity.UploadEntity;
import com.k2.mobile.app.model.http.client.util.URIBuilder;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.OtherUtil;

/**
 * @ClassName: HttpRequest
 * @Description: HTTP请求工具类
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
public class HttpRequest extends HttpRequestBase implements HttpEntityEnclosingRequest {

    private HttpEntity entity;

    private HttpMethod method;

    private URIBuilder uriBuilder;

    private Charset uriCharset;

    public HttpRequest(HttpMethod method) {
        super();
        this.method = method;
    }

    public HttpRequest(HttpMethod method, String uri) {
        super();
        this.method = method;
        setURI(uri);
    }

    public HttpRequest(HttpMethod method, URI uri) {
        super();
        this.method = method;
        setURI(uri);
    }

    public HttpRequest addQueryStringParameter(String name, String value) {
        uriBuilder.addParameter(name, value);
        return this;
    }

    public HttpRequest addQueryStringParameter(NameValuePair nameValuePair) {
        uriBuilder.addParameter(nameValuePair.getName(), nameValuePair.getValue());
        return this;
    }

    public HttpRequest addQueryStringParams(List<NameValuePair> nameValuePairs) {
        if (nameValuePairs != null) {
            for (NameValuePair nameValuePair : nameValuePairs) {
                uriBuilder.addParameter(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        return this;
    }

    public void setRequestParams(RequestParams param) {
        if (param != null) {
            if (uriCharset == null) {
                uriCharset = Charset.forName(param.getCharset());
            }
            List<RequestParams.HeaderItem> headerItems = param.getHeaders();
            if (headerItems != null) {
                for (RequestParams.HeaderItem headerItem : headerItems) {
                    if (headerItem.overwrite) {
                        this.setHeader(headerItem.header);
                    } else {
                        this.addHeader(headerItem.header);
                    }
                }
            }
            this.addQueryStringParams(param.getQueryStringParams());
            this.setEntity(param.getEntity());
        }
    }

    public void setRequestParams(RequestParams param, RequestCallBackHandler callBackHandler) {
        if (param != null) {
            if (uriCharset == null) {
                uriCharset = Charset.forName(param.getCharset());
            }
            List<RequestParams.HeaderItem> headerItems = param.getHeaders();
            if (headerItems != null) {
                for (RequestParams.HeaderItem headerItem : headerItems) {
                    if (headerItem.overwrite) {
                        this.setHeader(headerItem.header);
                    } else {
                        this.addHeader(headerItem.header);
                    }
                }
            }
            this.addQueryStringParams(param.getQueryStringParams());
            HttpEntity entity = param.getEntity();
            if (entity != null) {
                if (entity instanceof UploadEntity) {
                    ((UploadEntity) entity).setCallBackHandler(callBackHandler);
                }
                this.setEntity(entity);
            }
        }
    }

    @Override
    public URI getURI() {
        try {
            if (uriCharset == null) {
                uriCharset = OtherUtil.getCharsetFromHttpRequest(this);
            }
            if (uriCharset == null) {
                uriCharset = Charset.forName(HTTP.UTF_8);
            }
            return uriBuilder.build(uriCharset);
        } catch (URISyntaxException e) {
            LogUtil.e(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void setURI(URI uri) {
        this.uriBuilder = new URIBuilder(uri);
    }

    public void setURI(String uri) {
        this.uriBuilder = new URIBuilder(uri);
    }

    @Override
    public String getMethod() {
        return this.method.toString();
    }

    @Override
    public HttpEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean expectContinue() {
        Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        HttpRequest clone = (HttpRequest) super.clone();
        if (this.entity != null) {
            clone.entity = (HttpEntity) CloneUtils.clone(this.entity);
        }
        return clone;
    }

    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        HEAD("HEAD"),
        MOVE("MOVE"),
        COPY("COPY"),
        DELETE("DELETE"),
        OPTIONS("OPTIONS"),
        TRACE("TRACE"),
        CONNECT("CONNECT");

        private final String value;

        HttpMethod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
