package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author shitl
 * @Description 主界面-出库
 * @date 2019-05-17
 */
public class MainSellFragment extends Fragment {
    private EditText queryEt;
    private Button queryBtn;
    /** 销售单数据 */
    private List<SellOrderBean> itemList = new ArrayList<>();
    private BaseAdapter adapter;
    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sell, container, false);

        queryEt = view.findViewById(R.id.fragment_main_sell_et_query);
        queryBtn = view.findViewById(R.id.fragment_main_sell_btn_query);

        adapter = new SellOrderItemAdapter(view.getContext(), this.itemList);
        handler = new Handler();
        ListView listView = view.findViewById(R.id.fragment_main_sell_lv_sell);
        listView.setAdapter(adapter);

        queryBtnSetOnClickListener();
        createRunnable();

        this.loadSellOrderData();
        return view;
    }

    private void createRunnable() {
        this.runnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * 查询按钮监听点击事件
     */
    private void queryBtnSetOnClickListener() {
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSellOrderData();
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadSellOrderData() {
        HttpUtil.doGetAsyn(ConfigUtil.SYS_SERVICE_LIST_SELL_ORDER, null, new HttpUtil.CallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                if ("0000".equals(json.getString("resultCode"))) {
                    String recordListStr = json.getJSONObject("result").getJSONObject("pageBean").getString("recordList");
                    List<SellOrderBean> sellOrderBeanList = JSON.parseArray(recordListStr, SellOrderBean.class);
                    itemList.addAll(sellOrderBeanList);
                    handler.post(runnable);
                }
            }

            @Override
            public void onError(int responseCode, String msg) {
                System.out.println(responseCode + msg);
            }
        });
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
            // 订单编号
            TextView orderNumberTv = convertView.findViewById(R.id.item_main_sell_tv_orderNumber);
            orderNumberTv.setText(itemList.get(position).getOrderNumber());
            // 订单用户
            TextView userNameTv = convertView.findViewById(R.id.item_main_sell_tv_userName);
            userNameTv.setText(itemList.get(position).getUserName());
            // 订单时间
            TextView orderTimeTv = convertView.findViewById(R.id.item_main_sell_tv_orderTime);
            orderTimeTv.setText(DateUtil.parseDateToString(itemList.get(position).getOrderTime(), DateUtil.DATE_FORMAT_DATE));
            // 订单金额
            TextView orderAmountLongTv = convertView.findViewById(R.id.item_main_sell_tv_orderAmount_long);
            orderAmountLongTv.setText(meContext.getString(R.string.text_rmb_sign) + String.valueOf(itemList.get(position).getOrderAmount().longValue()));
            TextView orderAmountScaleTv = convertView.findViewById(R.id.item_main_sell_tv_orderAmount_scale);
            orderAmountScaleTv.setText(meContext.getString(R.string.text_split_point) + NumberUtil.getScale(itemList.get(position).getOrderAmount(), 2));
            // 订单利润
            TextView profitAmountLongTv = convertView.findViewById(R.id.item_main_sell_tv_profitAmount_long);
            BigDecimal profitAmount = itemList.get(position).getProfitAmount() == null ? new BigDecimal("0.00") : itemList.get(position).getProfitAmount();
            profitAmountLongTv.setText(meContext.getString(R.string.text_rmb_sign) + String.valueOf(profitAmount.longValue()));
            TextView profitAmountScaleTv = convertView.findViewById(R.id.item_main_sell_tv_profitAmount_scale);
            profitAmountScaleTv.setText(meContext.getString(R.string.text_split_point) + NumberUtil.getScale(profitAmount, 2));
            // 订单状态
            TextView orderStatusTv = convertView.findViewById(R.id.item_main_sell_tv_orderStatus);
            orderStatusTv.setText(itemList.get(position).getOrderStatus());
            return convertView;
        }
    }



}
