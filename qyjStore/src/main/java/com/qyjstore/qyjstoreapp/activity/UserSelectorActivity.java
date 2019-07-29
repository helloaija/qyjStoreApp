package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.qyjstore.qyjstoreapp.base.BaseActivity;
import com.qyjstore.qyjstoreapp.bean.UserBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
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
 * @Description 用户选择器页面
 * @date 2019-06-13
 */
public class UserSelectorActivity extends BaseActivity {
    private Context mContext;
    /** 查询框 */
    private EditText queryEt;
    /** 列表 */
    private XRecyclerView recyclerView;
    /** 用户数据 */
    private List<UserBean> itemList = new ArrayList<>();
    private UserSelectorItemAdapter adapter;
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    private Handler handler;
    private Runnable runnable;

    /** 表单组件 */
    private Button addUserBtn;
    private EditText userNameEt;
    private EditText mobilePhoneEt;
    private EditText addressEt;
    private EditText remarkEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_user_selector);
        queryEt = findViewById(R.id.activity_user_selector_queryEt);
        recyclerView = findViewById(R.id.activity_user_selector_xrv);
        addUserBtn = findViewById(R.id.activity_user_selector_userFormSubmitBtn);
        userNameEt = findViewById(R.id.activity_user_selector_userName);
        mobilePhoneEt = findViewById(R.id.activity_user_selector_mobilePhone);
        addressEt = findViewById(R.id.activity_user_selector_address);
        remarkEt = findViewById(R.id.activity_user_selector_remark);

        adapter = new UserSelectorItemAdapter(mContext, this.itemList);
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

        // 添加用户按钮
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEt.getText().toString().trim();
                String mobilePhone = mobilePhoneEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String remark = remarkEt.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.makeText(mContext, "用户名不能为空");
                    return;
                }

                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("userName", userName);
                paramMap.put("mobilePhone", mobilePhone);
                paramMap.put("address", address);
                paramMap.put("remark", remark);

                OkHttpUtil.doPost(ConfigUtil.SYS_SERVICE_INSERT_USER, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
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

                        final UserBean resultBean = json.getObject("result", UserBean.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setSelectResult(resultBean);
                                finish();
                            }
                        });
                    }
                });
            }
        });
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
        paramMap.put("userName", queryEt.getText().toString());
        paramMap.put("currentPage", String.valueOf(pageIndex));

        OkHttpUtil.doGet(ConfigUtil.SYS_SERVICE_LIST_USER, paramMap, new OkHttpUtil.HttpCallBack(mContext) {
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
                List<UserBean> userBeanList = JSON.parseArray(recordListStr, UserBean.class);
                itemList.addAll(userBeanList);
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

    class UserSelectorItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<UserBean> itemList;
        private Context meContext;

        public UserSelectorItemAdapter(Context meContext, List<UserBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_user_selector, parent, false);

            return new HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final UserBean bean = itemList.get(position);
            ((HoldView) viewHolder).userNameTv.setText(bean.getUserName());
            ((HoldView) viewHolder).mobilePhoneTv.setText(bean.getMobilePhone());

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
            private TextView userNameTv;
            private TextView mobilePhoneTv;

            private HoldView(View itemView) {
                super(itemView);
                // 用户名
                userNameTv = itemView.findViewById(R.id.item_user_selector_userName);
                // 联系电话
                mobilePhoneTv = itemView.findViewById(R.id.item_user_selector_mobilePhone);
            }
        }
    }

    /**
     * 回传选择的用户
     * @param userBean
     */
    private void setSelectResult(UserBean userBean) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(ConstantUtil.BUNDLE_KEY_SELECTED_USER, JSON.toJSONString(userBean));
            intent.putExtras(bundle);
        }
        setResult(0, intent);
    }

}
