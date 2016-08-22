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
 * 重试策略接口
 * Retry policy for a request.
 */
public interface RetryPolicy {

    /**
     * 获取当前请求用时（用于 Log）
     * Returns the current timeout (used for logging).
     */
    public int getCurrentTimeout();

    /**
     * 获取已经重试的次数（用于 Log）
     * Returns the current retry count (used for logging).
     */
    public int getCurrentRetryCount();

    /**
     * 确定是否重试，参数为这次异常的具体信息。在请求异常时此接口会被调用，可在此函数实现中抛出传入的异常表示停止重试。
     * Prepares for the next retry by applying a backoff to the timeout.
     *
     * @param error The error code of the last attempt.
     * @throws VolleyError In the event that the retry could not be performed (for example if we
     *                     ran out of attempts), the passed in error is thrown.
     */
    public void retry(VolleyError error) throws VolleyError;
}
