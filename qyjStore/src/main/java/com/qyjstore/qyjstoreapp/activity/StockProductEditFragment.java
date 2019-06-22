package com.qyjstore.qyjstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.ProductBean;
import com.qyjstore.qyjstoreapp.bean.StockProductBean;
import com.qyjstore.qyjstoreapp.bean.SpinnerItem;
import com.qyjstore.qyjstoreapp.bean.StockProductBean;
import com.qyjstore.qyjstoreapp.utils.AppUtil;
import com.qyjstore.qyjstoreapp.utils.ConstantUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author shitl
 * @Description 销售单编辑-产品
 * @date 2019-06-06
 */
public class StockProductEditFragment extends Fragment {
    public static String BUNDLE_KEY_PRODUCT_LIST = "productList";
    public static String BUNDLE_KEY_READ_ONLY = "readOnly";
    private List<StockProductBean> stockProductList;
    private boolean reayOnly = true;

    private Context mContext;
    private QMUIFloatLayout floatLayout;
    private Button addProductBtn;
    private QMUIAnimationListView mListView;
    private List<StockProductBean> mDataList;

    /** 当前选择产品的组件 */
    private View currentProductSelectView;
    /** 当前选择产品的对象 */
    private StockProductBean currentProductSelectBean;
    /** 事件，用来汇总订单总金额 */
    private StockProductEditEvent event;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_KEY_PRODUCT_LIST) && bundle.getSerializable(BUNDLE_KEY_PRODUCT_LIST) != null) {
                stockProductList = new ArrayList<>((List<StockProductBean>) bundle.getSerializable(BUNDLE_KEY_PRODUCT_LIST));
            }

            if (bundle.containsKey(BUNDLE_KEY_READ_ONLY)) {
                reayOnly = bundle.getBoolean(BUNDLE_KEY_READ_ONLY);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        View view = inflater.inflate(R.layout.fragment_stock_product_edit, container, false);
        floatLayout = view.findViewById(R.id.fragment_stock_product_floatlayout);
        addProductBtn = view.findViewById(R.id.fragment_stock_product_addBtn);
        mListView = view.findViewById(R.id.fragment_stock_product_edit_lv);

        initAddProductBtn();
        initListView();

        this.setData(stockProductList);
        this.setReadOnly(reayOnly);

        return view;
    }

    private void initAddProductBtn() {
        this.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListView.manipulate(new QMUIAnimationListView.Manipulator<BaseAdapter>() {
                    @Override
                    public void manipulate(BaseAdapter adapter) {
                        mDataList.add(new StockProductBean());
                    }
                });
            }
        });
    }

    private void initListView() {
        mDataList = new ArrayList<>();

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public Object getItem(int position) {
                return mDataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final StockProductBean bean = mDataList.get(position);
                final View view = LayoutInflater.from(mContext).inflate(R.layout.item_stock_product_edit, parent, false);

                TextView productNameTv = view.findViewById(R.id.item_stock_pruduct_edit_productNameLabel);
                productNameTv.setText("产品" + (position + 1));

                // 产品
                final EditText productNameEt = view.findViewById(R.id.item_stock_pruduct_edit_productName);
                productNameEt.setInputType(InputType.TYPE_NULL);
                productNameEt.setText(AppUtil.getString(bean.getProductTitle()));
                productNameEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        bean.setProductTitle(s.toString());
                    }
                });
                productNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!reayOnly && hasFocus) {
                            // 跳到产品选择页面
                            Intent intent = new Intent(getContext(), ProductSelectorActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(ProductSelectorActivity.BUNDLE_KEY_PAGE_TYPE, ProductSelectorActivity.PAGE_TYPE_STOCK);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 0);
                            currentProductSelectView = view;
                            currentProductSelectBean = bean;
                            v.clearFocus();
                        }
                    }
                });

                final TextView unitEt = view.findViewById(R.id.item_stock_pruduct_edit_unit);
                unitEt.setText(AppUtil.getString(bean.getProductUnit()));

                // 进价
                EditText stockAmountEt = view.findViewById(R.id.item_stock_pruduct_edit_stockAmount);
                stockAmountEt.setText(AppUtil.getString(bean.getPrice()));
                stockAmountEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(AppUtil.getString(s))) {
                            bean.setPrice(null);
                        } else {
                            bean.setPrice(new BigDecimal(AppUtil.getString(s)));
                        }
                        if (event != null) {
                            event.onPriceChange();
                        }
                    }
                });

                // 数量
                EditText numberEt = view.findViewById(R.id.item_stock_pruduct_edit_number);
                numberEt.setText(AppUtil.getString(bean.getNumber()));
                numberEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(AppUtil.getString(s))) {
                            bean.setNumber(null);
                        } else {
                            bean.setNumber(Double.valueOf(AppUtil.getString(s)).intValue());
                        }
                        if (event != null) {
                            event.onNumberChange();
                        }
                    }
                });

                Button removeBtn = view.findViewById(R.id.item_stock_pruduct_edit_removeBtn);
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListView.manipulate(new QMUIAnimationListView.Manipulator<BaseAdapter>() {
                            @Override
                            public void manipulate(BaseAdapter adapter) {
                                mDataList.remove(position);
                            }
                        });
                    }
                });

                productNameEt.setEnabled(!reayOnly);
                stockAmountEt.setEnabled(!reayOnly);
                stockAmountEt.setEnabled(!reayOnly);
                numberEt.setEnabled(!reayOnly);
                if (reayOnly) {
                    removeBtn.setVisibility(View.INVISIBLE);
                } else {
                    removeBtn.setVisibility(View.VISIBLE);
                }

                return view;
            }


        });
    }

    /**
     * 设置数据
     * @param stockProductList
     */
    public void setData(List<StockProductBean> stockProductList) {
        mDataList.clear();
        if (stockProductList == null || stockProductList.isEmpty()) {
            return;
        }
        // 复制一份数据，避免改变原来对象
        List<StockProductBean> list = null;
        try {
            list = cloneStockProductList(stockProductList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDataList.addAll(list);
    }

    /**
     * 设置只读
     * @param isReadOnly
     */
    public void setReadOnly(boolean isReadOnly) {
        this.reayOnly = isReadOnly;
        if (isReadOnly) {
            floatLayout.setVisibility(View.GONE);
        } else {
            floatLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取数据
     * @return
     */
    public List<StockProductBean> getData() {
        return new ArrayList<>(mDataList);
    }

    /**
     * 接收产品选择返回结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && data != null) {
            Bundle bundle = data.getExtras();
            String selectedProductJsonString = bundle.getString(ConstantUtil.BUNDLE_KEY_SELECTED_PRODUCT);
            if (!TextUtils.isEmpty(selectedProductJsonString)) {
                JSONObject json = JSON.parseObject(selectedProductJsonString);
                ProductBean selectedProductBean = json.toJavaObject(ProductBean.class);

                currentProductSelectBean.setProductId(selectedProductBean.getId());

                EditText productNameEt = currentProductSelectView.findViewById(R.id.item_stock_pruduct_edit_productName);
                productNameEt.setText(selectedProductBean.getTitle());
                TextView unitTv = currentProductSelectView.findViewById(R.id.item_stock_pruduct_edit_unit);
                unitTv.setText(selectedProductBean.getProductUnit());
                currentProductSelectBean.setProductUnit(selectedProductBean.getProductUnit());

                EditText stockAmountEt = currentProductSelectView.findViewById(R.id.item_stock_pruduct_edit_stockAmount);
                String[] stockPrices = selectedProductBean.getStockPrices();
                if (stockPrices != null && stockPrices.length > 0) {
                    stockAmountEt.setText(stockPrices[0]);
                }
            }
        }
    }

    /**
     * 事件接口
     */
    public interface StockProductEditEvent {
        void onPriceChange();

        void onNumberChange();
    }

    /**
     * 设置监听事件
     */
    public void setEvent(StockProductEditEvent event) {
        this.event = event;
    }

    /**
     * 复制
     * @param list
     * @return
     * @throws Exception
     */
    private List<StockProductBean> cloneStockProductList(List<StockProductBean> list) throws Exception {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        List<StockProductBean> cloneList = new ArrayList<>();
        for (StockProductBean bean : list) {
            StockProductBean cloneBean = (StockProductBean) bean.clone();
            cloneList.add(cloneBean);
        }
        return cloneList;
    }


}
