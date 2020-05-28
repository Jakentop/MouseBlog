package top.jaken.mouseblog.tools;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jaken
 * 一些验证的静态方法
 */
public abstract class VaildHelper {

    /**
     * 字符串是否为空
     * @param string
     * @return 为空则返回true 不为空返回false
     */
    public static boolean isStringEmpty(String string) {
        return "".equals(string);
    }

    /**
     * 传入一个字符串map，判断是否存在空项，存在则为true，不存在未false
     * @param map
     * @return
     */
    public static boolean isStringMapsEmpty(Map<String, String> map) {
        for (Map.Entry<String,String> t : map.entrySet()) {
            if(isStringEmpty(t.getKey())||isStringEmpty(t.getValue())) return true;
        }
        return false;
    }

    /**
     * 验证邮箱有效性，有效为False，无效为True
     * @param eMail
     * @return
     */
    public static boolean isEmailCurrent(String eMail) {
        String regEx = "[a-zA-Z_]{0,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(eMail);
        return !matcher.matches();
    }
}
