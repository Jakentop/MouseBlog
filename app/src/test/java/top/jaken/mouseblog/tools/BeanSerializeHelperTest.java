package top.jaken.mouseblog.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BeanSerializeHelperTest {

    private String resource;
    private AjaxResHandler handler;

    @Before
    public void setUp() throws Exception {
        resource="{\"code\":200,\"message\":\"查询成功\",\"data\":{\"total\":4,\"rows\":[{\"id\":15,\"title\":\"hello world\",\"body\":\"# hello this is my first test\\nnihao\\n\\n![timg.jpg](/img/2124f55c6a1746febb789ada2f4d9d80.jpg)\",\"discussCount\":0,\"blogViews\":1,\"time\":1590217750000,\"state\":1,\"user\":{\"id\":1,\"name\":\"admin\",\"reward\":\"null\"},\"tags\":[{\"id\":13,\"name\":\"test\"}]},{\"id\":14,\"title\":\"this is le\",\"body\":\"hello this is my first blog\\n![timg.jpg](1)\",\"discussCount\":0,\"blogViews\":1,\"time\":1590217052000,\"state\":1,\"user\":{\"id\":1,\"name\":\"admin\",\"reward\":\"null\"},\"tags\":[{\"id\":13,\"name\":\"test\"}]},{\"id\":13,\"title\":\"hello2\",\"body\":\"# helo\\n## do\",\"discussCount\":0,\"blogViews\":0,\"time\":1590053284000,\"state\":1,\"user\":{\"id\":4,\"name\":\"zyz\"},\"tags\":[{\"id\":14,\"name\":\"test2\"}]},{\"id\":12,\"title\":\"hello\",\"body\":\"hello\",\"discussCount\":3,\"blogViews\":2,\"time\":1590050713000,\"state\":1,\"user\":{\"id\":1,\"name\":\"admin\",\"reward\":\"null\"},\"tags\":[{\"id\":13,\"name\":\"test\"}]}]}}";
        handler=new AjaxResHandler() {
            @Override
            public String getListString(String dataString) {
                Map<String, String> map = JSON.parseObject(dataString,new TypeReference<HashMap<String, String>>(){});
                return map.get("rows");
            }
        };


    }

    @Test
    public void jsonStringToBean() {
    }

    @Test
    public void ajaxJsonStringToBean() {
    }

    @Test
    public void ajaxJsonStringToBeans() throws Exception {

    }
}