package top.jaken.mouseblog.activities.Mine.plug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;
import top.jaken.mouseblog.tools.VaildHelper;

public class MineChangePassword extends AppCompatActivity {

    private EditText oldPassword,newPassword,newPasswordRe,Code;
    private TextView Mail;
    private TextView ChangeSend;
    private Button Submit;

    /**
     * 处理handler，所有视图绑定的集中处理！
     * @param msg
     */
    private void finishHandler(Message msg) {
        switch (msg.what) {
            case 1:
//                处理邮箱绑定
                finishMail((AjaxResult)msg.obj);
                break;
            case 2:
//                处理验证码是否发送成功
                finishSendCode((AjaxResult) msg.obj);
                break;
            case 3:
//                处理修改密码
                finishSubmit((String) msg.obj);
                break;
            default:
                Log.e("Handler", "没有定义的回调");
        }
    }

    /**
     * 设置一个handler
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finishHandler(msg);
        }
    };

    private void finishSubmit(String res) {
        Map<String, Object> map = JSON.parseObject(res);
        if ("修改密码成功".equals(map.get("message"))) {
            Toast.makeText(MineChangePassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
            MineChangePassword.this.finish();
        }

    }

    /**
     * 初始化确认修改密码按钮initSubmit
     */
    private void initSubmit() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPasswordStr = oldPassword.getText().toString();
                final String newPasswordStr = newPassword.getText().toString();
                String newPasswordReStr = newPasswordRe.getText().toString();
                final String codeStr = Code.getText().toString();
                if ("".equals(codeStr)) {
                    Toast.makeText(MineChangePassword.this, "请点击发送邮箱验证码", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (!newPasswordReStr.equals(newPasswordStr)) {
                    Toast.makeText(MineChangePassword.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
//        开始请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AjaxInterface ajaxInterface = new AjaxInterface("/user/updatePassword");
                        ajaxInterface.setType(AjaxInterface.POST);
                        ajaxInterface.addToken(MineChangePassword.this.getApplication());
                        ajaxInterface.addDataItem("oldPassword", oldPasswordStr);
                        ajaxInterface.addDataItem("newPassword", newPasswordStr);
                        ajaxInterface.addDataItem("code", codeStr);
                        String res = ajaxInterface.doAjax();
                        handler.sendMessage(handler.obtainMessage(3, res));
                    }
                }).start();
            }
        });
    }

    /**
     * 发送验证按钮请求后状态回调finishChangeSend
     * @param result
     */
    private void finishSendCode(AjaxResult result) {
        if (result.JudgeCode(MineChangePassword.this.getApplicationContext())) {
            Toast.makeText(MineChangePassword.this, (String) result.getData().get("message"), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化发送验证按钮initChangeSend
     */
    private void initSendCode(){
        ChangeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mailAddress = Mail.getText().toString();
                if (!VaildHelper.isEmailCurrent(mailAddress)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AjaxInterface ajaxInterface = new AjaxInterface("/user/sendMail");
                            ajaxInterface.setType(AjaxInterface.POST);
                            ajaxInterface.addToken(MineChangePassword.this.getApplication());
                            ajaxInterface.addDataItem("mail", mailAddress);
                            AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                            handler.sendMessage(handler.obtainMessage(2, res));
                        }
                    }).start();
                }
                else{
                    Toast.makeText(MineChangePassword.this, "邮箱异常", Toast.LENGTH_SHORT).show();
                    Log.e("MineChangePassword", "邮箱异常");
                }
            }
        });
    }

    /**
     * 邮箱获取请求后finishMail
     * @param result
     */
    private void finishMail(AjaxResult result) {
        if (result.JudgeCode(MineChangePassword.this)) {
            Mail.setText((String) result.getData().get("data"));
        }
    }

    /**
     * 初始化邮箱initMail
     */
    private void initMail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface = new AjaxInterface("/user/mail");
                ajaxInterface.setType(AjaxInterface.GET);
                ajaxInterface.addToken(MineChangePassword.this.getApplication());
                AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
    }

    /**
     * 初始化绑定
     */
    private void init() {
        oldPassword = findViewById(R.id.ChangeOldPassword);
        newPassword = findViewById(R.id.ChangeNewPassword);
        newPasswordRe = findViewById(R.id.ChangeNewPasswordRe);
        Code = findViewById(R.id.ChangeCode);
        Mail = findViewById(R.id.ChangeMail);
        ChangeSend = findViewById(R.id.ChangeSend);
        Submit = findViewById(R.id.ChangeSubmit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_change_password);

        init();
        initMail();
        initSendCode();
        initSubmit();

    }
}
