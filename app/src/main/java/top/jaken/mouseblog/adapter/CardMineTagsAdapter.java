package top.jaken.mouseblog.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.activities.Mine.plug.MineCardTags;
import top.jaken.mouseblog.tools.AjaxInterface;
import top.jaken.mouseblog.tools.AjaxResult;

public class CardMineTagsAdapter extends ArrayAdapter {
    private final int ImageId;
    private String radiotext;
    private Button edit,delete;
    private TextView text;
    private View view;
    Map<String,Object> cardTags;
    MineCardTags mine;
    MyApplication app;



    private void initText() {
        text.setText(cardTags.get("name").toString());
    }

    /**
     * 发送一个请求，可以是修改请求也可以是删除请求
     */
    private void send_message(final Tag tag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AjaxInterface ajaxInterface;
                if ("".equals(tag.getName())) {
                    ajaxInterface = new AjaxInterface(String.format("/tag/%d", tag.getId()));
                    ajaxInterface.setType(AjaxInterface.DELETE);
                    ajaxInterface.addToken(app);
                    AjaxResult res = ajaxInterface.doAjaxWithJSON();
                    mine.handler.sendMessage(mine.handler.obtainMessage(2, res));
                }
                else
                {
                    ajaxInterface = new AjaxInterface("/tag");
                    ajaxInterface.setType(AjaxInterface.PUT);
                    ajaxInterface.addDataItem("tagId", tag.getId().toString());
                    ajaxInterface.addDataItem("tagName",tag.getName().toString());
                    ajaxInterface.addToken(app);
                    AjaxResult res = ajaxInterface.doAjaxWithJSON();
                    mine.handler.sendMessage(mine.handler.obtainMessage(3, res));
                }
            }
        }).start();
    }

    /**
     * 初始化修改标签功能Edit
     */
    private void initEdit() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mine);
                builder.setIcon(R.drawable.eye);
                builder.setTitle("修改当前Tag");
                View view = LayoutInflater.from(mine).inflate(R.layout.dialog_mine_tags_adapter_edit, null);
                builder.setView(view);
                final AlertDialog dialog= builder.show();
                final EditText text = view.findViewById(R.id.NewTxt);
                final Button btn = view.findViewById(R.id.Enter);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tag tag = new Tag((int) cardTags.get("id"), text.getText().toString());
                        send_message(tag);
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    /**
     * 初始化删除标签功能Delete
     */
    private void initDelete() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message(new Tag((int) cardTags.get("id")));
            }
        });
    }

    /**
     * 初始化绑值
     * @param position
     * @param convertView
     * @param parent
     */
    private void init(int position, View convertView, ViewGroup parent) {
        cardTags=(Map<String,Object> ) getItem(position);
        view = LayoutInflater.from(getContext()).inflate(ImageId, parent,false);
        text = view.findViewById(R.id.CardMineTags);
        edit = view.findViewById(R.id.CardBlogMineChange);
        delete = view.findViewById(R.id.CardBlogMineDelete);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        init(position, convertView, parent);
        initText();
        initEdit();
        initDelete();
        return view;
    }

    public CardMineTagsAdapter(Context context, int headImage, List<Map<String,Object>> obj, MineCardTags out, Application application) {
        super(context, headImage, obj);
        app=(MyApplication)application;
        mine=out;
        ImageId=headImage;
    }

    private class Tag{
        private int id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Tag(int id) {
            this.id=id;
            this.name = "";
        }
        public Tag(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
