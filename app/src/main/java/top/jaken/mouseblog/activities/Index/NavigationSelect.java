package top.jaken.mouseblog.activities.Index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaken
 * 这是一个Fragment的选择器
 */
public class NavigationSelect {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private AppCompatActivity app;
    private int count;
    private List<Navigation> list;
    private int FragmentContainer;

    /**
     * 构造方法
     * @param app
     * @param FragmentContainer
     */
    public NavigationSelect(AppCompatActivity app,int FragmentContainer) {
        this.app = app;
        //使用fragmentmanager和transaction来实现切换效果
        fragmentManager = app.getSupportFragmentManager();
//        初始化注册的列表
        list = new ArrayList<>();
        count=-1;
        this.FragmentContainer=FragmentContainer;
    }

    /**
     * 添加Fragment
     * @param navigation
     * @return
     */
    public boolean addFragment(Navigation navigation) {
        list.add(navigation);
        return true;
    }

    /**
     * 设置默认选中的Fragment
     * @param i
     * @return
     */
    public boolean setDefaultFragment(int i) {
        if (i >= list.size()) {
            return false;
        }
        Navigation navigation = list.get(i);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(this.FragmentContainer, navigation.getFragment());
        transaction.commit();
        count = i;
        return true;
    }
    public boolean setDefaultFragment(){
       return setDefaultFragment(0);
    }

    /**
     * 下一个Fragment
     * @param i
     * @return
     */
    public boolean next(int i) {
        if (list.size()==0) {
            return false;
        }
        i = (count + i) % list.size();
        return setDefaultFragment(i);
    }
    public boolean next(){
        return next(1);}

    /**
     * 设置一个fragment，注意你只需要传入已经注册过的fragment类即可
     * @param fragment
     * @return
     */
    public boolean setView(Class<androidx.fragment.app.Fragment> fragment) {
        for (Navigation navigation : list) {
            if(navigation.getFragment().getClass().equals(fragment)){
                transaction = fragmentManager.beginTransaction();
                transaction.replace(this.FragmentContainer, navigation.getFragment());
                transaction.commit();
                return true;
            }
        }
        return false;
    }
    public boolean setView(int navigationId) {
        for (Navigation navigation : list) {
            if(navigation.getNavigationId().equals(navigationId)){
                transaction = fragmentManager.beginTransaction();
                transaction.replace(this.FragmentContainer, navigation.getFragment());
                transaction.commit();
                return true;
            }
        }
        return false;
    }

    /**
     * 内部类用于分装Navigation的Layout和Class
     */
    static class Navigation{
        private  androidx.fragment.app.Fragment fragment;
        private int navigationId;

        /**
         * 创建一个Navigation，第一个参数为meun设置的导航id，第三个为fragment类
         * @param navigationId 导航的ID，此处的ID与导航Menu文件一致
         * @param fragment Fragment加载类的ID
         */
        public Navigation(int navigationId, Fragment fragment) {
            this.fragment = fragment;
            this.navigationId = navigationId;
        }

        public Integer getNavigationId() {
            return navigationId;
        }
        public void setNavigationId(int navigationId) {
            this.navigationId = navigationId;
        }

        public Fragment getFragment() {
            return fragment;
        }
        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }


    }
}
