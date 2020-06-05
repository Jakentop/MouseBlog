package top.jaken.mouseblog.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.activities.Mine.plug.MineBlog;
import top.jaken.mouseblog.dao.Blog;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class CardMineBlogAdapter extends ArrayAdapter {
    private final int ImageId;
    private View view;
    private TextView titleView,dateView,bodyView,tagsView;
    private Button changeBtn,deleteBtn;
    private Map<String,Object> item;
    private MineBlog mineBlog;
    MyApplication app;

    /**
     * 初始化数据
     */
    private void initData(int position) {
        item = (Map<String, Object>) getItem(position);
        titleView.setText((String) item.get("title"));

        Date date = new Date((Long) item.get("time"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dateView.setText(simpleDateFormat.format(date));

        String bodyStr = (String) item.get("body");
        bodyStr = bodyStr.length() > 50 ? bodyStr.substring(50) : bodyStr;
        bodyView.setText(bodyStr);

        String tagsStr="";
        List<Map<String, Object>> tags = (List<Map<String, Object>>) item.get("tags");
        for (Map<String, Object> it : tags) {
            tagsStr += (String) it.get("name");
        }
        tagsView.setText(tagsStr);

    }

    /**
     * 初始化绑值
     */
    private void init() {
        titleView = view.findViewById(R.id.CardBlogMineTitle);
        dateView = view.findViewById(R.id.CardBlogMineDate);
        bodyView = view.findViewById(R.id.CardBlogMineBody);
        tagsView = view.findViewById(R.id.CardBlogMineTags);
        changeBtn = view.findViewById(R.id.CardBlogMineChange);
        deleteBtn = view.findViewById(R.id.CardBlogMineDelete);
    }

    private void sendMessage(final Blog blog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == blog.getBlogTitle()) {
                    AjaxInterface ajaxInterface = new AjaxInterface(String.format("/blog/%d", (blog.getTagId())));
                    ajaxInterface.setType(AjaxInterface.DELETE);
                    ajaxInterface.addToken(app);
                    AjaxResult result = ajaxInterface.doAjaxWithJSON(true);
                    mineBlog.handler.sendMessage(mineBlog.handler.obtainMessage(2, result));
                }
                else
                {
                }

            }
        }).start();
    }

    /**
     * 初始化登出
     */
    private void initDeleteBtn() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog blog = new Blog((Integer) item.get("id"));
                sendMessage(blog);
            }
        });
    }

    /**
     * 初始化修改功能
     */
    private void initChangeBtn() {
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(app, "修改功能请使用web端", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view= LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
        init();
        initData(position);
        initDeleteBtn();
        initChangeBtn();
        return view;
    }

    public CardMineBlogAdapter(Context context, int headImage, List<Map<String,Object>> obj, MineBlog mineBlog, Application application) {
        super(context, headImage, obj);
        app=(MyApplication)application;
        this.mineBlog = mineBlog;
        ImageId=headImage;
    }
}

