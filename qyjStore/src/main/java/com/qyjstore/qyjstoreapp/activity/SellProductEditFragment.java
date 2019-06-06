package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellProductBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author shitl
 * @Description 销售单编辑-产品
 * @date 2019-06-06
 */
public class SellProductEditFragment extends Fragment {
    private Context mContext;
    private QMUIAnimationListView mListView;
    private List<SellProductBean> mDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_sell_product_edit, container, false);
        mListView = view.findViewById(R.id.fragment_sell_product_edit_lv);

        initListView();

        return view;
    }

    private void initListView() {
        mDataList = new ArrayList<>();
        mDataList.add(new SellProductBean());
        mDataList.add(new SellProductBean());
        mDataList.add(new SellProductBean());
        mDataList.add(new SellProductBean());

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public Object getItem(int position) {
                return mDataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_sell_product_edit, parent, false);
                return view;
            }
        });
    }
}
