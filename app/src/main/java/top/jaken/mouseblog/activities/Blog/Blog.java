package top.jaken.mouseblog.activities.Blog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.adapter.FrBlogFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Blog extends Fragment {

    private ViewPager pager;
    private TabLayout layout;
    private List<Fragment> list;

    public Blog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false);

    }

    /**
     * 视图创建后操作
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewPager();
    }

    /**
     * activity的初始化
     * @param view
     */
    private void init(View view) {
        pager = view.findViewById(R.id.FrBlogViewPager);
        layout=view.findViewById(R.id.FrBlogTabLayout);
    }

    /**
     * layout初始化，在注册一个Fragment后记得同步注册一个layout哈，我比较懒就不做绑定了
     */
    private void initTabLayout() {
        String[] title={"博客","我的"};
        layout.setupWithViewPager(pager);
        for (int i = 0; i < layout.getTabCount(); i++) {
            TabLayout.Tab tab = layout.getTabAt(i);
            if (tab != null) {
//                此处修改tab的内容或者样式
                tab.setText(title[i]);
            }
        }
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        list = new ArrayList<Fragment>();
        list.add(new BlogBlog());
        list.add(new BlogMine());

        pager.setAdapter(new FrBlogFragmentPagerAdapter(this.getFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                list));
    }
}
