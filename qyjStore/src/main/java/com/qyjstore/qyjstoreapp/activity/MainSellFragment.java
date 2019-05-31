package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;
import com.qyjstore.qyjstoreapp.utils.EnumUtil;
import com.qyjstore.qyjstoreapp.utils.HttpUtil;
import com.qyjstore.qyjstoreapp.utils.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 主界面-出库
 * @date 2019-05-17
 */
public class MainSellFragment extends Fragment {
    private Context context;
    /** 查询框 */
    private EditText queryEt;
    /** 查询按钮 */
    private Button queryBtn;
    /** 列表 */
    private XRecyclerView recyclerView;
    /** 销售单数据 */
    private List<SellOrderBean> itemList = new ArrayList<>();
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    private Handler loadItemHandler;
    private Runnable runnable;
    /** 查询内容 */
    private String queryText = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_sell, container, false);

        context = this.getContext();
        queryEt = view.findViewById(R.id.fragment_main_sell_et_query);
        queryBtn = view.findViewById(R.id.fragment_main_sell_btn_query);
        recyclerView = view.findViewById(R.id.fragment_main_sell_xrv);

        SellOrderItemAdapter adapter = new SellOrderItemAdapter(view.getContext(), this.itemList);
        recyclerView.setAdapter(adapter);

        loadItemHandler = new Handler();

        configRecyclerView();

        queryBtnSetOnClickListener();

        createRunnable();

        this.loadSellOrderData();
        return view;
    }

    /**
     * 配置列表
     */
    private void configRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置纵向
        recyclerView.setLayoutManager(layoutManager);
        // 显示刷新时间
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                pageCount = 1;
                itemList.clear();
                loadSellOrderData();
            }

            @Override
            public void onLoadMore() {
                pageIndex = pageIndex + 1;
                loadSellOrderData();
            }
        });
    }

    /**
     * 查询按钮监听点击事件
     */
    private void queryBtnSetOnClickListener() {
        queryText = queryEt.getText().toString();
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
        if (pageIndex > pageCount) {
            recyclerView.setNoMore(true);
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", queryText);
        paramMap.put("currentPage", String.valueOf(pageIndex));
        HttpUtil.doGetAsyn(ConfigUtil.SYS_SERVICE_LIST_SELL_ORDER, paramMap, new HttpUtil.CallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                if ("0000".equals(json.getString("resultCode"))) {
                    pageCount = json.getJSONObject("result").getInteger("pageCount");
                    if (pageIndex > pageCount) {
                        recyclerView.setNoMore(true);
                        return;
                    }
                    String recordListStr = json.getJSONObject("result").getString("recordList");
                    List<SellOrderBean> sellOrderBeanList = JSON.parseArray(recordListStr, SellOrderBean.class);
                    itemList.addAll(sellOrderBeanList);
                    loadItemHandler.post(runnable);
                }
            }

            @Override
            public void onError(int responseCode, String msg) {
                AppUtil.handleLoginExpire(context, responseCode);
            }
        });
    }

    /**
     * 创建加载数据线程
     */
    private void createRunnable() {
        this.runnable = new Runnable() {
            @Override
            public void run() {
                recyclerView.refreshComplete();
                if (pageIndex >= pageCount) {
                    recyclerView.setNoMore(true);
                }
            }
        };
    }

    class SellOrderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<SellOrderBean> itemList;
        private Context meContext;

        public SellOrderItemAdapter(Context meContext, List<SellOrderBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_main_sell, parent, false);

            return new HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            SellOrderBean bean = itemList.get(position);
            ((HoldView) viewHolder).orderNumberTv.setText(bean.getOrderNumber());
            ((HoldView) viewHolder).userNameTv.setText(bean.getUserName());
            ((HoldView) viewHolder).orderTimeTv.setText(DateUtil.parseDateToString(bean.getOrderTime(), DateUtil.DATE_FORMAT_DATE));
            ((HoldView) viewHolder).orderAmountLongTv.setText(meContext.getString(R.string.text_rmb_sign) + String.valueOf(bean.getOrderAmount().longValue()));
            ((HoldView) viewHolder).orderAmountScaleTv.setText(meContext.getString(R.string.text_split_point) + NumberUtil.getScale(bean.getOrderAmount(), 2));

            BigDecimal profitAmount = bean.getProfitAmount() == null ? new BigDecimal("0.00") : bean.getProfitAmount();
            ((HoldView) viewHolder).profitAmountLongTv.setText(meContext.getString(R.string.text_rmb_sign) + String.valueOf(profitAmount.longValue()));

            ((HoldView) viewHolder).profitAmountScaleTv.setText(meContext.getString(R.string.text_split_point) + NumberUtil.getScale(profitAmount, 2));

            ((HoldView) viewHolder).orderStatusTv.setText(EnumUtil.OrderStatusEnum.getTextByValue(bean.getOrderStatus()));

            if (position != 0) {
                // 设置间距
                ((RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams()).topMargin = 20;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        private class HoldView extends RecyclerView.ViewHolder {
            private TextView orderNumberTv;
            private TextView userNameTv;
            private TextView orderTimeTv;
            private TextView orderAmountLongTv;
            private TextView orderAmountScaleTv;
            private TextView profitAmountLongTv;
            private TextView profitAmountScaleTv;
            private TextView orderStatusTv;

            private HoldView(View itemView) {
                super(itemView);
                // 订单编号
                orderNumberTv = itemView.findViewById(R.id.item_main_sell_tv_orderNumber);
                // 订单用户
                userNameTv = itemView.findViewById(R.id.item_main_sell_tv_userName);
                // 订单时间
                orderTimeTv = itemView.findViewById(R.id.item_main_sell_tv_orderTime);
                // 订单金额
                orderAmountLongTv = itemView.findViewById(R.id.item_main_sell_tv_orderAmount_long);
                orderAmountScaleTv = itemView.findViewById(R.id.item_main_sell_tv_orderAmount_scale);
                // 订单利润
                profitAmountLongTv = itemView.findViewById(R.id.item_main_sell_tv_profitAmount_long);
                profitAmountScaleTv = itemView.findViewById(R.id.item_main_sell_tv_profitAmount_scale);
                // 订单状态
                orderStatusTv = itemView.findViewById(R.id.item_main_sell_tv_orderStatus);
            }
        }
    }
}
