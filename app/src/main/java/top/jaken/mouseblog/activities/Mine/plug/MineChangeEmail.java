package top.jaken.mouseblog.activities.Mine.plug;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;
import top.jaken.mouseblog.tools.VaildHelper;

public class MineChangeEmail extends AppCompatActivity {

    private TextView sendMailOld,sendMailNew,oldMailAddress;
    private EditText oldMailCode,newMailAddress,newMailCode;
    private Button submit;

    /**
     * handler统一处理
     * @param msg
     */
    private void doHandler(Message msg) {
        switch (msg.what) {
            case 1:
                finishOldMailAddress((AjaxResult)msg.obj);
                break;
            case 2:
                finishSendMailCode((AjaxResult)msg.obj);
                break;
            case 3:
                finishSubmit((AjaxResult) msg.obj);
            default:
                Log.e("MineChangeEmail", "handler捕捉异常");
                break;
        }
    }

    /**
     * handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            doHandler(msg);
        }
    };

    /**
     * 回调：完成提交后的操作
     * @param result
     */
    private void finishSubmit(AjaxResult result) {
        if (result.JudgeCode(MineChangeEmail.this)) {
            Toast.makeText(MineChangeEmail.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            MineChangeEmail.this.finish();
           
        }
    }

    /**
     * 初始化提交initSubmit
     */
    private void initSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String newMail = newMailAddress.getText().toString();
                        String oldMailCodeStr = oldMailCode.getText().toString();
                        String newMailCodeStr = newMailCode.getText().toString();
                        AjaxInterface ajaxInterface = new AjaxInterface("/user/updateMail");
                        ajaxInterface.setType(AjaxInterface.POST);
                        ajaxInterface.addDataItem("newMail", newMail);
                        ajaxInterface.addDataItem("oldMailCode", oldMailCodeStr);
                        ajaxInterface.addDataItem("newMailCode", newMailCodeStr);
                        ajaxInterface.addToken(MineChangeEmail.this.getApplication());
                        AjaxResult res = ajaxInterface.doAjaxWithJSON();
                        handler.sendMessage(handler.obtainMessage(3, res));
                    }
                }).start();
            }
        });
    }

    /**
     * 回调：发送邮件验证码 finishSendMailCode
     * @param result
     */
    private void finishSendMailCode(AjaxResult result) {
        if (result.JudgeCode(MineChangeEmail.this)) {
            Toast.makeText(MineChangeEmail.this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 邮箱验证码发送（两个事件通用）
     * @param mailAddress
     */
    private void sendMailCode(final String mailAddress) {
        if (!VaildHelper.isEmailCurrent(mailAddress)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AjaxInterface ajaxInterface = new AjaxInterface("/user/sendMail");
                    ajaxInterface.setType(AjaxInterface.POST);
                    ajaxInterface.addToken(MineChangeEmail.this.getApplication());
                    ajaxInterface.addDataItem("mail", mailAddress);
                    AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                    handler.sendMessage(handler.obtainMessage(2, res));
                }
            }).start();
        }
        else{
            Toast.makeText(MineChangeEmail.this, "邮箱异常", Toast.LENGTH_SHORT).show();
            Log.e("MineChangePassword", "邮箱异常");
        }
    }

    /**
     * 初始化：发送邮件验证码，两处控件
     */
    private void initSendMail() {
        sendMailOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldMail = oldMailAddress.getText().toString();
                sendMailCode(oldMail);
            }
        });
        sendMailNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMail = newMailAddress.getText().toString();
                sendMailCode(newMail);
            }
        });
    }

    /**
     * 回调：获取原邮箱的回调finishOldMailAddress
     * @param result
     */
    private void finishOldMailAddress(AjaxResult result) {
        if (result.JudgeCode(MineChangeEmail.this)) {
            oldMailAddress.setText((String) result.getData().get("data"));
        }
    }

    /**
     * 初始化原邮箱信息initOldMailAddress
     */
    private void initOldMailAddress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface = new AjaxInterface("/user/mail");
                ajaxInterface.setType(AjaxInterface.GET);
                ajaxInterface.addToken(MineChangeEmail.this.getApplication());
                AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
    }

    /**
     * 初始化与绑值
     */
    private void init() {
        sendMailNew = findViewById(R.id.ChangeMailSendNewCode);
        oldMailAddress = findViewById(R.id.ChangeMailOld);
        sendMailOld = findViewById(R.id.ChangeMailOldSend);
        oldMailCode = findViewById(R.id.ChangeMailOldCode);
        newMailAddress = findViewById(R.id.ChangeMailNewAddress);
        newMailCode = findViewById(R.id.ChangeMailNewCode);
        submit = findViewById(R.id.ChangeMailSubmit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_change_email);
        init();
        initOldMailAddress();
        initSendMail();
        initSubmit();
        
    }
}
