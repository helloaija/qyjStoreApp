package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.base.BaseActivity;

/**
 * @Author shitl
 * @Description 销售单详情
 * @date 2019-05-31
 */
public class SellOrderInfoActivity extends BaseActivity {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;
        setContentView(R.layout.activity_sell_order_info);
    }
}
