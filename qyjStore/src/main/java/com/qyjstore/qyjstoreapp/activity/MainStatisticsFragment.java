package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 主界面-统计
 * @date 2019-05-17
 */
public class MainStatisticsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_statistics, container, false);
        return view;
    }
}
