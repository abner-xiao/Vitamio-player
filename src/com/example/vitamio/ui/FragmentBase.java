package com.example.vitamio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.vitamio.R;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-9
 * Time: 下午5:54
 */
public class FragmentBase extends Fragment {
    protected ListView mListView;
    protected View mLoadingLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base, container, false);
        mListView = (ListView) v.findViewById(android.R.id.list);
        mLoadingLayout = v.findViewById(R.id.loading);
        return v;
    }
}
