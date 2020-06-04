package top.jaken.mouseblog.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;

public class CardBlogMineAdapter extends ArrayAdapter {
    private final int ImageId;
    private View view;
    private TextView titleView,dateView,bodyView,tagsView;
    private Button changeBtn,deleteBtn;
    private Map<String,Object> item;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view= LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
        init();
        initData(position);
        return view;
    }

    public CardBlogMineAdapter(Context context, int headImage, List<Map<String,Object>> obj, Application application) {
        super(context, headImage, obj);
        app=(MyApplication)application;
        ImageId=headImage;
    }


}
