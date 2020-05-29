package top.jaken.mouseblog.activities.Blog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.jaken.mouseblog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogMine extends Fragment {

    public BlogMine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_mine, container, false);
    }
}
