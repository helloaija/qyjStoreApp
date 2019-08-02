package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.bean.SellProductBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;
import com.qyjstore.qyjstoreapp.utils.NumberUtil;
import com.qyjstore.qyjstoreapp.utils.OkHttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 销售产品列表页面
 * @date 2019-08-01
 */
public class SellProductActivity extends BaseActivity {
    private Context mContext;

    /** 顶部标题栏 */
    private QMUITopBarLayout mTopBar;
    /** 查询框 */
    private EditText queryEt;
    /** 列表 */
    private XRecyclerView mRecyclerView;
    private SellProductActivity.SellPruductItemAdapter adapter;
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    /** 销售单数据 */
    private List<SellProductBean> itemList = new ArrayList<>();
    private Handler loadItemHandler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
        setContentView(R.layout.activity_sell_product);

        mTopBar = findViewById(R.id.activity_sell_product_topbar);
        queryEt = findViewById(R.id.activity_sell_product_queryEt);
        mRecyclerView = findViewById(R.id.activity_sell_product_recyclerView);

        adapter = new SellProductActivity.SellPruductItemAdapter(mContext, this.itemList);

        initTopBar();

        initQueryEt();

        // 初始化列表
        initRecyclerView();

        // 初始化加载数据线程
        initRunnable();

        // 加载数据
        loadSellProductData(true);
    }

    private void initTopBar() {
        mTopBar.setTitle("销售产品列表");
    }

    private void initQueryEt() {
        // 查询框
        queryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadSellProductData(true);
            }
        });
    }

    /**
     * 配置列表
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置纵向
        mRecyclerView.setLayoutManager(layoutManager);
        // 显示刷新时间
        mRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadSellProductData(true);
            }

            @Override
            public void onLoadMore() {
                loadSellProductData(false);
            }
        });
    }

    /**
     * 加载数据
     */
    /**
     * 是否重新加载数据，true：加载第一页，false加载下一页
     * @param isReload
     */
    private void loadSellProductData(boolean isReload) {
        if (isReload) {
            pageIndex = 1;
            pageCount = 1;
            itemList.clear();
            adapter.notifyDataSetChanged();
        } else {
            pageIndex = pageIndex + 1;
        }
        if (pageIndex > pageCount) {
            mRecyclerView.setNoMore(true);
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("nameOrTitle", queryEt.getText().toString());
        paramMap.put("currentPage", String.valueOf(pageIndex));

        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_LIST_SELL_PRODUCT, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response, String responeText) {

                JSONObject json = JSON.parseObject(responeText);
                if (!"0000".equals(json.getString("resultCode"))) {
                    Looper.prepare();
                    ToastUtil.makeText(mContext, json.getString("resultMessage"));
                    Looper.loop();
                    return;
                }

                pageCount = json.getJSONObject("result").getJSONObject("pageBean").getInteger("pageCount");
                if (pageIndex > pageCount) {
                    loadItemHandler.post(runnable);
                    return;
                }

                List<SellProductBean> sellProductBeanList = json.getJSONObject("result").getJSONObject("pageBean").getJSONArray("recordList").toJavaList(SellProductBean.class);
                itemList.addAll(sellProductBeanList);
                loadItemHandler.post(runnable);
            }
        });
    }

    /**
     * 创建加载数据线程
     */
    private void initRunnable() {
        this.runnable = new Runnable() {
            @Override
            public void run() {
                mRecyclerView.refreshComplete();
                if (pageIndex >= pageCount) {
                    mRecyclerView.setNoMore(true);
                }
            }
        };
    }

    class SellPruductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<SellProductBean> itemList;
        private Context meContext;

        public SellPruductItemAdapter(Context meContext, List<SellProductBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_sell_product, parent, false);

            return new SellProductActivity.SellPruductItemAdapter.HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final SellProductBean bean = itemList.get(position);

            ((HoldView) viewHolder).productTitleTv.setText(bean.getProductTitle());
            ((HoldView) viewHolder).userNameTv.setText(bean.getUserName());
            ((HoldView) viewHolder).orderTimeTv.setText(DateUtil.parseDateToString(bean.getOrderTime(), DateUtil.DATE_FORMAT_DATE));
            if (bean.getPrice() != null) {
                ((HoldView) viewHolder).priceTv.setText(NumberUtil.getDoubleString(bean.getPrice(), 2));
            }
            if (bean.getStockPrice() != null) {
                ((HoldView) viewHolder).stockPriceTv.setText(NumberUtil.getDoubleString(bean.getStockPrice(), 2));
            }
            if (bean.getNumber() != null) {
                ((HoldView) viewHolder).numberTv.setText(String.valueOf(bean.getNumber()));
            }
            ((HoldView) viewHolder).productUnitTv.setText(bean.getProductUnit());

            View mItemView = viewHolder.itemView;
            if (position == 0) {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 0;
            } else {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 20;
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
            private TextView productTitleTv;
            private TextView userNameTv;
            private TextView orderTimeTv;
            private TextView priceTv;
            private TextView stockPriceTv;
            private TextView numberTv;
            private TextView productUnitTv;

            private HoldView(View itemView) {
                super(itemView);
                // 产品名称
                productTitleTv = itemView.findViewById(R.id.item_sell_product_productTitle);
                // 用户名
                userNameTv = itemView.findViewById(R.id.item_sell_product_userName);
                // 销售时间
                orderTimeTv = itemView.findViewById(R.id.item_sell_product_orderTime);
                // 售价
                priceTv = itemView.findViewById(R.id.item_sell_product_price);
                // 进价
                stockPriceTv = itemView.findViewById(R.id.item_sell_product_stockPrice);
                // 单位
                productUnitTv = itemView.findViewById(R.id.item_sell_product_productUnit);
                // 销售数量
                numberTv = itemView.findViewById(R.id.item_sell_product_number);
            }
        }
    }
}
