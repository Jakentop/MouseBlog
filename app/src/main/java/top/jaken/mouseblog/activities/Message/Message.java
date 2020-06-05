package top.jaken.mouseblog.activities.Message;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.adapter.CardMessageAdapter;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private ImageButton btn;
    private EditText body;
    private List<Map<String,Object>> messageList;
    private int total=-1,count=1;

    /**
     * handle
     */
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            finishGetMessage(msg);
        }
    };

    /**
     * handle获取数据后的回调
     * @param msg
     */
    private void finishGetMessage(android.os.Message msg) {
        if (msg.what == 1) {
            CardMessageAdapter adapter = new CardMessageAdapter(Message.this.getContext(), R.layout.card_message, messageList);
            listView.setAdapter(adapter);
            count++;
            refreshLayout.setRefreshing(false);
        } else if (msg.what == 2) {
//            添加成功
            AjaxResult res = (AjaxResult) msg.obj;
            if(res.JudgeCode(Message.this.getContext()))
            get_message(5);

        }
    }

    /**
     * 通过请求获取message留言信息
     * @param start
     * @param offset
     */
    private void get_message(final int start,final int offset) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                第一次调用初始化
                if(start==1) {
                    messageList = new ArrayList<>();
                    total=-1;count=1;
                }
//                以及到底了不需要请求
                if (total!=-1&&(start-1)*5>total) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                AjaxInterface ajaxInterface = new AjaxInterface(String.format("/message/%d/%d", start, offset));
                ajaxInterface.setType(AjaxInterface.GET);
                AjaxResult res = ajaxInterface.doAjaxWithJSON();
                if (res.JudgeCode()) {
                    total = (int) res.getData().get("total");
//                    将当前获取到的数据添加进来
                    messageList.addAll((List<Map<String, Object>>) res.getData().get("rows"));
                    handler.sendMessage(handler.obtainMessage(1));
                }
                handler.sendMessage(handler.obtainMessage(0));
            }
        }).start();
    }
    private void get_message(int offset) {
        get_message(count, offset);
    }

    /**
     * 初始化ImageButton留言板的提交留言功能
     */
    private void initBtn() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = body.getText().toString();
                new Thread(new Runnable() {
                   @Override
                   public void run() {
                       AjaxInterface ajaxInterface = new AjaxInterface("/message");
                       ajaxInterface.setType(AjaxInterface.POST);
                       ajaxInterface.addDataItem("messageBody", text);
                       MyApplication app = (MyApplication) Message.this.getContext().getApplicationContext();
                       ajaxInterface.addRequestProperty("Authorization", (String) app.get(MyApplication.MY_TOKEN_STR));
                       AjaxResult res = ajaxInterface.doAjaxWithJSON();
                       handler.sendMessage(handler.obtainMessage(2, res));

                    }
                }).start();
                body.setText("");
            }
        });
    }

    /**
     * 初始化refreshLayout上拉刷新
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_message(1,5);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.loading);

    }

    /**
     * 初始化listview,下拉加载更多内容
     */
    private void initListView() {
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE)
                {
                    get_message(5);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        get_message(1, 5);
    }

    /**
     * 执行页面绑定等
     */
    private void init() {
        listView = this.getView().findViewById(R.id.FrMessageListView);
        refreshLayout = this.getView().findViewById(R.id.FrMessageRefreshLayout);
        btn = this.getView().findViewById(R.id.FrMessageImageButton);
        body = this.getView().findViewById(R.id.FrMessageBody);

    }

    /**
     * 视图创建后行为
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListView();
        initRefreshLayout();
        initBtn();
    }

    public Message() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}
