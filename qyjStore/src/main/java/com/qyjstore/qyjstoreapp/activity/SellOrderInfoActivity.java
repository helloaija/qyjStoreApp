package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 销售单详情
 * @date 2019-05-31
 */
public class SellOrderInfoActivity extends BaseActivity {
    private Context mContext;
    /** 顶部标题栏 */
    private QMUITopBarLayout mTopBar;
    /** 标签 */
    private QMUITabSegment mTabSegment;
    private ViewPager mViewPager;
    /** 订单编辑碎片 */
    private SellOrderEditFragment orderFragment;
    /** 产品编辑碎片 */
    private SellProductEditFragment productFragment;

    private List<PagerItem> paperItemList;

    private PagerAdapter mPagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
        setContentView(R.layout.activity_sell_order_info);

        this.mTopBar = findViewById(R.id.activity_sell_order_info_topbar);
        this.mTabSegment = findViewById(R.id.activity_sell_order_info_tabSegment);
        this.mViewPager = findViewById(R.id.activity_sell_order_info_viewPager);

        initPaperItemList();
        initPagerAdapter();
        initTopBar();
        initTabSegment();
    }

    private void initPaperItemList() {
        orderFragment = new SellOrderEditFragment();
        productFragment = new SellProductEditFragment();

        paperItemList = new ArrayList<>();
        paperItemList.add(new PagerItem("订单信息", orderFragment));
        paperItemList.add(new PagerItem("产品信息", productFragment));

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


}
