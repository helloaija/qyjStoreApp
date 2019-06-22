package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.StockOrderBean;
import com.qyjstore.qyjstoreapp.utils.*;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 主界面-入库
 * @date 2019-05-17
 */
public class MainStockFragment extends Fragment {

    private Context context;
    /** 查询框 */
    private EditText queryEt;
    /** 查询按钮 */
    private Button queryBtn;
    /** 新增按钮 */
    private Button addBtn;
    /** 列表 */
    private XRecyclerView recyclerView;
    /** 入库单数据 */
    private List<StockOrderBean> itemList = new ArrayList<>();
    private MainStockFragment.StockOrderItemAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_main_stock, container, false);

        context = this.getContext();
        queryEt = view.findViewById(R.id.fragment_main_stock_et_query);
        queryBtn = view.findViewById(R.id.fragment_main_stock_btn_query);
        addBtn = view.findViewById(R.id.fragment_main_stock_addBtn);
        recyclerView = view.findViewById(R.id.fragment_main_stock_xrv);

        adapter = new MainStockFragment.StockOrderItemAdapter(view.getContext(), this.itemList);
        recyclerView.setAdapter(adapter);

        loadItemHandler = new Handler();

        configRecyclerView();

        queryBtnSetOnClickListener();

        initAddBtn();

        createRunnable();

        this.loadStockOrderData();
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
                adapter.notifyDataSetChanged();
                loadStockOrderData();
            }

            @Override
            public void onLoadMore() {
                pageIndex = pageIndex + 1;
                loadStockOrderData();
            }
        });
    }

    /**
     * 查询按钮监听点击事件
     */
    private void queryBtnSetOnClickListener() {
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryText = queryEt.getText().toString();
                pageIndex = 1;
                pageCount = 1;
                itemList.clear();
                adapter.notifyDataSetChanged();
                loadStockOrderData();
            }
        });
    }

    /**
     * 添加按钮初始化
     */
    private void initAddBtn() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StockOrderInfoActivity.class);
                intent.putExtra("orderId", "");
                startActivity(intent);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadStockOrderData() {
        if (pageIndex > pageCount) {
            recyclerView.setNoMore(true);
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", queryText);
        paramMap.put("currentPage", String.valueOf(pageIndex));

        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_LIST_STOCK_ORDER, paramMap, new OkHttpUtil.HttpCallBack(context) {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response, String responeText) {

                JSONObject json = JSON.parseObject(responeText);
                if (!"0000".equals(json.getString("resultCode"))) {
                    Looper.prepare();
                    ToastUtil.makeText(context, json.getString("resultMessage"));
                    Looper.loop();
                    return;
                }

                pageCount = json.getJSONObject("result").getInteger("pageCount");
                if (pageIndex > pageCount) {
                    recyclerView.setNoMore(true);
                    return;
                }
                JSONArray recordJSONArray = json.getJSONObject("result").getJSONArray("recordList");
                List<StockOrderBean> stockOrderBeanList = recordJSONArray.toJavaList(StockOrderBean.class);
                itemList.addAll(stockOrderBeanList);
                loadItemHandler.post(runnable);
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

    class StockOrderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<StockOrderBean> itemList;
        private Context meContext;

        public StockOrderItemAdapter(Context meContext, List<StockOrderBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_main_stock, parent, false);

            return new MainStockFragment.StockOrderItemAdapter.HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final StockOrderBean bean = itemList.get(position);
            ((MainStockFragment.StockOrderItemAdapter.HoldView) viewHolder).orderNumberTv.setText(bean.getOrderNumber());
            ((MainStockFragment.StockOrderItemAdapter.HoldView) viewHolder).orderTimeTv.setText(DateUtil.parseDateToString(bean.getOrderTime(), DateUtil.DATE_FORMAT_DATE));
            ((MainStockFragment.StockOrderItemAdapter.HoldView) viewHolder).orderAmountLongTv.setText(meContext.getString(R.string.text_rmb_sign) + String.valueOf(bean.getOrderAmount().longValue()));
            ((MainStockFragment.StockOrderItemAdapter.HoldView) viewHolder).orderAmountScaleTv.setText(meContext.getString(R.string.text_split_point) + NumberUtil.getScale(bean.getOrderAmount(), 2));
            ((MainStockFragment.StockOrderItemAdapter.HoldView) viewHolder).orderStatusTv.setText(EnumUtil.OrderStatusEnum.getTextByValue(bean.getOrderStatus()));

            View mItemView = viewHolder.itemView;
            if (position == 0) {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 0;
            } else {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 20;
            }

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StockOrderInfoActivity.class);
                    intent.putExtra("orderId", String.valueOf(bean.getId()));
                    startActivity(intent);
                }
            });
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
            private TextView orderTimeTv;
            private TextView orderAmountLongTv;
            private TextView orderAmountScaleTv;
            private TextView orderStatusTv;

            private HoldView(View itemView) {
                super(itemView);
                // 订单编号
                orderNumberTv = itemView.findViewById(R.id.item_main_stock_tv_orderNumber);
                // 订单时间
                orderTimeTv = itemView.findViewById(R.id.item_main_stock_tv_orderTime);
                // 订单金额
                orderAmountLongTv = itemView.findViewById(R.id.item_main_stock_tv_orderAmount_long);
                orderAmountScaleTv = itemView.findViewById(R.id.item_main_stock_tv_orderAmount_scale);
                // 订单状态
                orderStatusTv = itemView.findViewById(R.id.item_main_stock_tv_orderStatus);
            }
        }
    }
}
