package com.android.volley.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.json.GsonFactory;
import com.android.volley.json.JsonConvertFactory;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ruoyun on 2015/10/15.
 */
public class VolleyConfiguration {

    private static final String DEFAULT_CACHE_DIR = "volley";
    public final Context context;
    public final String cachePath;
    public final int cacheSize;
    public static boolean isDebug;
    public static boolean hasNoHttpConnectCache;
    public final HttpStack httpStack;
    public final JsonConvertFactory jsonConvertFactory;
//    public final SSLContext sslContext;//http urlconnection 的SSLSocketFactory类是不同的。


    public VolleyConfiguration(Builder builder) {
        this.context = builder.context;
        this.cachePath = builder.cachePath;
        this.isDebug = builder.isDebug;
        this.hasNoHttpConnectCache = builder.hasNoHttpConnectCache;
        this.httpStack = builder.httpStack;
        this.jsonConvertFactory = builder.jsonConvertFactory;
//        this.sslContext = builder.sslContext;
        this.cacheSize = builder.cacheSize;
    }


    public static class Builder {
        private Context context;
        private String cachePath;
        private int cacheSize;
        private boolean isDebug;
        private boolean hasNoHttpConnectCache;
        private HttpStack httpStack;
        private JsonConvertFactory jsonConvertFactory;
        //        private SSLContext sslContext;//http urlconnection 的SSLSocketFactory类是不同的。
        //证书，可以是多个证书 jks 单向验证
        private List certificatesList = new ArrayList();
        //private InputStream[] certificates;
        //private String[] certificatesPath;

        //双向验证 bks
        private InputStream bksFile;
        private String bksPassword;


        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.cachePath = null;
            this.isDebug = false;
            this.hasNoHttpConnectCache = false;
            this.httpStack = null;
            this.jsonConvertFactory = null;
            this.bksFile = null;
            this.bksPassword = null;
            this.cacheSize = DefaultConfigurationFactory.DEFAULT_DISK_USAGE_BYTES;
        }


        public Builder builderCachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public Builder builderCacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder builderIsDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;

        }

        public Builder builderHasNoHttpConnectCache(boolean isHas) {
            this.hasNoHttpConnectCache = isHas;
            return this;
        }

        public Builder builderHttpStack(HttpStack httpStack) {
            this.httpStack = httpStack;
            return this;
        }

        public Builder builderJsonConvertFactory(JsonConvertFactory jsonConvertFactory) {
            this.jsonConvertFactory = jsonConvertFactory;
            return this;
        }

        public Builder builderSetCertificates(InputStream... certificates) {//单项验证
            certificatesList.addAll(Arrays.asList(certificates));
            return this;
        }

        public Builder builderSetCertificatesFromAssets(String... certificatesPath) {
            for (String path : certificatesPath) {
                try {
                    certificatesList.add(context.getAssets().open(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public Builder builderClientKeyManagerFromAssets(String bksFilePath, String bksPassword) {
            try {
                this.bksFile = context.getAssets().open(bksFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.bksPassword = bksPassword;
            return this;
        }

        public VolleyConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new VolleyConfiguration(this);
        }


        private void initEmptyFieldsWithDefaultValues() {
            if (TextUtils.isEmpty(cachePath)) {
                this.cachePath = DefaultConfigurationFactory.createDiskCache(context, VolleyConfiguration.DEFAULT_CACHE_DIR);
            }
            if (httpStack == null) {
                String userAgent = "volley/0";
                try {
                    String packageName = context.getPackageName();
                    PackageInfo info = context.getPackageManager().getPackageInfo(
                            packageName, 0);
                    userAgent = packageName + "/" + info.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                }
                if (Build.VERSION.SDK_INT >= 9) {
                    httpStack = new HurlStack();

                } else {
                    // Prior to Gingerbread, HttpUrlConnection was unreliable.
                    // See:
                    // http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                    httpStack = new HttpClientStack(
                            AndroidHttpClient.newInstance(userAgent), DefaultConfigurationFactory.setHttpCertificates(bksFile, bksPassword));//没有添加https验证
                }
            }
            if (certificatesList.size() > 0 || bksFile != null) {
                httpStack.setSslSocketFactory(DefaultConfigurationFactory.setCertificates((InputStream[]) certificatesList.toArray(), bksFile, bksPassword).getSocketFactory());
            }

            //默认使用gson 解析
            if (jsonConvertFactory == null) {
                jsonConvertFactory = new GsonFactory();
            }
        }
    }
}
