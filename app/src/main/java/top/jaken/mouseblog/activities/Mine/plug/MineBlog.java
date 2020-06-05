package top.jaken.mouseblog.activities.Mine.plug;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.ArticleShow;
import top.jaken.mouseblog.activities.Blog.BlogMine;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.activities.Mine.Mine;
import top.jaken.mouseblog.adapter.CardBlogMineAdapter;
import top.jaken.mouseblog.adapter.CardMineBlogAdapter;
import top.jaken.mouseblog.adapter.CardMineTagsAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class MineBlog extends AppCompatActivity {
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private List<Map<String,Object>> data;
    private int total,count;

    private void finishHandler(Message msg) {
        switch (msg.what) {
            case 1:
//                获取数据的回调
                finishGetData((AjaxResult) msg.obj);
                break;
            case 2:
                finishDeleteItem((AjaxResult) msg.obj);
                break;
            default:
                Log.e("BlogMine", "回调异常");
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finishHandler(msg);
        }
    };

    /**
     * 删除后的回调finishDeleteItem
     * @param result
     */
    private void finishDeleteItem(AjaxResult result) {
        if (result.JudgeCode(MineBlog.this)) {
            Toast.makeText(MineBlog.this, "删除成功", Toast.LENGTH_SHORT).show();
            get_data(1, 5);
        }
    }

    /**
     * 获取数据的回调 finishGetData
     * @param result
     */
    private void finishGetData(AjaxResult result) {
        if (result.JudgeCode(MineBlog.this)) {
            count++;
            data.addAll((List<Map<String, Object>>) result.getData().get("rows"));
            CardMineBlogAdapter adapter = new CardMineBlogAdapter(MineBlog.this, R.layout.card_blog_mine, data, MineBlog.this,MineBlog.this.getApplication());
            listView.setAdapter(adapter);
        }
        refreshLayout.setRefreshing(false);
    }

    /**
     * 获取数据
     * @param offset
     */
    private void get_data(final int offset) {
        MyApplication app = (MyApplication) this.getApplication();
        String token = (String) app.get(MyApplication.MY_TOKEN_STR);
        if (null == token) {
            Log.i("BlogMine", "还没有登录");
            return;
        }

        if(total!=-1&&(count-1)*offset>total)return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface = new AjaxInterface(String.format("/blog/myblog/%d/%d", count, offset));
                ajaxInterface.addToken(MineBlog.this.getApplication());
                ajaxInterface.setType(AjaxInterface.GET);
                AjaxResult res = ajaxInterface.doAjaxWithJSON();
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
    }
    private void get_data(int start,int offset) {
        count=start;
        total=-1;
        data = new ArrayList<>();
        get_data(offset);
    }

    private void itemClick(int position) {
        Map<String, Object> item = data.get(position);
        int id = (int) item.get("id");
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        Intent intent = new Intent(MineBlog.this, ArticleShow.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        //初始化
        get_data(1, 5);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(position);
            }
        });
    }

    /**
     * 初始化下拉刷新功能
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data(1, 5);
            }
        });
    }

    /**
     * 初始化绑值
     */
    private void init() {
        listView = findViewById(R.id.FrBlogMineListView);
        refreshLayout = findViewById(R.id.FrBlogMineRefresh);
        total=-1;
        count=1;
        data = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_blog);
        init();
        initRefreshLayout();
        initListView();

    }
}
