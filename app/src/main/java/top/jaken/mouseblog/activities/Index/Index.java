package top.jaken.mouseblog.activities.Index;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.activities.Blog.Blog;
import top.jaken.mouseblog.activities.ui.Home;
import top.jaken.mouseblog.activities.ui.Message;
import top.jaken.mouseblog.activities.ui.Mine;

public class Index extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_index);
//        初始化主页导航
        init();
//        初始化App生命周期变量初始值
        initApplication();
    }

    /**
     * 注册主页导航
     */
    private void init() {

        final NavigationSelect navigationSelect = new NavigationSelect(this,R.id.container);
//        主页的导航
        navigationSelect.addFragment(new NavigationSelect.Navigation(R.id.navigation_home,new Home()));
//        博客导航
        navigationSelect.addFragment(new NavigationSelect.Navigation(R.id.navigation_blog,new Blog()));
//        留言板导航
        navigationSelect.addFragment(new NavigationSelect.Navigation(R.id.navigation_message, new Message()));
//        我的导航
        navigationSelect.addFragment(new NavigationSelect.Navigation(R.id.navigation_mine, new Mine()));
//        绑定底部导航
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        添加选中事件
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
              return  navigationSelect.setView(menuItem.getItemId());
            }
        });
        navigationSelect.setDefaultFragment(0);//设置默认为主页
    }

    /**
     * 初始化App生命周期的变量
     * 将token导入app生命周期加快访问速度
     */
    private void initApplication() {
//        设置app的token
        SharedPreferences sharedPreferences = this.
                getApplicationContext().
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.application_token), null);
        MyApplication app = (MyApplication) this.getApplication();
        app.set(MyApplication.MY_TOKEN_STR, token);
    }




}
