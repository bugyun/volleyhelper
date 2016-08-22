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

/**
 * 请求结果的传输接口，用于传递请求结果或者请求错误。
 */
public interface ResponseDelivery {
    /**
     * 此方法用于传递请求结果，request 和 response 参数分别表示请求信息和返回结果信息。
     * Parses a response from the network or cache and delivers it.
     */
    public void postResponse(Request<?> request, Response<?> response);

    /**
     * 此方法用于传递请求结果，并在完成传递后执行 Runnable。
     * Parses a response from the network or cache and delivers it. The provided
     * Runnable will be executed after delivery.
     */
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable);

    /**
     * 此方法用于传输请求错误。
     * Posts an error for the given request.
     */
    public void postError(Request<?> request, VolleyError error);
}
