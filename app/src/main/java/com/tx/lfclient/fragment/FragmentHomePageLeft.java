package com.tx.lfclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tx.lfclient.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.fragment_home_page_left)
public class FragmentHomePageLeft extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this,inflater,container);
        return view;
    }
}
