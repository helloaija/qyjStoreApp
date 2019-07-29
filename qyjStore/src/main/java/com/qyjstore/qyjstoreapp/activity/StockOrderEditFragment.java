package com.qyjstore.qyjstoreapp.activity;

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
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.StockOrderBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @Author shitl
 * @Description 销售单编辑-订单
 * @date 2019-06-06
 */
public class StockOrderEditFragment extends Fragment {
    public static String BUNDLE_KEY_STOCK_ORDER = "stockOrder";
    public static String BUNDLE_KEY_READ_ONLY = "readOnly";
    /** 起始订单信息，新增就为空，编辑就为编辑的订单 */
    private StockOrderBean originalStockOrder;
    private boolean reayOnly = true;

    private EditText orderNumberEt;
    private EditText orderAmountEt;
    private EditText orderTimeEt;
    private EditText payAmountEt;
    private EditText payTimeEt;
    private EditText supplierNameEt;
    private EditText supplierPhoneEt;
    private EditText supplierAddressEt;
    private EditText supplierMessageEt;
    private EditText remarkEt;

    private DatePicker datePicker;
    private Button datePickConcelBtn;
    private Button datePickSureBtn;
    private Group datePickGroup;

    /** 当前时间选择的编辑框，时间选择后写着这个编辑框 */
    private EditText currentDatePickView;
    private QMUIEmptyView emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_KEY_STOCK_ORDER) && bundle.getSerializable(BUNDLE_KEY_STOCK_ORDER) != null) {
                originalStockOrder = (StockOrderBean) bundle.getSerializable(BUNDLE_KEY_STOCK_ORDER);
            }
            if (bundle.containsKey(BUNDLE_KEY_READ_ONLY)) {
                reayOnly = bundle.getBoolean(BUNDLE_KEY_READ_ONLY);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_order_edit, container, false);

        orderNumberEt = view.findViewById(R.id.fragment_stock_order_edit_orderNumber);
        orderAmountEt = view.findViewById(R.id.fragment_stock_order_edit_orderAmount);
        orderTimeEt = view.findViewById(R.id.fragment_stock_order_edit_orderTime);
        payAmountEt = view.findViewById(R.id.fragment_stock_order_edit_payAmount);
        payTimeEt = view.findViewById(R.id.fragment_stock_order_edit_payTime);
        supplierNameEt = view.findViewById(R.id.fragment_stock_order_edit_supplierName);
        supplierPhoneEt = view.findViewById(R.id.fragment_stock_order_edit_supplierPhone);
        supplierAddressEt = view.findViewById(R.id.fragment_stock_order_edit_supplierAddress);
        supplierMessageEt = view.findViewById(R.id.fragment_stock_order_edit_supplierMessage);
        remarkEt = view.findViewById(R.id.fragment_stock_order_edit_remark);
        datePicker = view.findViewById(R.id.fragment_stock_order_edit_datePicker);
        datePickConcelBtn = view.findViewById(R.id.fragment_stock_order_edit_concelDatePick);
        datePickSureBtn = view.findViewById(R.id.fragment_stock_order_edit_sureDatePick);
        datePickGroup = view.findViewById(R.id.fragment_stock_order_edit_datePickGroup);
        emptyView = view.findViewById(R.id.fragment_stock_order_edit_emptyView);

        setData(originalStockOrder);

        initChildView();

        setReadOnly(reayOnly);

        return view;
    }

    /**
     * 初始化组件
     */
    private void initChildView() {
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
                    currentDatePickView.setText(datePicker.getYear() + "年" + (datePicker.getMonth() + 1) + "月" + datePicker.getDayOfMonth() + "日");
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
     * @param stockOrder
     */
    public void setData(StockOrderBean stockOrder) {
        this.originalStockOrder = stockOrder;
        if (stockOrder != null) {
            orderNumberEt.setText(AppUtil.getString(stockOrder.getOrderNumber()));
            orderAmountEt.setText(AppUtil.getString(stockOrder.getOrderAmount()));
            orderTimeEt.setText(DateUtil.parseDateToString(stockOrder.getOrderTime(), DateUtil.DATE_FORMAT_DATE_CH));
            payAmountEt.setText(AppUtil.getString(stockOrder.getHasPayAmount()));
            payTimeEt.setText(DateUtil.parseDateToString(stockOrder.getPayTime(), DateUtil.DATE_FORMAT_DATE_CH));
            supplierNameEt.setText(AppUtil.getString(stockOrder.getSupplierName()));
            supplierPhoneEt.setText(AppUtil.getString(stockOrder.getSupplierPhone()));
            supplierAddressEt.setText(AppUtil.getString(stockOrder.getSupplierAddress()));
            supplierMessageEt.setText(AppUtil.getString(stockOrder.getSupplierMessage()));
            remarkEt.setText(stockOrder.getRemark());
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
        orderTimeEt.setEnabled(!flag);
        payTimeEt.setEnabled(!flag);
        supplierNameEt.setEnabled(!flag);
        supplierPhoneEt.setEnabled(!flag);
        supplierAddressEt.setEnabled(!flag);
        supplierMessageEt.setEnabled(!flag);
    }

    /**
     * 获取编辑的数据
     * @return
     */
    public StockOrderBean getData() {
        StockOrderBean data = new StockOrderBean();
        if (originalStockOrder != null) {
            data.setId(originalStockOrder.getId());
        }
        data.setOrderNumber(orderNumberEt.getText().toString());

        String orderAmount = orderAmountEt.getText().toString();
        data.setOrderAmount(TextUtils.isEmpty(orderAmount) ? null : new BigDecimal(orderAmount));
        data.setOrderTime(DateUtil.parseStringToDate(orderTimeEt.getText().toString(), DateUtil.DATE_FORMAT_DATE_CH));
        String hasPayAmount = payAmountEt.getText().toString();
        data.setHasPayAmount(TextUtils.isEmpty(hasPayAmount) ? new BigDecimal(0.00) : new BigDecimal(hasPayAmount));
        data.setPayTime(DateUtil.parseStringToDate(payTimeEt.getText().toString(), DateUtil.DATE_FORMAT_DATE_CH));
        data.setRemark(remarkEt.getText().toString().trim());

        data.setSupplierName(supplierNameEt.getText().toString().trim());
        data.setSupplierPhone(supplierPhoneEt.getText().toString().trim());
        data.setSupplierAddress(supplierAddressEt.getText().toString().trim());
        data.setSupplierMessage(supplierMessageEt.getText().toString().trim());

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
