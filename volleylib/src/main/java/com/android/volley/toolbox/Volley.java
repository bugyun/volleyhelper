/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;

import java.io.File;

public class Volley {

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack   An {@link HttpStack} to use for the network, or null for
     *                default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        File cacheDir = getDiskCacheDir(context, DEFAULT_CACHE_DIR);
        VolleyLog.d("cacheDir is ：" + cacheDir.getAbsolutePath().toString());
        // UserAgent用来封装应用的包名跟版本号，放在请求的头部，提供给服务器
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }
        // 一般我们调用另外一个构造函数，这个参数都传null，volley会判断sdk版本选择HttpClient还是HttpURLConnection。
        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See:
                // http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(
                        AndroidHttpClient.newInstance(userAgent));
            }
        }
        // 创建一个Network对象，network里面会调用stack去跟网络通信
        Network network = new BasicNetwork(stack);
        // 创建RequestQueue，并将缓存实现DiskBasedCache和网络实现BasicNetwork传进去
        // cache路径：/data/data/<pakge name>/cache/<name>
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir),
                network);
        queue.start();

        return queue;
    }


    public static RequestQueue newRequestQueue(HttpStack httpStack, String cacheDir, int cacheSize) {
        Network network = new BasicNetwork(httpStack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(new File(cacheDir), cacheSize),
                network);
        queue.start();
        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
                /** /sdcard/Android/data/<application package>/cache */
            } else {
                cachePath = context.getCacheDir().getPath();
                /** /data/data/<application package>/cache */
            }
        } catch (Exception e) {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
