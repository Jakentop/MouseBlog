package top.jaken.mouseblog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;
import top.jaken.mouseblog.tools.VaildHelper;

/**
 * @author jaken
 * 用户注册控制器
 */
public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private Button btnGetMailCode;
    private EditText txtName,txtPassword,txtRepPassword,txtMail,txtMailCode,txtInviterCode;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                注册
                case 1:
                    finishRegister((Bundle) msg.obj);
                    break;
//                 发送验证码
                case 2:
                    finishSendMailCode((AjaxResult) msg.obj);
                    break;
//                 Toast错误信息操作
                case 3:
                    Toast.makeText(UserRegister.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;



            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        register_item();
    }

    /**
     * 绑定当前layout
     */
    private void register_item() {
        txtInviterCode = findViewById(R.id.regInviteCode);
        txtMail = findViewById(R.id.regMail);
        txtMailCode = findViewById(R.id.regMailCode);
        txtName = findViewById(R.id.regUserName);
        txtPassword = findViewById(R.id.regPassword);
        txtRepPassword = findViewById(R.id.regRePassword);
//        事件绑定
        btnGetMailCode = findViewById(R.id.regGetMailCode);
        btnRegister = findViewById(R.id.regRegister);
        btnRegister.setOnClickListener(this);
        btnGetMailCode.setOnClickListener(this);
    }

    /**
     * 请求完成的回调方法
     * @param msg 注意传参应该是一个Bundle
     */
    private void finishRegister(Bundle msg) {
        AjaxResult res = (AjaxResult) msg.get("res");
        if (res.JudgeCode(UserRegister.this)) {
            Log.i("注册成功",res.toString());
        }

    }

    /**
     * 用户注册的请求方法
     */
    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                验证
                Map<String, String> data = new ArrayMap<>();
                data.put("name", txtName.getText().toString());
                data.put("password", txtPassword.getText().toString());
                data.put("reppassword", txtRepPassword.getText().toString());
                data.put("mail", txtName.getText().toString());
                data.put("mailCode", txtMailCode.getText().toString());
                data.put("inviteCode", txtInviterCode.getText().toString());
                if(VaildHelper.isStringMapsEmpty(data))
                {
                    handler.sendMessage(handler.obtainMessage(3, "信息填入不完整"));
                }
                else if(!data.get("password").equals(data.get("reppassword")))
                {
                    handler.sendMessage(handler.obtainMessage(3, "两次密码不一致"));
                }
                else
                {
//                    准备请求
                    AjaxInterface register = new AjaxInterface("/user/register");
                    data.remove("repassword");
                    register.setType(AjaxInterface.POST);
                    register.setData(data);
                    AjaxResult res = register.doAjaxWithJSON();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("res", res);
                    handler.sendMessage(handler.obtainMessage(1, bundle));
                }
            }
        }).start();

    }

    /**
     * 完成验证码发送的回调方法
     * @param res
     */
    private void finishSendMailCode(AjaxResult res) {
        if (res.JudgeCode(UserRegister.this)) {
            Toast.makeText(UserRegister.this, "验证码已发送，五分钟有效", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 获取邮箱码
     */
    private void getMailCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Mail = txtMail.getText().toString();
                Log.i("获取的邮箱", Mail);
                if (VaildHelper.isEmailCurrent(Mail)) {
                    handler.sendMessage(handler.obtainMessage(3,"邮箱无效"));
                }
                else
                {
                    AjaxInterface sendCode = new AjaxInterface("/user/sendMail");
                    sendCode.setType(AjaxInterface.POST);
                    sendCode.addDataItem("mail", Mail);
                    AjaxResult res = sendCode.doAjaxWithJSON();
                    handler.sendMessage(handler.obtainMessage(2, res));
                }
            }
        }).start();
    }

    /**
     * 类的默认按钮事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regRegister:
                Log.i("事件", "触发了注册事件");
                register();
                break;
            case R.id.regGetMailCode:
                Log.i("事件", "触发了发送验证码事件");
                getMailCode();
                break;
        }
    }
}
