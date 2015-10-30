package com.android.volley.json;

/**
 * Created by ruoyun on 2015/10/15.
 */
public interface JsonConvertFactory {

    public abstract <T> T fromJson(String json, Class<T> clzz) throws Exception;

    public abstract <T> String toJson(T bean);
}
