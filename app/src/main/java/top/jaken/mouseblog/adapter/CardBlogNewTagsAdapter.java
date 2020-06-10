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


public class CardBlogNewTagsAdapter extends ArrayAdapter {

    private final int ImageId;
    private String radiotext;

    public CardBlogNewTagsAdapter(Context context, int headImage, List<Map<String,Object>> obj) {
        super(context, headImage, obj);
        ImageId=headImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> item=(Map<String,Object> ) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
        TextView text = view.findViewById(R.id.CardBlogTagsText);
        text.setText((String) item.get("name"));
        return view;
    }
}
