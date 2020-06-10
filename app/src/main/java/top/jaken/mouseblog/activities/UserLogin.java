package top.jaken.mouseblog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.Index;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

/**
 * @author jaken
 * 用户登录视图
 * undo 登录成功后的跳转
 */
public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText UserNameText;
    private EditText PasswordText;
    private Button Loginbtn;
    private Button Registerbtn;
    private TextView TipsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
//        绑定控件
        UserNameText = findViewById(R.id.regUserName);
        PasswordText = findViewById(R.id.Password);
        Loginbtn = findViewById(R.id.Login);
        Registerbtn = findViewById(R.id.Register);
        TipsText = findViewById(R.id.Tips);
        Loginbtn.setOnClickListener(this);
        Registerbtn.setOnClickListener(this);
//        初始化tokenApi
        Context context=getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.application_token), null);
        editor.putString(getString(R.string.application_user_type), null);
        editor.putString(getString(R.string.application_user_name), null);
        MyApplication app = (MyApplication) this.getApplication();
        app.set(MyApplication.MY_TOKEN_STR, null);
        app.set(MyApplication.MY_USER_NAMEE_STR, null);
        app.set(MyApplication.MY_USERE_TYPE_STR, null);

    }

    /**
     * 用户注册的回调
     */
    @SuppressLint("HandlerLeak")
    private Handler loginHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = (Bundle) msg.obj;
            AjaxResult res = (AjaxResult) bundle.get("res");
            if (res.JudgeCode(UserLogin.this)) {
                String token = (String) (res.getData().get("token"));
                String type = ((List<String>) (res.getData().get("roles"))).get(0).toLowerCase();
                String name = (String) (res.getData().get("name"));

                SharedPreferences sharedPreferences = UserLogin.
                        this.
                        getApplicationContext().
                        getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.application_token), token);
                editor.putString(getString(R.string.application_user_name), name);
                editor.putString(getString(R.string.application_user_type), type);
                editor.commit();
                MyApplication app = (MyApplication) UserLogin.this.getApplication();
                app.set(MyApplication.MY_TOKEN_STR, token);
                app.set(MyApplication.MY_USERE_TYPE_STR, type);
                app.set(MyApplication.MY_USER_NAMEE_STR, name);
                Log.i("登录成功：", res.getData().get("name").toString());
                Intent intent = new Intent(UserLogin.this, Index.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //登录成功跳转
            }
        }
    };

    /**
     * 用户登录方法
     */
    public void Login() {
        String UserName = UserNameText.getText().toString();
        String Password = PasswordText.getText().toString();
        if ("".equals(UserName) || "".equals(Password)) {
            TipsText.setText("用户名或密码不能为空");
            return ;
        }
        AjaxInterface loginAjax = new AjaxInterface("/user/login");
        loginAjax.setType(AjaxInterface.POST);
        loginAjax.addDataItem("name", UserName);
        loginAjax.addDataItem("password", Password);
        Bundle bundle = new Bundle();
//        将请求的信息结果交给handler处理
        bundle.putSerializable("res", loginAjax.doAjaxWithJSON());
        loginHandler.sendMessage(loginHandler.obtainMessage(1,bundle));

    }

    /**
     * 注册按钮的实现
     */
    public void Register(){
        Intent intent = new Intent(this, UserRegister.class);
        startActivity(intent);
    }

    /**
     * 类的默认按钮事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Login();
                    }
                }).start();
                break;
            case R.id.Register:
                this.Register();
                break;
        }
    }

}
