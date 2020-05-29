package top.jaken.mouseblog.activities.Blog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.ArticleShow;
import top.jaken.mouseblog.adapter.BlogCardAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogBlog extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 文章列表
     */
    private List<Map<String,Object>> blogCards;
    /**
     * 上次请求后是否还有内容
     */
    private int total;
    private int count;
    /**
     * 用于绑值的数组，并通过调用动态修改或者重建
     */
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    /**
     * handle用于处理请求完成的回调
     */
    @SuppressLint("HandlerLeak")
    private Handler ajaxFinish=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finishGetData(msg);
        }
    };

    /**
     * 完成请求后绑值回调
     * @param msg
     */
    private void finishGetData(Message msg) {
        BlogCardAdapter adapter = new BlogCardAdapter(BlogBlog.this.getContext(),R.layout.blog_card, blogCards);
//            添加项目列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                Map<String,Object> blogCard=(Map<String,Object>)adapterView.getAdapter().getItem(i);
                intent.setClass(BlogBlog.this.getContext(), ArticleShow.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", (Integer) blogCard.get("id"));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        listView.setAdapter(adapter);
    }

    /**
     * 获取博客列表数据
     * @param start
     * @param offset
     * @param isReplace true表示为替换当前列表
     */
    private void get_data(final int start, final int offset, final boolean isReplace) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final AjaxInterface ajax = new AjaxInterface(String.format("/blog/home/%d/%d", start, offset));
                ajax.setType("GET");
                AjaxResult res = ajax.doAjaxWithJSON();//发起请求
                Log.i("请求返回的结果：", res.toString());
//                处理请求结果
                if (res.JudgeCode(BlogBlog.this.getContext())) {
                    Map<String, Object> data = res.getData();
                    total = (int) data.get("total");
                    List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("rows");
                    if (isReplace) {
                        count=1;
                        blogCards = new ArrayList<Map<String, Object>>();
                    }
                    for (Map<String, Object> item : list) {
                        blogCards.add(item);
                    }
                }
                count++;
                ajaxFinish.sendMessage(new Message());
            }
        });
    }

    /**
     * 绑值初始化
     */
    private void init() {
        listView=BlogBlog.this.getView().findViewById(R.id.FrBlogBlogLists);
        refreshLayout = BlogBlog.this.getView().findViewById(R.id.FrBlogBlogRefreshLayout);
    }

    /**
     * 初始化RefreshLayout
     */
    @SuppressLint("ResourceAsColor")
    private void initRefreshLayout(){
        refreshLayout.setColorSchemeColors(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.design_default_color_primary
        );

    }

    /**
     * 视图创建后做的事情
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
//      绑定初始值
        count=1;
        get_data(count,5,true);
    }

    @Override
    public void onRefresh() {
        get_data(count++,5,false);
    }

    public BlogBlog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_blog, container, false);
    }

}
