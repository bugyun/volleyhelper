# volleyhelper
volley 帮助类

使用方法：

public class MainActivity extends Activity implements IHelperAction { //实现接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyConfiguration configuration = new VolleyConfiguration.Builder(this)//
                .builderCachePath()//配置缓存的保存路径
                .builderIsDebug(true)//日志是否显示
                .builderHasNoHttpConnectCache(true)//无网络情况下时候，时候使用缓存
                .builderCacheSize(5 * 1024 * 1024)//配置缓存的大小
                .builderHttpStack(new OkHttpStack(new OkHttpClient()))//配置使用okhttp
                .builderJsonConvertFactory(new GsonFactory())//配置使用gson来解析json字串
                .builderSetCertificatesFromAssets("srca.cer")//默认证书放在assets目录下,单项证书
                .builderClientKeyManagerFromAssets("srca.cer","123456")//双向认证
                .build();
        VolleyHelper.getInstance().init(configuration);
        VolleyHelper.getInstance().loadData(Request.Method.GET, this, "", HelperParams.NET_WORK_ONE);


    }
    
    /**
     * @param isSucceed  请求是否成功
     * @param response   返回的字符串
     * @param netWorkNum 访问序号
     * @param error 错误的信息
     * @param <T> 返回的泛型，json解析
     */
    @Override
    public <T> void onDataLoaded(boolean isSucceed, T response, int netWorkNum, VolleyError error) {

    }

    /**
     * @param request
     * @param netWorkNum 访问序号
     */
    @Override
    public void setLoadParams(Request request, int netWorkNum) {

    }
}


