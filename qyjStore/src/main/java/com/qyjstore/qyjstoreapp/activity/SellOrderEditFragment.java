package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 销售单编辑-订单
 * @date 2019-06-06
 */
public class SellOrderEditFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_order_edit, container, false);
        return view;
    }
}
