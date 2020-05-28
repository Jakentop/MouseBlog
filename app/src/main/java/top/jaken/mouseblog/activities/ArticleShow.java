package top.jaken.mouseblog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import br.tiagohm.markdownview.MarkdownView;
import top.jaken.mouseblog.R;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class ArticleShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_show);
        MarkdownView markdownView = findViewById(R.id.arcBody);
        //初始化
        init(this.getIntent().getExtras());
        //更新文章视图
        updateArcitle();
    }

    /**
     * 类成员变量
     */
    private MarkdownView mdBody;
    private TextView txtTitle,txtTalk,txtWatch,txtUser,txtDate;
    private int ID;

    /**
     * 初始化绑值，获取当前需要显示的数据
     */
    private void init(Bundle bundle) {
//        获取需要显示的文章ID
        ID=bundle.getInt("ID");
//        绑定视图对象
        mdBody = findViewById(R.id.arcBody);
        txtTitle = findViewById(R.id.arcTitle);
        txtTalk = findViewById(R.id.arcTalk);
        txtDate = findViewById(R.id.arcDate);
        txtUser = findViewById(R.id.arcUser);
        txtWatch = findViewById(R.id.arcWatch);
    }


    /**
     * handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                AjaxResult res = (AjaxResult) msg.obj;
                finishUpdateArcitle(res);
            }
        }
    };

    /**
     * 更新文章的回调方法
     */
    private void finishUpdateArcitle(AjaxResult res) {
        if (res.JudgeCode(ArticleShow.this)) {
            Map<String, Object> data = res.getData();
            txtTitle.setText(data.get("title").toString());
            txtUser.setText(((Map<String, Object>) (data.get("user"))).get("name").toString());
            mdBody.loadMarkdown(data.get("body").toString());
            txtTalk.setText(((Integer)data.get("discussCount")).toString());
            txtWatch.setText(((Integer)data.get("blogViews")).toString());

            Date date = new Date((Long) data.get("time"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            txtDate.setText(dateFormat.format(date));
        }
    }

    /**
     * 更新文章
     */
    private void updateArcitle() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        AjaxInterface ajax = new AjaxInterface(
                                String.format("/blog/%d/true", ID)
                        );
                        ajax.setType(AjaxInterface.GET);
                        AjaxResult res = ajax.doAjaxWithJSON();
                        handler.sendMessage(handler.obtainMessage(1, res));
                    }
                }
        ).start();
    }

}
