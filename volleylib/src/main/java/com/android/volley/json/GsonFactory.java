package com.android.volley.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Gson类库的封装工具类，专门负责解析json数据</br>
 * 内部实现了Gson对象的单例
 * Created by ruoyun on 2015/8/19.
 */
public class GsonFactory implements JsonConvertFactory {

    private Gson gson;

    public GsonFactory() {
        gson = new Gson();
    }


    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    @Override
    public <T> String toJson(T ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }


    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    @Override
    public <T> T fromJson(String jsonStr, Class<T> cl) throws JsonSyntaxException {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(jsonStr, cl);
        }
        return t;
    }


}
