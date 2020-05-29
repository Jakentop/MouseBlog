package top.jaken.mouseblog.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import br.tiagohm.markdownview.MarkdownView;

public class FrBlogFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public FrBlogFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> mFragments) {
        super(fm, behavior);
        this.list = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getClass().getSimpleName();
    }

}
