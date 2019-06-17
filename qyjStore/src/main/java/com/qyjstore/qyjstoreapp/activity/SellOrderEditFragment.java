package com.qyjstore.qyjstoreapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.bean.UserBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @Author shitl
 * @Description 销售单编辑-订单
 * @date 2019-06-06
 */
public class SellOrderEditFragment extends Fragment {
    public static String BUNDLE_KEY_SELL_ORDER = "sellOrder";
    public static String BUNDLE_KEY_READ_ONLY = "readOnly";
    /** 起始订单信息，新增就为空，编辑就为编辑的订单 */
    private SellOrderBean originalSellOrder;
    private boolean reayOnly = true;

    private EditText orderNumberEt;
    private EditText orderAmountEt;
    private EditText userNameEt;
    private EditText orderTimeEt;
    private EditText payAmountEt;
    private EditText payTimeEt;
    private EditText remarkEt;

    private DatePicker datePicker;
    private Button datePickConcelBtn;
    private Button datePickSureBtn;
    private Group datePickGroup;

    /** 当前选择的购买人id */
    private Long userId;
    /** 当前时间选择的编辑框，时间选择后写着这个编辑框 */
    private EditText currentDatePickView;
    private QMUIEmptyView emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_KEY_SELL_ORDER) && bundle.getSerializable(BUNDLE_KEY_SELL_ORDER) != null) {
                originalSellOrder = (SellOrderBean) bundle.getSerializable(BUNDLE_KEY_SELL_ORDER);
                userId = originalSellOrder.getUserId();
            }
            if (bundle.containsKey(BUNDLE_KEY_READ_ONLY)) {
                reayOnly = bundle.getBoolean(BUNDLE_KEY_READ_ONLY);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_order_edit, container, false);

        orderNumberEt = view.findViewById(R.id.fragment_sell_order_edit_orderNumber);
        orderAmountEt = view.findViewById(R.id.fragment_sell_order_edit_orderAmount);
        userNameEt = view.findViewById(R.id.fragment_sell_order_edit_userName);
        orderTimeEt = view.findViewById(R.id.fragment_sell_order_edit_orderTime);
        payAmountEt = view.findViewById(R.id.fragment_sell_order_edit_payAmount);
        payTimeEt = view.findViewById(R.id.fragment_sell_order_edit_payTime);
        remarkEt = view.findViewById(R.id.fragment_sell_order_edit_remark);
        datePicker = view.findViewById(R.id.fragment_sell_order_edit_datePicker);
        datePickConcelBtn = view.findViewById(R.id.fragment_sell_order_edit_concelDatePick);
        datePickSureBtn = view.findViewById(R.id.fragment_sell_order_edit_sureDatePick);
        datePickGroup = view.findViewById(R.id.fragment_sell_order_edit_datePickGroup);
        emptyView = view.findViewById(R.id.fragment_sell_order_edit_emptyView);

        setData(originalSellOrder);

        initChildView();

        setReadOnly(reayOnly);

        return view;
    }

    /**
     * 初始化组件
     */
    private void initChildView() {
        userNameEt.setInputType(InputType.TYPE_NULL);
        userNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!reayOnly && hasFocus) {
                    // 跳到用户选择页面
                    Intent intent = new Intent(getContext(), UserSelectorActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                    v.clearFocus();
                }
            }
        });

        orderTimeEt.setInputType(InputType.TYPE_NULL);
        orderTimeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!reayOnly && hasFocus) {
                    // 打开日期选择器
                    currentDatePickView = orderTimeEt;
                    datePickGroup.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    v.clearFocus();
                }
            }
        });

        payTimeEt.setInputType(InputType.TYPE_NULL);
        payTimeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!reayOnly && hasFocus) {
                    // 打开日期选择器
                    currentDatePickView = payTimeEt;
                    emptyView.setVisibility(View.VISIBLE);
                    datePickGroup.setVisibility(View.VISIBLE);
                    v.clearFocus();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    }
                });

        datePickConcelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyView.setVisibility(View.GONE);
                datePickGroup.setVisibility(View.GONE);
            }
        });

        datePickSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDatePickView != null) {
                    currentDatePickView.setText(datePicker.getYear() + "年" + datePicker.getMonth() + "月" + datePicker.getDayOfMonth() + "日");
                }
                datePickGroup.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }
        });

        emptyView.hide();
        emptyView.getBackground().setAlpha(50);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    /**
     * 设置数据
     * @param sellOrder
     */
    public void setData(SellOrderBean sellOrder) {
        this.originalSellOrder = sellOrder;
        if (sellOrder != null) {
            orderNumberEt.setText(AppUtil.getString(sellOrder.getOrderNumber()));
            orderAmountEt.setText(AppUtil.getString(sellOrder.getOrderAmount()));
            userNameEt.setText(AppUtil.getString(sellOrder.getUserName()));
            orderTimeEt.setText(DateUtil.parseDateToString(sellOrder.getOrderTime(), DateUtil.DATE_FORMAT_DATE_CH));
            payAmountEt.setText(AppUtil.getString(sellOrder.getHasPayAmount()));
            payTimeEt.setText(DateUtil.parseDateToString(sellOrder.getPayTime(), DateUtil.DATE_FORMAT_DATE_CH));
            remarkEt.setText(sellOrder.getRemark());
        }
    }

    /**
     * 设置只读
     * @param flag
     */
    public void setReadOnly(boolean flag) {
        reayOnly = flag;
        payAmountEt.setEnabled(!flag);
        remarkEt.setEnabled(!flag);
        userNameEt.setEnabled(!flag);
        orderTimeEt.setEnabled(!flag);
        payTimeEt.setEnabled(!flag);
    }

    /**
     * 接收用户选择返回结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && data != null) {
            Bundle bundle = data.getExtras();
            String selectedUserJsonString = bundle.getString(ConstantUtil.BUNDLE_KEY_SELECTED_USER);
            if (!TextUtils.isEmpty(selectedUserJsonString)) {
                UserBean selectedUserBean = JSON.parseObject(selectedUserJsonString, UserBean.class);
                userId = selectedUserBean.getId();
                userNameEt.setText(selectedUserBean.getUserName());
            }
        }
    }

    /**
     * 获取编辑的数据
     * @return
     */
    public SellOrderBean getData() {
        SellOrderBean data = new SellOrderBean();
        if (originalSellOrder != null) {
            data.setId(originalSellOrder.getId());
        }
        data.setUserId(userId);
        data.setOrderNumber(orderNumberEt.getText().toString());
        data.setOrderAmount(new BigDecimal(orderAmountEt.getText().toString()));
        data.setUserName(userNameEt.getText().toString().trim());
        data.setOrderTime(DateUtil.parseStringToDate(orderTimeEt.getText().toString(), DateUtil.DATE_FORMAT_DATE_CH));
        data.setHasPayAmount(new BigDecimal(payAmountEt.getText().toString()));
        data.setPayTime(DateUtil.parseStringToDate(payTimeEt.getText().toString(), DateUtil.DATE_FORMAT_DATE_CH));
        data.setRemark(remarkEt.getText().toString().trim());

        return data;
    }

    /**
     * 设置订单金额
     * @param orderAmount
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        orderAmountEt.setText(AppUtil.getString(orderAmount.setScale(2, BigDecimal.ROUND_DOWN)));
    }
}
