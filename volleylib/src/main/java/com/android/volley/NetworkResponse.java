/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Network中方法 performRequest 的返回值，Request的 parseNetworkResponse(…) 方法入参，是 Volley 中用于内部 Response 转换的一级。
 * 封装了网络请求响应的 StatusCode，Headers 和 Body 等。
 * Data and headers returned from {@link Network#performRequest(Request)}.
 */
public class NetworkResponse {
    /**
     * Creates a new network response.
     *
     * @param statusCode    the HTTP status code
     * @param data          Response body
     * @param headers       Headers returned with this response, or null for none
     * @param notModified   True if the server returned a 304 and the data was already in cache
     * @param networkTimeMs Round-trip network time to receive network response
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                           boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                           boolean notModified) {
        this(statusCode, data, headers, notModified, 0);
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false, 0);
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false, 0);
    }

    /**
     * Http 响应状态码
     * The HTTP status code.
     */
    public final int statusCode;

    /**
     * Body 数据
     * Raw data from this response.
     */
    public final byte[] data;

    /**
     * 响应 Headers
     * Response headers.
     */
    public final Map<String, String> headers;

    /**
     * 表示是否为 304 响应
     * True if the server returned a 304 (Not Modified).
     */
    public final boolean notModified;

    /**
     * 请求耗时
     * Network roundtrip time in milliseconds.
     */
    public final long networkTimeMs;
}

