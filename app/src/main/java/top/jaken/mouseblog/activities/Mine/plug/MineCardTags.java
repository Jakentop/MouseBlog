package top.jaken.mouseblog.activities.Mine.plug;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
                finishEdit(msg);
                break;
            case 3:
                finishDelete(msg);

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
            listView.setAdapter(new CardMineTagsAdapter(this, R.id.CardMineTags, list, this));
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
                AjaxResult res = ajaxInterface.doAjaxWithJSON();
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
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
        get_data();
    }
}
