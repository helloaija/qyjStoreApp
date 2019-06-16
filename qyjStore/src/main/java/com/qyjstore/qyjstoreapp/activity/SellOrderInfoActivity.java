package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.bean.SellProductBean;
import com.qyjstore.qyjstoreapp.utils.*;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 销售单详情
 * @date 2019-05-31
 */
public class SellOrderInfoActivity extends BaseActivity {
    private Context mContext;
    /**
     * 页面状态，新增:ADD, 查看:VIEW, 编辑:EDIT,
     */
    private String pageState = PAGE_STATE_ADD;
    private static String PAGE_STATE_ADD = "ADD";
    private static String PAGE_STATE_VIEW = "VIEW";
    private static String PAGE_STATE_EDIT = "EDIT";
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 编辑时的订单ID，为空为新增
     */
    private SellOrderBean sellOrder;
    /**
     * 顶部标题栏
     */
    private QMUITopBarLayout mTopBar;
    /**
     * 标签
     */
    private QMUITabSegment mTabSegment;
    private ViewPager mViewPager;
    /**
     * 订单编辑碎片
     */
    private SellOrderEditFragment orderFragment;
    /**
     * 产品编辑碎片
     */
    private SellProductEditFragment productFragment;

    private List<PagerItem> paperItemList;

    private PagerAdapter mPagerAdapter;

    private Button editBtn;
    private Button concelBtn;
    private Button deleteBtn;
    private Button saveBtn;

    /**
     * 用来初始化tab
     */
    private Handler handler = new InitTapHandler(this);

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
        setContentView(R.layout.activity_sell_order_info);

        this.mTopBar = findViewById(R.id.activity_sell_order_info_topbar);
        this.mTabSegment = findViewById(R.id.activity_sell_order_info_tabSegment);
        this.mViewPager = findViewById(R.id.activity_sell_order_info_viewPager);
        this.editBtn = findViewById(R.id.activity_sell_order_info_editBtn);
        this.concelBtn = findViewById(R.id.activity_sell_order_info_concelBtn);
        this.deleteBtn = findViewById(R.id.activity_sell_order_info_delBtn);
        this.saveBtn = findViewById(R.id.activity_sell_order_info_saveBtn);

        // 初始化topBar
        initTopBar();
        // 初始化按钮
        initButton();
        // 初始化页面状态
        initPageState();
    }

    /**
     * 初始化页面状态
     */
    private void initPageState() {
        Intent intent = getIntent();
        String orderIdIn = intent.getStringExtra("orderId");
        if (!TextUtils.isEmpty(orderIdIn)) {
            orderId = Long.valueOf(orderIdIn);
            pageState = PAGE_STATE_VIEW;

            // 加载数据
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("sellId", String.valueOf(orderId));
            OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_GET_SELL_ORDER, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response, String responeText) {
                    JSONObject json = JSON.parseObject(responeText);
                    if (!isLoadDataSuccess(json)) {
                        Looper.prepare();
                        ToastUtil.makeText(mContext, "加载数据失败");
                        Looper.loop();
                        return;
                    }
                    sellOrder = json.getObject("result", SellOrderBean.class);
                    Log.d("SellOrderInfoActivity", "load sellOrder success:" + sellOrder);
                    handler.sendMessage(new Message());
                }
            });
        } else {
            pageState = PAGE_STATE_ADD;
            // 初始化tab
            initPaperItemList();
            initPagerAdapter();
            initTabSegment();
        }

        setButtonVisibility();
    }

    /**
     * 初始化tab
     */
    private void initPaperItemList() {
        orderFragment = new SellOrderEditFragment();
        productFragment = new SellProductEditFragment();

        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        if (PAGE_STATE_ADD.equals(pageState)) {
            bundle.putBoolean(SellOrderEditFragment.BUNDLE_KEY_READ_ONLY, PAGE_STATE_VIEW.equals(pageState));

            List<SellProductBean> list = new ArrayList<>();
            list.add(new SellProductBean());
            bundle2.putSerializable(SellProductEditFragment.BUNDLE_KEY_PRODUCT_LIST, (Serializable) list);
            bundle2.putBoolean(SellProductEditFragment.BUNDLE_KEY_READ_ONLY, PAGE_STATE_VIEW.equals(pageState));

        } else {
            bundle.putSerializable(SellOrderEditFragment.BUNDLE_KEY_SELL_ORDER, sellOrder);
            bundle.putBoolean(SellOrderEditFragment.BUNDLE_KEY_READ_ONLY, PAGE_STATE_VIEW.equals(pageState));

            bundle2.putSerializable(SellProductEditFragment.BUNDLE_KEY_PRODUCT_LIST, (Serializable) sellOrder.getSellProductList());
            bundle2.putBoolean(SellProductEditFragment.BUNDLE_KEY_READ_ONLY, PAGE_STATE_VIEW.equals(pageState));
        }
        orderFragment.setArguments(bundle);
        productFragment.setArguments(bundle2);

        paperItemList = new ArrayList<>();
        paperItemList.add(new PagerItem("订单信息", orderFragment));
        paperItemList.add(new PagerItem("产品信息", productFragment));

        productFragment.setEvent(new SellProductEditFragment.SellProductEditEvent() {

            @Override
            public void onPriceChange() {
                calcOrderAmount();
            }

            @Override
            public void onNumberChange() {
                calcOrderAmount();
            }
        });
    }

    private void initPagerAdapter() {
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return paperItemList.size();
            }

            @Override
            public Fragment getItem(int i) {
                return paperItemList.get(i).view;
            }
        };
    }

    /**
     * 初始化标题栏
     */
    private void initTopBar() {
        mTopBar.setTitle("销售订单信息");
    }

    private void initTabSegment() {
        mViewPager.setAdapter(mPagerAdapter);
        for (PagerItem pi : paperItemList) {
            mTabSegment.addTab(new QMUITabSegment.Tab(pi.title));
        }

        mTabSegment.setupWithViewPager(mViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        // 显示下划线
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorPosition(false);
        mTabSegment.setIndicatorWidthAdjustContent(true);
    }

    private class PagerItem {
        private String title;
        private Fragment view;

        private PagerItem(String title, Fragment fragment) {
            this.title = title;
            this.view = fragment;
        }
    }

    /**
     * 初始化按钮
     */
    private void initButton() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPageState(PAGE_STATE_EDIT);
                setButtonVisibility();
                orderFragment.setReadOnly(false);
                productFragment.setReadOnly(false);
            }
        });

        concelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPageState(PAGE_STATE_VIEW);
                setButtonVisibility();
                orderFragment.setReadOnly(true);
                orderFragment.setData(sellOrder);
                productFragment.setReadOnly(true);
                productFragment.setData(sellOrder.getSellProductList());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.MessageDialogBuilder(mContext).setTitle("标题").setMessage("确定要发送吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                Map<String, String> paramMap = new HashMap<>();
                                paramMap.put("sellId", AppUtil.getString(orderId));
                                OkHttpUtil.doPost(ConfigUtil.SYS_SERVICE_DELETE_SELL_ORDER, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response, String responeText) {
                                        JSONObject json = JSON.parseObject(responeText);
                                        if (!isLoadDataSuccess(json)) {
                                            Looper.prepare();
                                            ToastUtil.makeText(mContext, getResultMessage(json));
                                            Looper.loop();
                                            return;
                                        }
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.makeText(mContext, "删除成功");
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .create(mCurrentDialogStyle).show();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellOrderBean orderBean = orderFragment.getData();
                if (orderBean == null) {
                    ToastUtil.makeText(mContext, "订单信息为空");
                    return;
                }
                List<SellProductBean> productBeanList = productFragment.getData();
                if (productBeanList == null || productBeanList.isEmpty()) {
                    ToastUtil.makeText(mContext, "请添加产品信息");
                    return;
                }

                String postUrl = ConfigUtil.SYS_SERVICE_ADD_SELL_ORDER;
                Map<String, String> paramMap = new HashMap<>();
                if (orderBean.getId() != null) {
                    paramMap.put("id", AppUtil.getString(orderBean.getId()));
                    postUrl = ConfigUtil.SYS_SERVICE_UPDATE_SELL_ORDER;
                }
                paramMap.put("orderNumber", AppUtil.getString(orderBean.getOrderNumber()));
                paramMap.put("orderAmount", AppUtil.getString(orderBean.getOrderAmount()));
                paramMap.put("userId", AppUtil.getString(orderBean.getUserId()));
                paramMap.put("userName", AppUtil.getString(orderBean.getUserName()));
                paramMap.put("orderTime", DateUtil.parseDateToString(orderBean.getOrderTime()));
                paramMap.put("hasPayAmount", AppUtil.getString(orderBean.getHasPayAmount()));
                paramMap.put("payTime", DateUtil.parseDateToString(orderBean.getPayTime()));
                paramMap.put("remark", AppUtil.getString(orderBean.getRemark()));

                for (int i = 0; i < productBeanList.size(); i++) {
                    SellProductBean bean = productBeanList.get(i);
                    paramMap.put("sellProductList[" + i + "].id", AppUtil.getString(bean.getId()));
                    paramMap.put("sellProductList[" + i + "].productId", AppUtil.getString(bean.getProductId()));
                    paramMap.put("sellProductList[" + i + "].productTitle", AppUtil.getString(bean.getProductTitle()));
                    paramMap.put("sellProductList[" + i + "].productUnit", AppUtil.getString(bean.getProductUnit()));
                    paramMap.put("sellProductList[" + i + "].stockPrice", AppUtil.getString(bean.getStockPrice()));
                    paramMap.put("sellProductList[" + i + "].number", AppUtil.getString(bean.getNumber()));
                    paramMap.put("sellProductList[" + i + "].price", AppUtil.getString(bean.getPrice()));
                }

                OkHttpUtil.doPost(postUrl, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response, String responeText) {
                        JSONObject json = JSON.parseObject(responeText);
                        if (!isLoadDataSuccess(json)) {
                            Looper.prepare();
                            ToastUtil.makeText(mContext, getResultMessage(json));
                            Looper.loop();
                            return;
                        }
                        sellOrder = json.getObject("result", SellOrderBean.class);
                        Log.d("SellOrderInfoActivity", "load sellOrder success:" + sellOrder);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setPageState(PAGE_STATE_VIEW);
                                setButtonVisibility();
                                orderFragment.setReadOnly(true);
                                orderFragment.setData(sellOrder);
                                productFragment.setReadOnly(true);
                                productFragment.setData(sellOrder.getSellProductList());
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 静态内部类-处理页面跳转
     */
    private static class InitTapHandler extends Handler {
        WeakReference<SellOrderInfoActivity> weakReference;

        private InitTapHandler(SellOrderInfoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            weakReference.get().initPaperItemList();
            weakReference.get().initPagerAdapter();
            weakReference.get().initTabSegment();
            super.handleMessage(msg);
        }
    }

    /**
     * 设置页面状态
     * @param pageState
     */
    private void setPageState(String pageState) {
        this.pageState = pageState;
    }

    /**
     * 设置按钮显隐
     */
    private void setButtonVisibility() {
        if (PAGE_STATE_ADD.equals(pageState)) {
            editBtn.setVisibility(View.GONE);
            concelBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
        } else if (PAGE_STATE_VIEW.equals(pageState)) {
            editBtn.setVisibility(View.VISIBLE);
            concelBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
        } else if (PAGE_STATE_EDIT.equals(pageState)) {
            editBtn.setVisibility(View.GONE);
            concelBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            editBtn.setVisibility(View.GONE);
            concelBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 计算订单金额
     */
    private void calcOrderAmount() {
        List<SellProductBean> beans = productFragment.getData();
        if (beans == null || beans.isEmpty()) {
            return;
        }
        BigDecimal orderAmount = new BigDecimal("0.00");
        for (SellProductBean bean : beans) {
            if (bean.getNumber() != null && bean.getPrice() != null) {
                orderAmount = orderAmount.add(BigDecimal.valueOf(bean.getNumber()).multiply(BigDecimal.valueOf(bean.getPrice())));
            }
        }
        orderFragment.setOrderAmount(orderAmount);
    }
}
