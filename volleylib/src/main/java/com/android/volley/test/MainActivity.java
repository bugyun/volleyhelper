package com.android.volley.test;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.control.HelperParams;
import com.android.volley.control.IHelperAction;
import com.android.volley.control.OkHttpStack;
import com.android.volley.control.VolleyHelper;
import com.android.volley.core.VolleyConfiguration;
import com.android.volley.json.GsonFactory;
import com.squareup.okhttp.OkHttpClient;

public class MainActivity extends Activity implements IHelperAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyConfiguration configuration = new VolleyConfiguration.Builder(this)//
                .builderIsDebug(true)//
                .builderHasNoHttpConnectCache(true)//
                .builderHttpStack(new OkHttpStack(new OkHttpClient()))//
                .builderJsonConvertFactory(new GsonFactory())
                .builderSetCertificatesFromAssets("srca.cer")//默认证书放在assets目录下,单项证书
                .builderClientKeyManagerFromAssets("srca.cer","123456")
                .build();
        VolleyHelper.getInstance().init(configuration);
        VolleyHelper.getInstance().loadData(Request.Method.GET, this, "", HelperParams.NET_WORK_ONE);


    }


    @Override
    public <T> void onDataLoaded(boolean isSucceed, T response, int netWorkNum, VolleyError error) {

    }

    @Override
    public void setLoadParams(Request request, int netWorkNum) {

    }
}
