/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.SystemClock;

import com.k2.mobile.app.utils.LogUtil;

/**
 * @ClassName: RetryHandler
 * @Description: HTTP请求重试处理类
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
public class RetryHandler implements HttpRequestRetryHandler {

    private static final int RETRY_SLEEP_INTERVAL = 500;

    private static HashSet<Class<?>> exceptionWhiteList = new HashSet<Class<?>>();

    private static HashSet<Class<?>> exceptionBlackList = new HashSet<Class<?>>();

    static {
        exceptionWhiteList.add(NoHttpResponseException.class);
        exceptionWhiteList.add(UnknownHostException.class);
        exceptionWhiteList.add(SocketException.class);

        exceptionBlackList.add(InterruptedIOException.class);
        exceptionBlackList.add(SSLHandshakeException.class);
    }

    private final int maxRetries;

    public RetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean retryRequest(IOException exception, int retriedTimes, HttpContext context) {
        boolean retry = true;

        if (exception == null || context == null) {
            return false;
        }

        Object isReqSent = context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
        boolean sent = isReqSent == null ? false : (Boolean) isReqSent;

        if (retriedTimes > maxRetries) {
            retry = false;
        } else if (exceptionBlackList.contains(exception.getClass())) {
            retry = false;
        } else if (exceptionWhiteList.contains(exception.getClass())) {
            retry = true;
        } else if (!sent) {
            retry = true;
        }

        if (retry) {
            try {
                Object currRequest = context.getAttribute(ExecutionContext.HTTP_REQUEST);
                if (currRequest != null) {
                    if (currRequest instanceof HttpRequestBase) {
                        HttpRequestBase requestBase = (HttpRequestBase) currRequest;
                        retry = "GET".equals(requestBase.getMethod());
                    } else if (currRequest instanceof RequestWrapper) {
                        RequestWrapper requestWrapper = (RequestWrapper) currRequest;
                        retry = "GET".equals(requestWrapper.getMethod());
                    }
                } else {
                    retry = false;
                    LogUtil.e("retry error, curr request is null");
                }
            } catch (Throwable e) {
                retry = false;
                LogUtil.e("retry error", e);
            }
        }

        if (retry) {
            SystemClock.sleep(RETRY_SLEEP_INTERVAL); // sleep a while and retry http request again.
        }

        return retry;
    }

}