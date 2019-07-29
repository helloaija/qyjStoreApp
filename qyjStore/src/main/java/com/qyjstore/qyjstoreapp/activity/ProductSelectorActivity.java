package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.bean.ProductBean;
import com.qyjstore.qyjstoreapp.bean.SpinnerItem;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
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
 * @Description 产品选择器页面
 * @date 2019-06-13
 */
public class ProductSelectorActivity extends BaseActivity {
    public static String BUNDLE_KEY_PAGE_TYPE = "PAGE_TYPE";
    public static String PAGE_TYPE_SELL = "SELL";
    public static String PAGE_TYPE_STOCK = "STOCK";
    /** 页面类型，销售、进货 */
    private String pageType = PAGE_TYPE_SELL;

    private Context mContext;
    /** 查询框 */
    private EditText queryEt;
    /** 列表 */
    private XRecyclerView recyclerView;
    /** 产品数据 */
    private List<ProductBean> itemList = new ArrayList<>();
    private ProductSelectorItemAdapter adapter;
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    private Handler handler;
    private Runnable runnable;

    /** 表单组件 */
    private ConstraintLayout formBarLayout;
    private GridLayout gridLayout;
    private Button addProductBtn;
    private EditText titleEt;
    private Spinner productTypeEt;
    private EditText productUnitEt;
    private EditText priceEt;
    private EditText remarkEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                pageType = bundle.getString(ProductSelectorActivity.BUNDLE_KEY_PAGE_TYPE);
            }
        }

        setContentView(R.layout.activity_product_selector);
        queryEt = findViewById(R.id.activity_product_selector_queryEt);
        recyclerView = findViewById(R.id.activity_product_selector_xrv);

        formBarLayout = findViewById(R.id.activity_product_selector_productFormTopBar);
        gridLayout = findViewById(R.id.activity_product_selector_gridLayout);
        addProductBtn = findViewById(R.id.activity_product_selector_productFormSubmitBtn);
        titleEt = findViewById(R.id.activity_product_selector_title);
        productTypeEt = findViewById(R.id.activity_product_selector_productType);
        productUnitEt = findViewById(R.id.activity_product_selector_productUnit);
        priceEt = findViewById(R.id.activity_product_selector_price);
        remarkEt = findViewById(R.id.activity_product_selector_remark);

        adapter = new ProductSelectorItemAdapter(mContext, this.itemList);
        recyclerView.setAdapter(adapter);

        handler = new Handler();

        initChildView();

        configRecyclerView();

        initRunnable();

        this.loadSellOrderData();
    }

    /**
     * 初始化组件
     */
    private void initChildView() {
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
                pageIndex = 1;
                pageCount = 1;
                itemList.clear();
                adapter.notifyDataSetChanged();
                loadSellOrderData();
            }
        });

        if (PAGE_TYPE_SELL.equals(pageType)) {
            formBarLayout.setVisibility(View.GONE);
            gridLayout.setVisibility(View.GONE);
        } else {
            formBarLayout.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.VISIBLE);

            // 添加产品按钮
            addProductBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = titleEt.getText().toString().trim();
                    String productType = ((SpinnerItem) productTypeEt.getSelectedItem()).getValue();
                    String productUnit = productUnitEt.getText().toString().trim();
                    String price = priceEt.getText().toString().trim();
                    String remark = remarkEt.getText().toString().trim();

                    if (TextUtils.isEmpty(title)) {
                        ToastUtil.makeText(mContext, "产品名不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(productUnit)) {
                        ToastUtil.makeText(mContext, "单位不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(price)) {
                        ToastUtil.makeText(mContext, "售价不能为空");
                        return;
                    }

                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("title", title);
                    paramMap.put("productType", productType);
                    paramMap.put("productUnit", productUnit);
                    paramMap.put("price", price);
                    paramMap.put("remark", remark);

                    OkHttpUtil.doPost(ConfigUtil.SYS_SERVICE_SAVE_PRODUCT, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
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

                            final ProductBean resultBean = json.getObject("result", ProductBean.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setSelectResult(resultBean);
                                    hideKeyboard();
                                    finish();
                                }
                            });
                        }
                    });
                }
            });

            // 产品类型下拉
            SpinnerItem[] spinnerItemList = new SpinnerItem[2];
            spinnerItemList[0] = new SpinnerItem("化肥", "MANURE");
            spinnerItemList[1] = new SpinnerItem("农药", "PESTICIDE");
            ArrayAdapter<SpinnerItem> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, spinnerItemList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productTypeEt.setAdapter(spinnerAdapter);
        }
    }

    /**
     * 配置列表
     */
    private void configRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
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
     * 加载数据
     */
    private void loadSellOrderData() {
        if (pageIndex > pageCount) {
            handler.post(runnable);
            return;
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("title", queryEt.getText().toString());
        paramMap.put("currentPage", String.valueOf(pageIndex));

        // 销售展示进货的产品
        String url = ConfigUtil.SYS_SERVICE_LIST_STOCK_PRODUCT;
        if (PAGE_TYPE_STOCK.equals(pageType)) {
            // 进货展示全部产品
           // url = ConfigUtil.SYS_SERVICE_LIST_PRODUCT;
        }

        OkHttpUtil.doGet(url, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
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

                pageCount = json.getJSONObject("result").getInteger("pageCount");
                if (pageIndex > pageCount) {
                    handler.post(runnable);
                    return;
                }
                String recordListStr = json.getJSONObject("result").getString("recordList");
                List<ProductBean> productBeanList = JSON.parseArray(recordListStr, ProductBean.class);
                itemList.addAll(productBeanList);
                handler.post(runnable);
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
                recyclerView.refreshComplete();
                if (pageIndex >= pageCount) {
                    recyclerView.setNoMore(true);
                }
            }
        };
    }

    class ProductSelectorItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ProductBean> itemList;
        private Context meContext;

        public ProductSelectorItemAdapter(Context meContext, List<ProductBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_product_selector, parent, false);

            return new HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final ProductBean bean = itemList.get(position);
            ((HoldView) viewHolder).titleTv.setText(bean.getTitle());
            ((HoldView) viewHolder).numberTv.setText(AppUtil.getString(bean.getNumber()));

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
                    setSelectResult(bean);
                    hideKeyboard();
                    finish();
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
            private TextView titleTv;
            private TextView numberTv;

            private HoldView(View itemView) {
                super(itemView);
                // 产品名称
                titleTv = itemView.findViewById(R.id.item_product_selector_title);
                // 库存数量
                numberTv = itemView.findViewById(R.id.item_product_selector_number);
            }
        }
    }

    /**
     * 回传选择的产品
     * @param productBean
     */
    private void setSelectResult(ProductBean productBean) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(ConstantUtil.BUNDLE_KEY_SELECTED_PRODUCT, JSON.toJSONString(productBean));
            intent.putExtras(bundle);
        }
        setResult(0, intent);
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View vv = getWindow().peekDecorView();
        if (null != vv && imm != null) {
            imm.hideSoftInputFromWindow(vv.getWindowToken(), 0);
        }
    }

}
