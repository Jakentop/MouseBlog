package top.jaken.mouseblog.activities.Mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Index.MyApplication;
import top.jaken.mouseblog.activities.Mine.plug.MineAbout;
import top.jaken.mouseblog.activities.Mine.plug.MineAdmin;
import top.jaken.mouseblog.activities.Mine.plug.MineCardTags;
import top.jaken.mouseblog.activities.Mine.plug.MineChangeEmail;
import top.jaken.mouseblog.activities.Mine.plug.MineChangePassword;
import top.jaken.mouseblog.activities.Mine.plug.MineBlog;
import top.jaken.mouseblog.activities.UserLogin;
import top.jaken.mouseblog.tools.VaildHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mine extends Fragment {

    private CardView about,admin,cardtag,changeEmail,changePassword,mineBlog;
    private TextView name,logout;
    private ConstraintLayout noLogin;
    private View view;

    /**
     * 初始化标签管理CardTags
     */
    private void initCardTags() {
        cardtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MineCardTags.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化修改邮箱ChangeEmail
     */
    private void initChangeEmail() {
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MineChangeEmail.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化修改密码ChangePassword
     */
    private void initChangePassword() {
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MineChangePassword.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化我的博客MineBlog
     */
    private void initMineBlog() {
        mineBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MineBlog.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化管理员界面Admin
     */
    private void initAdmin() {

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication app = (MyApplication) view.getContext().getApplicationContext();
                if ("admin".equals(app.get(MyApplication.MY_USERE_TYPE_STR))) {
                    Intent intent = new Intent(view.getContext(), MineAdmin.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(app, "当前用户没有权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化关于页面About
     */
    private void initAbout() {
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MineAbout.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置当前功能页面的可视状态
     * @param isLogtin
     */
    private void setVisibility(Boolean isLogtin) {
        int ViewID = isLogtin ? View.VISIBLE : View.GONE;
        int ViewIDNoLogin=isLogtin ? View.GONE : View.VISIBLE;
        about.setVisibility(ViewID);
        admin.setVisibility(ViewID);
        cardtag.setVisibility(ViewID);
        changeEmail.setVisibility(ViewID);
        changePassword.setVisibility(ViewID);
        mineBlog.setVisibility(ViewID);
        noLogin.setVisibility(ViewIDNoLogin);

    }

    /**
     * 初始化用户姓名UserName
     */
    private void initName() {
        MyApplication app = (MyApplication) this.getActivity().getApplication();
        if (VaildHelper.isLogin(app)) {
            setVisibility(true);
            name.setText((String) app.get(MyApplication.MY_USER_NAMEE_STR));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ;
                }
            });
        }
        else{
            setVisibility(false);
            name.setText(getString(R.string.fr_mine_login_tips));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Mine.this.getActivity(), UserLogin.class);
                    startActivityForResult(intent,1);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initName();
        initLogout();
    }

    /**
     * 用户登出事件Logout
     * @param app
     */
    private void logoutHandler(MyApplication app) {
//        删除app上下文
        app.delete(MyApplication.MY_TOKEN_STR);
        app.delete(MyApplication.MY_USER_NAMEE_STR);
        app.delete(MyApplication.MY_USERE_TYPE_STR);
//        删除数据库
        SharedPreferences sharedPreferences = Mine.
                this.
                getContext().
                getApplicationContext().
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.application_token), null);
        editor.putString(getString(R.string.application_user_name), null);
        editor.putString(getString(R.string.application_user_type), null);
        editor.commit();
//        修改页面状态
        name.setText(getString(R.string.fr_mine_login_tips));
        logout.setVisibility(View.GONE);
        initName();//初始化名字
        Toast.makeText(Mine.this.getContext(), "用户登出成功", Toast.LENGTH_SHORT).show();
        setVisibility(false);
    }

    /**
     * 初始化用户登出
     */
    private void initLogout() {
        final MyApplication app = (MyApplication) this.getActivity().getApplication();
        if (VaildHelper.isLogin(app)) {
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutHandler(app);
                }
            });
        }
        else{
            logout.setVisibility(View.GONE);
            name.setText(getString(R.string.fr_mine_login_tips));
            logout.setOnClickListener(null);
        }
    }

    /**
     * 绑值初始化
     */
    private void init() {
        view = this.getView();
        about = view.findViewById(R.id.FrMineAbout);
        admin = view.findViewById(R.id.FrMineAdmin);
        cardtag = view.findViewById(R.id.FrMineCardTags);
        changeEmail = view.findViewById(R.id.FrMineChangeEmail);
        changePassword = view.findViewById(R.id.FrMineChangePassword);
        mineBlog = view.findViewById(R.id.FrMineBlog);
        name = view.findViewById(R.id.FrMineUserName);
        logout = view.findViewById(R.id.FrMineLogout);
        noLogin = view.findViewById(R.id.FrMineNoLogin);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            initName();
            initLogout();
        }
    }

    /**
     * 视图创建后
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initName();
        initLogout();
        initAbout();
        initAdmin();
        initMineBlog();
        initCardTags();
        initChangeEmail();
        initChangePassword();
        initChangePassword();
    }

    public Mine() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }
}
