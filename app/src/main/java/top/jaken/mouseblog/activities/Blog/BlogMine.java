package top.jaken.mouseblog.activities.Blog;

import android.annotation.SuppressLint;
import android.app.Application;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.adapter.CardBlogMineAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

/**
 * @deprecated
 * A simple {@link Fragment} subclass.
 */
public class BlogMine extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private List<Map<String,Object>> data;
    private View view;
    private int total,count;

    private void finishHandler(Message msg) {
        switch (msg.what) {
            case 1:
//                获取数据的回调
                finishGetData((AjaxResult) msg.obj);
                break;
            default:
                Log.e("BlogMine", "回调异常");
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finishHandler(msg);
        }
    };

    /**
     * 获取数据的回调
     * @param result
     */
    private void finishGetData(AjaxResult result) {
        if (result.JudgeCode(BlogMine.this.getContext())) {
            count++;
            data.addAll((List<Map<String, Object>>) result.getData().get("rows"));
            CardBlogMineAdapter adapter = new CardBlogMineAdapter(BlogMine.this.getContext(), R.layout.card_blog_mine, data, BlogMine.this.getActivity().getApplication());
            listView.setAdapter(adapter);
        }
    }

    /**
     * 获取数据
     * @param offset
     */
    private void get_data(final int offset) {
        MyApplication app = (MyApplication) this.getActivity().getApplication();
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
                ajaxInterface.addToken(BlogMine.this.getActivity().getApplication());
                ajaxInterface.setType(AjaxInterface.GET);
                AjaxResult res = ajaxInterface.doAjaxWithJSON();
                handler.sendMessage(handler.obtainMessage(1, res));
            }
        }).start();
    }

    private void get_data(int start,int offset) {
        count=start;
        total=-1;
        get_data(offset);
    }

    /**
     * 初始化绑值
     */
    private void init() {
        listView = view.findViewById(R.id.FrBlogMineListView);
        refreshLayout = view.findViewById(R.id.FrBlogMineRefresh);
        total=-1;
        count=1;
        data = new ArrayList<>();
    }

    /**
     * 视图替换完成后
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();
        get_data(5);

    }

    public BlogMine() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_mine, container, false);
    }
}
