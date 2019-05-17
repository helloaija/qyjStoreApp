package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 主界面-出库
 * @date 2019-05-17
 */
public class MainSellFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sell, container, false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content1);
        txt_content.setText("123456");
        return view;
    }
}
