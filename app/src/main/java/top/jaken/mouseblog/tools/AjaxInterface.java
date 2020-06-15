package top.jaken.mouseblog.tools;

import android.app.Application;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.activities.Index.MyApplication;

/**
 * @author jaken
 * 这是用于网络请求的封装，注意此方法并没有使用
 */
public class AjaxInterface {
    /**
     * Type参数POST请求
     */
    public final static String POST = "POST";
    /**
     * Type参数GET请求
     */
    public final static String GET = "GET";
    /**
     * Type参数PUT请求
     */
    public final static String PUT = "PUT";
    /**
     * Type参数Delete请求
     */
    public final static String DELETE="DELETE";
    /**
     * 默认POST请求会自动在请求头中添加表单，此选项为不添加
     */
    public final static String POST_WITHOUT_FORM_PROPTYPE = "POSTno";

    public String getUrl() {
        return url;
    }

    /**
     * 请求URL
     */
    private final String url = "http://127.0.0.1:8001";
    /**
     * 请求的路径
     */
    private String path = null;
    /**
     * 请求的表单数据仅Post请求有效
     */
    private Map<String, String> data = null;
    /**
     * 默认请求的RequestProptypes
     */
    private Map<String, String> props = new ArrayMap<String, String>();
    /**
     * 请求的编码
     */
    private String encode = "UTF-8";
    /**
     * 默认请求类型为Post
     */
    private String Type = AjaxInterface.POST;


    /*构造方法*/
    public AjaxInterface(String path) {
        this.path = path;
    }

    public AjaxInterface(String path, String encode) {
        this.encode = encode;
        this.path = path;
    }

    /**
     * 将map转换为buffer对象
     *
     * @param params
     * @param encode
     * @return
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer buffer = new StringBuffer();
        if (params == null) return buffer;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 将输入流转换为文本
     *
     * @param inputStream
     * @return
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * 发起一次请求，注意请在请求前处理好参数
     *
     * @return 对于请求出现错误则Return null值
     */
    public String doAjax() {
        if (path == null)
            return null;

//        开始请求
        try {
            URL url = new URL(this.url + path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(this.Type);
            httpURLConnection.setUseCaches(false);//不使用缓存
//            请求类型注意写成表单类型
            for (Map.Entry<String, String> item : props.entrySet()) {
                httpURLConnection.addRequestProperty(item.getKey(), item.getValue());
            }
            //tips：注意只要打开了输出流，请求会自动变为POST，此时与上面设置无关
            if (this.Type.equals(AjaxInterface.POST)||this.Type.equals(AjaxInterface.PUT)) {
                byte[] data = getRequestData(this.data, this.encode).toString().getBytes();
                httpURLConnection.setDoOutput(true);//启动输出流
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
                httpURLConnection.connect();

                OutputStream out = httpURLConnection.getOutputStream();
                out.write(data);//写入数据
                out.flush();
                out.close();
            }

            int respones = httpURLConnection.getResponseCode();
            if (respones == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                String resString = dealResponseResult(inputStream);
                return resString;
            }
            ;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发起一次请求，这是doAjax的一次封装
     *
     * @return 成功则返回AjaxResult对象，失败返回null
     */
    public AjaxResult doAjaxWithJSON() {
        String res = doAjax();
        if (res == null) {
            Log.e("请求出现问题", this.getClass().toString());
            AjaxResult result = new AjaxResult(204, null, "网络出现错误",null);
            return result;
        }

        Map<String, Object> map = JSON.parseObject(res);
        AjaxResult result = new AjaxResult((int) map.get("code"),
                (Map<String, Object>) map.get("data"),
                (String) map.get("message"),
                this.Type);
        return result;
    }

    /**
     * 如果data域为Array则传入True参数
     * 由于发现这个开源的后端及其不负责任，在tag中有一个data的封装偷懒采用了list形式，所以我不得不对doAjaxWithJson做一次重载
     * 注意在AjaxResult中，data域中被封装成了只有一个元素也就是data的map
     * @param isDataArray
     * @return
     */
    public AjaxResult doAjaxWithJSON(boolean isDataArray) {
        if (isDataArray == true) {
            String res = doAjax();
            if (res == null) {
                Log.e("请求出现问题", this.getClass().toString());
                return null;
            }
            Map<String, Object> map = JSON.parseObject(res);
            Map<String, Object> data = new ArrayMap<>();
            data.put("data", map.get("data"));
            AjaxResult result = new AjaxResult((int) map.get("code"),
                    data,
                    (String) map.get("message"),
                    this.Type);
            return result;
        } else {
            return doAjaxWithJSON();
        }
    }

    /**
     * 在dataMap中添加项，如果data未初始化则直接返回false
     *
     * @param key
     * @param value
     * @return
     */
    public boolean addDataItem(String key, String value) {
        if (this.data == null) return false;
        try {
            this.data.put(key, value);
        } catch (Exception e) {
            Log.e("Ajax添加Post参数时出现错误(" + this.toString() + ")：", e.getMessage().toString());
            return false;
        }
        return true;
    }

    public Map<String, String> getData() {
        return data;
    }

    /**
     * 设置一个dataMap数组，此操作将直接替换原先设置的值
     * 对于Get请求及时设置了data，请求时也不会将data发送
     *
     * @param data
     */
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    /**
     * 查看当前请求是否已经生成了Data类型
     *
     * @return
     */
    public boolean isDataExist() {
        return this.data != null;
    }

    /**
     * 设置此方法需要用户登录Token
     *
     * @param application
     */
    public void addToken(Application application) {
        MyApplication app = (MyApplication) application;
        String token = (String) app.get(MyApplication.MY_TOKEN_STR);
        this.addRequestProperty("Authorization", token);
    }

    /**
     * 添加请求头，key value的形式添加
     *
     * @param key
     * @param value
     */
    public void addRequestProperty(String key, String value) {
        if (this.props == null) {
            this.props = new ArrayMap<>();
        }
        this.props.put(key, value);
    }

    /**
     * 对于Post请求使用表单的需要添加此方法，此方法默认被添加了！
     */
    public void RequestForm() {
        addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return Type;
    }

    /**
     * 设置请求类型，如果为POST则会初始化一个默认为空的data对象
     *
     * @param type "POST" or "GET"
     */
    public void setType(String type) {
        Type = type;
        if ("POST".equals(type.toUpperCase()) || "PUT".equals(type.toUpperCase())) {
            if(data==null)
            data = new ArrayMap<String, String>();
            this.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        if ("POSTno".equals(type)) {
            data = new ArrayMap<String, String>();
        }
    }

}
