package top.jaken.mouseblog.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;

import static java.nio.file.Paths.get;


public class BlogCardAdapter extends ArrayAdapter {

    private final int ImageId;
    private String radiotext;

    public BlogCardAdapter(Context context, int headImage, List<Map<String,Object>> obj) {
        super(context, headImage, obj);
        ImageId=headImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> blogCard=(Map<String,Object> ) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
//        此处开始梆值
        TextView title=view.findViewById(R.id.Title);
        TextView state = view.findViewById(R.id.State);
        TextView user = view.findViewById(R.id.User);
        TextView body = view.findViewById(R.id.Body);
        TextView tags = view.findViewById(R.id.Tags);
        TextView date = view.findViewById(R.id.Date);
//        开始设置值
        title.setText((String) blogCard.get("title"));
        state.setText(((Integer) blogCard.get("state")).toString());
        user.setText((String) ((Map<String, Object>) blogCard.get("user")).get("name"));
        String bodyString=(String) blogCard.get("body");
        bodyString.replace('\n', ' ');
        body.setText(bodyString.length() > 50 ? bodyString.substring(0, 50)+"..." : bodyString);//50字限制
        //日期格式化
        Date dateDate = new Date((Long) blogCard.get("time"));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        date.setText(simpleDateFormat.format(dateDate));
        //设置标签
        String tagString="";
        List<Map<String,Object>> tagsMap = (List<Map<String,Object>>) blogCard.get("tags");
        for (Map<String, Object> item : tagsMap) {
            tagString += item.get("name") + ";";
        }
        tags.setText(tagString);
        return view;
    }
}
