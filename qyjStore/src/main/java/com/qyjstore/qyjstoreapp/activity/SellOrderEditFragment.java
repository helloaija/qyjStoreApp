package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.SellOrderBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.DateUtil;

import java.util.Calendar;

/**
 * @Author shitl
 * @Description 销售单编辑-订单
 * @date 2019-06-06
 */
public class SellOrderEditFragment extends Fragment {
    public static String BUNDLE_KEY_SELL_ORDER = "sellOrder";
    public static String BUNDLE_KEY_READ_ONLY = "readOnly";
    private SellOrderBean sellOrder;
    private boolean reayOnly = true;

    private TextView orderNumberTv;
    private TextView orderAmountTv;
    private EditText userNameAtv;
    private EditText orderTimeTv;
    private EditText payAmountTv;
    private EditText payTimeTv;

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
            if (bundle.containsKey(BUNDLE_KEY_SELL_ORDER) && bundle.getSerializable(BUNDLE_KEY_SELL_ORDER) != null) {
                sellOrder = (SellOrderBean) bundle.getSerializable(BUNDLE_KEY_SELL_ORDER);
            }
            if (bundle.containsKey(BUNDLE_KEY_READ_ONLY)) {
                reayOnly = bundle.getBoolean(BUNDLE_KEY_READ_ONLY);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_order_edit, container, false);

        orderNumberTv = view.findViewById(R.id.fragment_sell_order_edit_orderNumber);
        orderAmountTv = view.findViewById(R.id.fragment_sell_order_edit_orderAmount);
        userNameAtv = view.findViewById(R.id.fragment_sell_order_edit_userName);
        orderTimeTv = view.findViewById(R.id.fragment_sell_order_edit_orderTime);
        payAmountTv = view.findViewById(R.id.fragment_sell_order_edit_payAmount);
        payTimeTv = view.findViewById(R.id.fragment_sell_order_edit_payTime);
        datePicker = view.findViewById(R.id.fragment_sell_order_edit_datePicker);
        datePickConcelBtn = view.findViewById(R.id.fragment_sell_order_edit_concelDatePick);
        datePickSureBtn = view.findViewById(R.id.fragment_sell_order_edit_sureDatePick);
        datePickGroup = view.findViewById(R.id.fragment_sell_order_edit_datePickGroup);
        emptyView = view.findViewById(R.id.fragment_sell_order_edit_emptyView);

        setData(sellOrder);

        initChildView();

        return view;
    }

    /**
     * 初始化组件
     */
    private void initChildView() {
        orderTimeTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentDatePickView = orderTimeTv;
                    datePickGroup.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    v.clearFocus();
                }
            }
        });

        payTimeTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentDatePickView = payTimeTv;
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
        this.sellOrder = sellOrder;
        if (sellOrder != null) {
            orderNumberTv.setText(AppUtil.getString(sellOrder.getOrderNumber()));
            orderAmountTv.setText(AppUtil.getString(sellOrder.getOrderAmount()));
            userNameAtv.setText(AppUtil.getString(sellOrder.getUserName()));
            orderTimeTv.setText(DateUtil.parseDateToString(sellOrder.getOrderTime(), DateUtil.DATE_FORMAT_DATE_CH));
            payAmountTv.setText(AppUtil.getString(sellOrder.getHasPayAmount()));
            payTimeTv.setText(DateUtil.parseDateToString(sellOrder.getPayTime(), DateUtil.DATE_FORMAT_DATE_CH));
        }
    }

    /**
     * 设置只读
     * @param flag
     */
    public void setReadOnly(boolean flag) {
        reayOnly = flag;
        if (flag) {
            userNameAtv.setInputType(InputType.TYPE_NULL);
            orderTimeTv.setInputType(InputType.TYPE_NULL);
            payAmountTv.setInputType(InputType.TYPE_NULL);
            payTimeTv.setInputType(InputType.TYPE_NULL);
        } else {
            userNameAtv.setInputType(InputType.TYPE_CLASS_TEXT);
            orderTimeTv.setInputType(InputType.TYPE_CLASS_TEXT);
            payAmountTv.setInputType(InputType.TYPE_CLASS_TEXT);
            payTimeTv.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

}
