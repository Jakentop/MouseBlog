package top.jaken.mouseblog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.adapter.BlogCardAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class blog_list extends AppCompatActivity {

    private List<Map<String,Object>> blogCards = new ArrayList<>();
    private int total;
    @SuppressLint("HandlerLeak")
    private Handler ajaxFinish=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BlogCardAdapter adapter = new BlogCardAdapter(blog_list.this,R.layout.blog_card, blogCards);
            ListView listView=findViewById(R.id.FrBlogBlogLists);
//            添加项目列表点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    Map<String,Object> blogCard=(Map<String,Object>)adapterView.getAdapter().getItem(i);
                    intent.setClass(blog_list.this, ArticleShow.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", (Integer) blogCard.get("id"));
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
//        设置绑定数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_data(1,5);
                ajaxFinish.sendMessage(new Message());
            }
        }).start();


    }

    /**
     * 获取博客列表数据
     * undo：建议编写统一的错误处理页面
     */
    private void get_data(int start,int offset) {
        final AjaxInterface ajax = new AjaxInterface(String.format("/blog/home/%d/%d", start, offset));
        ajax.setType("GET");
        AjaxResult res = ajax.doAjaxWithJSON();//发起请求
        Log.i("请求返回的结果：", res.toString());
//                处理请求结果
        if (res.JudgeCode(blog_list.this)) {

            Map<String, Object> data = res.getData();
            total = (int) data.get("total");
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("rows");
            for (Map<String, Object> item : list) {
                blogCards.add(item);
            }
        }
    }
}
