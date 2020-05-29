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

import java.util.List;

import top.jaken.mouseblog.R;
import top.jaken.mouseblog.adapter.FrBlogFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Blog extends Fragment {

    private ViewPager pager;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.FrBlogViewPager);
        pager.setAdapter(new FrBlogFragmentPagerAdapter(this.getFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                list));

    }
}
