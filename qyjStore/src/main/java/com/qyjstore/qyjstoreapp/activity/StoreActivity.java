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
import com.qyjstore.qyjstoreapp.bean.ProductBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.OkHttpUtil;
import com.qyjstore.qyjstoreapp.utils.ToastUtil;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 库存数据页面
 * @date 2019-07-30
 */
public class StoreActivity extends BaseActivity {
    private Context mContext;

    /** 顶部标题栏 */
    private QMUITopBarLayout mTopBar;
    /** 查询框 */
    private EditText queryEt;
    /** 列表 */
    private XRecyclerView mRecyclerView;
    private StoreActivity.ProductItemAdapter adapter;
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    /** 销售单数据 */
    private List<ProductBean> itemList = new ArrayList<>();
    private Handler loadItemHandler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
        setContentView(R.layout.activity_store);

        mTopBar = findViewById(R.id.activity_store_topbar);
        queryEt = findViewById(R.id.activity_store_queryEt);
        mRecyclerView = findViewById(R.id.activity_store_sellRecyclerView);

        adapter = new StoreActivity.ProductItemAdapter(mContext, this.itemList);

        initTopBar();

        initQueryEt();

        // 初始化列表
        initRecyclerView();

        // 初始化加载数据线程
        initRunnable();

        // 加载数据
        loadProductData(true);
    }

    private void initTopBar() {
        mTopBar.setTitle("产品库存");
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
                loadProductData(true);
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
                loadProductData(true);
            }

            @Override
            public void onLoadMore() {
                loadProductData(false);
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
    private void loadProductData(boolean isReload) {
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
        paramMap.put("title", queryEt.getText().toString());
        paramMap.put("currentPage", String.valueOf(pageIndex));

        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_LIST_STORE, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
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

                List<ProductBean> productBeanList = json.getJSONObject("result").getJSONObject("pageBean").getJSONArray("recordList").toJavaList(ProductBean.class);
                itemList.addAll(productBeanList);
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

    class ProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ProductBean> itemList;
        private Context meContext;

        public ProductItemAdapter(Context meContext, List<ProductBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_store_product, parent, false);

            return new StoreActivity.ProductItemAdapter.HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final ProductBean bean = itemList.get(position);
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).titleTv.setText(bean.getTitle());
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).numberTv.setText(String.valueOf(bean.getNumber()));
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).soldNumberTv.setText(String.valueOf(bean.getSoldNumber()));
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).sellPriceTv.setText(String.valueOf(bean.getPrice().setScale(2, BigDecimal.ROUND_DOWN)));
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).productUnitTv.setText(bean.getProductUnit());
            ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).sellProductUnitTv.setText(bean.getProductUnit());
            if (bean.getStockPrice() != null) {
                ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).stockPriceTv.setText(String.valueOf(bean.getStockPrice().setScale(2, BigDecimal.ROUND_DOWN)));
            } else {
                ((StoreActivity.ProductItemAdapter.HoldView) viewHolder).stockPriceTv.setText("0.00");
            }


            View mItemView = viewHolder.itemView;
            if (position == 0) {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 0;
            } else {
                // 设置间距
                ((RecyclerView.LayoutParams) mItemView.getLayoutParams()).topMargin = 20;
            }

            // mItemView.setOnClickListener(new View.OnClickListener() {
            //     @Override
            //     public void onClick(View v) {
            //         Intent intent = new Intent(mContext, SellOrderInfoActivity.class);
            //         intent.putExtra("orderId", String.valueOf(bean.getId()));
            //         startActivity(intent);
            //     }
            // });
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
            private TextView titleTv;
            private TextView numberTv;
            private TextView soldNumberTv;
            private TextView sellPriceTv;
            private TextView stockPriceTv;
            private TextView productUnitTv;
            private TextView sellProductUnitTv;

            private HoldView(View itemView) {
                super(itemView);
                // 产品名称
                titleTv = itemView.findViewById(R.id.item_store_productTitle);
                // 库存数量
                numberTv = itemView.findViewById(R.id.item_store_number);
                // 已售数量
                soldNumberTv = itemView.findViewById(R.id.item_store_soldNumber);
                // 预售金额
                sellPriceTv = itemView.findViewById(R.id.item_store_sellPrice);
                // 进货金额
                stockPriceTv = itemView.findViewById(R.id.item_store_stockPrice);
                // 库存单位
                productUnitTv = itemView.findViewById(R.id.item_store_productUnit);
                // 已售单位
                sellProductUnitTv = itemView.findViewById(R.id.item_store_sellProductUnit);
            }
        }
    }
}
