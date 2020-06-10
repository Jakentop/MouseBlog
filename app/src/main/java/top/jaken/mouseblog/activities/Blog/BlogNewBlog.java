package top.jaken.mouseblog.activities.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.adapter.CardBlogNewTagsAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

/**
 * @author jaken
 */
public class BlogNewBlog extends AppCompatActivity {

    private EditText body;
    private EditText title;
    private GridView grid;
    private ImageButton sendBtn;
    private List<Map<String,Object>> data;
    private List<Map<String,Object>> check;

    /**
     * Handler的回调方法
     * @param msg
     */
    private void doHandler(Message msg) {
        switch (msg.what) {
            case 1:
                finishGetData((AjaxResult)msg.obj);
                break;
            case 2:
                finishSendBlog((AjaxResult) msg.obj);
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            doHandler(msg);
        }
    };

    /**
     * 结束发送博客
     * @param result
     */
    private void finishSendBlog(AjaxResult result) {
        if (result.JudgeCode(BlogNewBlog.this)) {
            Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 初始化发送按钮
     */
    private void initSendBtn() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> data = new ArrayMap<>();
                        String blogTitle = title.getText().toString();
                        String blogBody = body.getText().toString();
                        List<String> list = new ArrayList<>();
                        for (Map<String, Object> item : check) {
                            list.add(item.get("id").toString());
                        }
                        String tagId = Arrays.toString(list.toArray()).replace("[","").replace("]","");
                        data.put("blogTitle", blogTitle);
                        data.put("blogBody", blogBody);
                        data.put("tagId",tagId);
                        AjaxInterface ajaxInterface = new AjaxInterface("/blog");
                        ajaxInterface.setType(AjaxInterface.POST);
                        ajaxInterface.setData(data);
                        ajaxInterface.addToken(BlogNewBlog.this.getApplication());
                        AjaxResult res = ajaxInterface.doAjaxWithJSON(true);
                        handler.sendMessage(handler.obtainMessage(2, res));
                    }
                }).start();
            }
        });
    }

    /**
     * 获取数据的回调
     * @param result
     */
    private void finishGetData(AjaxResult result) {
        if (result.JudgeCode(BlogNewBlog.this)) {
            data = (List<Map<String, Object>>) result.getData().get("data");
            CardBlogNewTagsAdapter adapter = new CardBlogNewTagsAdapter(BlogNewBlog.this, R.layout.card_blog_tags, data);
            grid.setAdapter(adapter);
        }
    }

    /**
     * 获取标签
     */
    private void get_data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface = new AjaxInterface("/tag");
                ajaxInterface.setType(AjaxInterface.GET);
                ajaxInterface.addToken(BlogNewBlog.this.getApplication());
                AjaxResult result = ajaxInterface.doAjaxWithJSON(true);
                handler.sendMessage(handler.obtainMessage(1, result));
            }
        }).start();
    }

    private Map<String,Object> isChecked(int position) {
        for (Map<String, Object> item : check) {
            if ((int) item.get("position") == position) {
                return item;
            }
        }
        return null;
    }

    private void initGridView() {
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardView cardView = view.findViewById(R.id.CardBlogCardView);
                Map<String, Object> res = isChecked(position);
                if (res == null) {
                    res = new HashMap<>();
                    res.put("position", position);
                    res.put("name", data.get(position).get("name"));
                    res.put("id", data.get(position).get("id"));
                    check.add(res);
                    cardView.setCardBackgroundColor(getColor(R.color.colorDeepTitle));

                }else{
                    check.remove(res);
                    cardView.setCardBackgroundColor(0xFFFFFFFF);
                }

            }
        });
    }

    private void init() {
        body = findViewById(R.id.BlogNewText);
        title = findViewById(R.id.BlogNewTitle);
        grid = findViewById(R.id.BlogNewGridView);
        sendBtn = findViewById(R.id.BlogNewSendBtn);
        data = new ArrayList<>();
        check = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_new_blog);
        init();
        get_data();
        initGridView();
        initSendBtn();

    }
}
