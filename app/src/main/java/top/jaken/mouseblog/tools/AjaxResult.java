package top.jaken.mouseblog.tools;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Map;

import top.jaken.mouseblog.activities.UserLogin;

/**
 * @author jaken
 * 标准的请求结果类，该类包含标准请求的大多数结果，包括请求的信息
 */
public class AjaxResult implements Serializable {
    private int code;
    private Map<String,Object> data;
    private String type;
    private String message;

    @Override
    public String toString() {
        return "AjaxResult{" +
                "code=" + code +
                ", data=" + data +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public AjaxResult(int code, Map<String, Object> data, String type) {
        this.code = code;
        this.data = data;
        this.type = type;
    }

    public AjaxResult(int code, Map<String, Object> data, String message, String type) {
        this.data = data;
        this.code = code;
        this.type = type;
        this.message = message;
    }

    /**
     * 判断返回码是否正确，请注意code应该为int类型否则直接报错，成功返回true，失败则显示Toast
     * @param context
     * @return
     */
    public boolean JudgeCode(Context context,Boolean doAuth) {
        if (AjaxResponseCode.OK==(int)code) {
            return true;
        }
        else
        {
            switch ((int)code) {
                case AjaxResponseCode.ERROR:
                    Toast.makeText(context, "请求失败:"+this.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.LOGINERROR:
                    Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.TOKENEXPIREE:
                    Toast.makeText(context, "Token过期请尝试重新登录", Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.ACCESSERROR:
                    Toast.makeText(context, "权限不足", Toast.LENGTH_SHORT).show();
//                    权限不足自动跳转到登录界面
                    Intent intent = new Intent(context, UserLogin.class);
                    context.startActivity(intent);
                    break;
                case AjaxResponseCode.REMOTEERROR:
                    Toast.makeText(context, "远程调用失败", Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.REPERROR:
                    Toast.makeText(context, "重复操作", Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.SERVICEERROR:
                    Toast.makeText(context, "业务层出现错误", Toast.LENGTH_SHORT).show();
                    break;
                case AjaxResponseCode.NOTFOUND:
                    Toast.makeText(context, "资源不存在", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }

    public boolean JudgeCode(Context context) {
        return JudgeCode(context, true);
    }

    /**
     * 判断返回码类型，如果不成功则返回false，成功则返回true
     * @return
     */
    public boolean JudgeCode() {
        if (AjaxResponseCode.OK==(int)code) {
            return true;
        }
        else
            return false;
    }

    public int getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
