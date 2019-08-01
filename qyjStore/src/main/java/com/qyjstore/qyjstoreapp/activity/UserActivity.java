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
import com.qyjstore.qyjstoreapp.bean.UserBean;
import com.qyjstore.qyjstoreapp.utils.ConfigUtil;
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
 * @Description 购买用户列表页面
 * @date 2019-07-30
 */
public class UserActivity extends BaseActivity {
    private Context mContext;

    /** 顶部标题栏 */
    private QMUITopBarLayout mTopBar;
    /** 查询框 */
    private EditText queryEt;
    /** 列表 */
    private XRecyclerView mRecyclerView;
    private UserActivity.UserItemAdapter adapter;
    /** 当前页 */
    private int pageIndex = 1;
    /** 总页数 */
    private int pageCount = 1;
    /** 销售单数据 */
    private List<UserBean> itemList = new ArrayList<>();
    private Handler loadItemHandler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
        setContentView(R.layout.activity_user);

        mTopBar = findViewById(R.id.activity_user_topbar);
        queryEt = findViewById(R.id.activity_user_queryEt);
        mRecyclerView = findViewById(R.id.activity_user_userRecyclerView);

        adapter = new UserActivity.UserItemAdapter(mContext, this.itemList);

        initTopBar();

        initQueryEt();

        // 初始化列表
        initRecyclerView();

        // 初始化加载数据线程
        initRunnable();

        // 加载数据
        loadUserData(true);
    }

    private void initTopBar() {
        mTopBar.setTitle("购买人列表");
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
                loadUserData(true);
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
                loadUserData(true);
            }

            @Override
            public void onLoadMore() {
                loadUserData(false);
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
    private void loadUserData(boolean isReload) {
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
                    loadItemHandler.post(runnable);
                    return;
                }

                List<UserBean> userBeanList = json.getJSONObject("result").getJSONArray("recordList").toJavaList(UserBean.class);
                itemList.addAll(userBeanList);
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

    class UserItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<UserBean> itemList;
        private Context meContext;

        public UserItemAdapter(Context meContext, List<UserBean> itemList) {
            this.meContext = meContext;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(meContext).inflate(R.layout.item_user, parent, false);

            return new UserActivity.UserItemAdapter.HoldView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final UserBean bean = itemList.get(position);
            ((UserActivity.UserItemAdapter.HoldView) viewHolder).userNameTv.setText(bean.getUserName());
            ((UserActivity.UserItemAdapter.HoldView) viewHolder).mobilePhoneTv.setText(bean.getMobilePhone());

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
            private TextView userNameTv;
            private TextView mobilePhoneTv;

            private HoldView(View itemView) {
                super(itemView);
                // 产品名称
                userNameTv = itemView.findViewById(R.id.item_user_userName);
                // 库存数量
                mobilePhoneTv = itemView.findViewById(R.id.item_user_mobilePhone);
            }
        }
    }
}
