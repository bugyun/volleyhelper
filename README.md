# Volleyhelper
volley帮助类

## 什么是volley
* 之前，我们在程序中需要网络通信的时候，大体使用的东西莫过于AsyncTaskLoader、HttpURLConnection、AsyncTask、HTTPClient（Apache）等。
* Google I/O 2013上，Volley发布了，Volley是Android平台上的网络通信库，能使网络通信更快，更简单，更健壮。
* Volley名称的由来： a burst or emission of many things or a large amount at once

## 什么是Volleyhelper

基于2015年10月31日最新volley代码，它是一个通过配置各种参数，来很方便的实现网络方面的业务逻辑，简化开发者代码。Volley特别适合数据量不大但是通信频繁的场景，所以代码中没有增加上传和下载的模块。


## 使用方法

##### 1.初始化
使用默认值
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyHelper.getInstance().init(this);// 初始化volleyhelper,使用默认值
    }
}
```
配置参数
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
          VolleyConfiguration configuration = new VolleyConfiguration.Builder(this)//
                 //.builderCachePath()//配置缓存地址，默认sd/appname/cache/volley
                .builderIsDebug(true)//是否打印日志，默认不打印
                .builderHasNoHttpConnectCache(true)//没有网络情况下，是否使用缓存
                .builderCacheSize(5 * 1024 * 1024)//缓存大小
                .builderHttpStack(new OkHttpStack(new OkHttpClient()))//使用okhttp,默认httpclient和httpurlconnetion.
                .builderJsonConvertFactory(new GsonFactory())//json解析方式，默认gosn解析
                .builderSetCertificatesFromAssets("srca.cer")//https,默认证书放在assets目录下,单项证书
                .builderClientKeyManagerFromAssets("srca.cer", "123456")//https双向证书
                .build();
        VolleyHelper.getInstance().init(configuration);
    }
}
```
##### 2.activity中使用方法
```
public class MainActivity extends Activity implements VolleyHelper.IHelperAction {
    private String url = "www.baibu.com";//不管是post还是get,不用拼接url,不要？&后面
    //错误列子：www.baidu.com/baidu?word=markdown&tn=98012088_3_dg&ch=2&ie=utf-8
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认使用String类型
        VolleyHelper.getInstance().loadData(Request.Method.GET, this, url, HelperParams.NET_WORK_ONE);
        //使用json解析，这里ApplicationInfo.class,是一个JavaBean，列子
        VolleyHelper.getInstance().loadData(Request.Method.POST, this, url, ApplicationInfo.class, HelperParams.NET_WORK_TWO);
    }

    /**设置请求参数，不管是get还是post请求，都在这里设置请求参数
     * @param request 
     * @param netWorkNum 访问序号
     */
    @Override
    public void setLoadParams(Request request, int netWorkNum) {
        switch (netWorkNum){
            case HelperParams.NET_WORK_ONE:
                request.setParams("", "");//设置请求参数
                request.setHeaders("", "");//设置请求头信息
                request.setRetryPolicy(new DefaultRetryPolicy(10000,//设置请求超时时间，等等
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                break;
            case HelperParams.NET_WORK_TWO:
                request.setParams("", "");
                request.setHeaders("", "");
                break;
        }
    }
    
    /**
     * @param isSucceed  请求是否成功
     * @param response   返回的字符串
     * @param netWorkNum 访问序号
     * @param error      错误的信息
     * @param <T>        返回的泛型，json解析
     */
    @Override
    public <T> void onDataLoaded(boolean isSucceed, T response, int netWorkNum, VolleyError error) {
        if (!isSucceed) {//请求失败
            Toast.makeText(this, "请检查手机网络~~" + error.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        switch (netWorkNum) {
            case HelperParams.NET_WORK_ONE:
                String string = (String) response;
                //逻辑
                break;
            case HelperParams.NET_WORK_TWO:
                ApplicationInfo info = (ApplicationInfo) response;
                //逻辑
                break;
        }

    }
}
```

## 有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* QQ: 76428796(暂时只用QQ)


## 感激
感谢以下的项目,排名不分先后

* [volley](https://android.googlesource.com/platform/frameworks/volley) (有墙)

## 相关资料

* [volley官方介绍视频](http://www.youtube.com/watch?v=yhv8l9F44qo&feature=player_embedded)(有墙)
* [volley源码](https://android.googlesource.com/platform/frameworks/volley) (有墙)


## 关于作者

```
String name = "张耀煌",
  
```
