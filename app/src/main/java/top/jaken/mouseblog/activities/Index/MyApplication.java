package top.jaken.mouseblog.activities.Index;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.tools.VaildHelper;

/**
 * @author jaken
 * 这是重写了安卓全局上下文，我们在这里封装了一个数据访问器，用户扩展全局变量使用
 * 我们不允许在程序运行过程中动态的添加或删除字段，因此你需要在下述的数据域中写入你需要的字段名称，用于访问
 * 如果您需要动态添加一些临时数据，我们推荐使用Bundle在Activity生命周期中传递
 * 如果您实在需要在App生命周期中动态添加数据，请使用二次绑定的方式
 * 请注意命名格式，如果格式错误可能无法注册到map中
 * 命名以MY_开始
 */
public class MyApplication extends Application {

    /**
     * 返回用户的登录态
     */
    public static final String MY_TOKEN_STR = "token";
    /**
     * 当前用户的权限，分为三种：
     * 未登录：none
     * 普通用户：user
     * 管理员：admin
     */
    public static final String MY_USERE_TYPE_STR="user_type";

//    此处以上，写入需要在App生命周期中使用的变量值
    private List<String> list;

    private HashMap<String, Object> map;

    /**
     * 获取注册的对象，如果key不存在不会创建，只会返回null值
     * @param key
     * @return
     */
    public Object get(String key) {
        if (list.indexOf(key) >= 0) {
            return map.get(key);
        }
        else return null;
    }

    /**
     * 更新一个键值对，如果键不存在不会创建，并返回null值
     * @param key
     * @param value
     * @return
     */
    public Object set(String key, Object value) {
        if (list.indexOf(key) >= 0) {
            map.remove(key);
            map.put(key, value);
            return value;
        }
        else return null;
    }

    @Override
    public void onCreate() {
        map = new HashMap<String, Object>();
        String err="";
//        反射机制
        Field[] data = this.getClass().getFields();
        for (Field d : data) {
            if (VaildHelper.isCurrentAppMapKeyName(d.getName())) {
                try {
                    map.put((String) d.get(this),null);
                } catch (IllegalAccessException e) {
                    err+=d.getName()+";";
                }
            }
        }
        //打印当前注册的类
        list = new ArrayList<>();
        for (Map.Entry<String,Object> e : map.entrySet()) {
            list.add(e.getKey());
        }
        Log.e("注册上下文发生的错误对象", err);
        Log.i("当前上下文", list.toString());
        super.onCreate();
    }
}
