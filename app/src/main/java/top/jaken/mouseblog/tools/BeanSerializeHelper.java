package top.jaken.mouseblog.tools;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanSerializeHelper {

    /**
     * 将json文本转换为bean的格式，通过fastjson
     * @param dataString
     * @return
     */
    public static Object jsonStringToBean(String dataString,Class<?> t)
    {
        Object res= JSON.parseObject(dataString,t);
        return res;
    }

    /**
     * 根据标准的后端格式请求后的序列化，注意请求错误会抛出错误异常
     * @param dataString
     * @param t
     * @return
     * @throws Exception 请求错误的信息
     */
    public static Object ajaxJsonStringToBean(String dataString,Class<?>t) throws Exception
    {
        Map<String,String> res=JSON.parseObject(dataString, HashMap.class);
        if ("200".equals(res.get("code"))) {
//            请求成功
            Object r=JSON.parseObject(res.get("data"),t);
            return r;
        }
        else{
            Exception e = new Exception("请求错误,错误码："+res.get("code")+",错误信息："+res.get("message"));
            throw e;
        }
    }

    /**
     * 序列化一个ajax请求的数组，注意需要实现AjaxResHandler的Bean才可以
     * @param dataString
     * @param t
     * @param handler
     * @return
     * @throws Exception
     */
    public static List<?> ajaxJsonStringToBeans(String dataString,Class<?>t,AjaxResHandler handler) throws Exception
    {
        Map<String,String> res=JSON.parseObject(dataString, HashMap.class);
        if ("200".equals(res.get("code"))) {
//            请求成功
            String formatString = handler.getListString(res.get("data"));
            return JSON.parseArray(formatString, t);
        }
        else{
            Exception e = new Exception("请求错误,错误码："+res.get("code")+",错误信息："+res.get("message"));
            throw e;
        }
    }

}
