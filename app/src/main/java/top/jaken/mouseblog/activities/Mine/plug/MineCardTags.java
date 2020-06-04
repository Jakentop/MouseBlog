package top.jaken.mouseblog.activities.Mine.plug;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Magnifier;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.adapter.CardMineTagsAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class MineCardTags extends AppCompatActivity {


    private EditText editText;
    private ImageButton btn;
    private ListView listView;
    private List<Map<String, Object>> list;

    /**
     * handler的回调封装
     * @param msg
     */
    private void handlerCallBack(Message msg) {
        switch (msg.what) {
            case 1:
                finishGetData((AjaxResult)msg.obj);
                break;
            case 2:
                finishDelete(msg);
                break;
            case 3:
                finishEdit(msg);
                break;
            case 4:
                finishAddItem((AjaxResult)msg.obj);

        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            handlerCallBack(msg);
        }
    };

    /**
     * 结束编辑
     * @param msg
     */
    public void finishEdit(Message msg) {
        AjaxResult res = (AjaxResult) msg.obj;
        if (res.JudgeCode(MineCardTags.this)) {
            Toast.makeText(MineCardTags.this, "更新成功", Toast.LENGTH_SHORT).show();
            get_data();
        }
    }

    /**
     * 结束删除
     * @param msg
     */
    public void finishDelete(Message msg) {
        AjaxResult res = (AjaxResult) msg.obj;
        if (res.JudgeCode(MineCardTags.this)) {
            Toast.makeText(MineCardTags.this, "删除成功", Toast.LENGTH_SHORT).show();
            get_data();
        }
    }

    /**
     * 完成数据请求后的额回调FinishGetData
     * @param res
     */
    private void finishGetData(AjaxResult res) {
        if (res.JudgeCode(this)) {
            list = (List<Map<String, Object>>) res.getData().get("data");
            listView.setAdapter(new CardMineTagsAdapter(this, R.layout.card_mine_tags, list, this,MineCardTags.this.getApplication()));
        }
    }

    /**
     * 获取新的tag标签数据
     */
    private void get_data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface = new AjaxInterface("/tag");
                ajaxInterface.setType(AjaxInterface.GET);
                ajaxInterface.addToken(MineCardTags.this.getApplication());
                AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
    }

    /**
     * 完成添加后的回调FinishAddItem
     */
    private void finishAddItem(AjaxResult res) {
        if (res.JudgeCode(MineCardTags.this)) {
            Toast.makeText(MineCardTags.this, "添加成功", Toast.LENGTH_SHORT).show();
            get_data();
            editText.setText("");
        }
    }

    /**
     * 初始化新增tag按钮
     */
    private void initBtn() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tagName = editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AjaxInterface ajaxInterface = new AjaxInterface("/tag");
                        ajaxInterface.setType(AjaxInterface.POST);
                        ajaxInterface.addDataItem("tagName", tagName);
                        ajaxInterface.addToken(MineCardTags.this.getApplication());
                        AjaxResult res = ajaxInterface.doAjaxWithJSON();
                        handler.sendMessage(handler.obtainMessage(4, res));
                    }
                }).start();
            }
        });
    }

    /**
     * 初始化绑值
     */
    private void init() {
        editText = findViewById(R.id.MineCardTagsEditText);
        btn = findViewById(R.id.MineCardTagsBtn);
        listView = findViewById(R.id.MineCardTagsListView);
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_card_tags);
        init();
        initBtn();
        get_data();
    }
}
