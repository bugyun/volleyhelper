package com.android.volley.control;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by ruoyun on 2015/10/16.
 */
public class JsonConvertRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;

    private Class<T> mClass;

    public JsonConvertRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                              Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
    }

    public JsonConvertRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                              Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonConvertFactory.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
