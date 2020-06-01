package top.jaken.mouseblog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;

public class CardMessageAdapter extends ArrayAdapter {
    private final int ImageId;
    private String radiotext;

    public CardMessageAdapter(Context context, int headImage, List<Map<String,Object>> obj) {
        super(context, headImage, obj);
        ImageId=headImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> cardMessage=(Map<String,Object> ) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
//        此处开始梆值
        TextView name = view.findViewById(R.id.CardMessageName);
        TextView body = view.findViewById(R.id.CardMessageBody);
        TextView date = view.findViewById(R.id.CardMessageDate);
//        开始设置值
        name.setText((String) cardMessage.get("name"));
        body.setText((String) cardMessage.get("body"));
        //日期格式化
        Date dateDate = new Date((Long) cardMessage.get("time"));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        date.setText(simpleDateFormat.format(dateDate));
        return view;
    }
}
