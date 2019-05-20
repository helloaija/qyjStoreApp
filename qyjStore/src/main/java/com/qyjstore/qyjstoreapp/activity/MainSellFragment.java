package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author shitl
 * @Description 主界面-出库
 * @date 2019-05-17
 */
public class MainSellFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sell, container, false);

        int len = 20;
        List<SellOrderBean> itemList = new ArrayList<>();
        for (int i = 1; i <= len; i ++) {
            SellOrderBean bean = new SellOrderBean("用户名" + i, new Date());
            itemList.add(bean);
        }
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_expandable_list_item_1, orders);
        ListView listView = view.findViewById(R.id.fragment_main_sell_lv_sell);
        listView.setAdapter(new SellOrderItemAdapter(view.getContext(), itemList));

        return view;
    }

    class SellOrderItemAdapter extends BaseAdapter {

        private List<SellOrderBean> itemList;
        private Context meContext;

        public SellOrderItemAdapter(Context meContext, List<SellOrderBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(meContext).inflate(R.layout.item_main_sell, parent, false);
            TextView userNameTv = convertView.findViewById(R.id.item_main_sell_tv_userName);
            userNameTv.setText(itemList.get(position).getUserName());
            TextView orderTimeTv = convertView.findViewById(R.id.item_main_sell_tv_orderTime);
            orderTimeTv.setText(DateUtil.parseDateToString(itemList.get(position).getOrderTime(), DateUtil.DATE_FORMAT_DATE));
            return convertView;
        }
    }

}
