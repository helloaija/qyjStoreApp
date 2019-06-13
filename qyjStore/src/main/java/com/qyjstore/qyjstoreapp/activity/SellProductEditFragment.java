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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qyjstore.qyjstoreapp.R;
import com.qyjstore.qyjstoreapp.bean.ProductBean;
import com.qyjstore.qyjstoreapp.bean.SellProductBean;
import com.qyjstore.qyjstoreapp.bean.UserBean;
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
public class SellProductEditFragment extends Fragment {
    public static String BUNDLE_KEY_PRODUCT_LIST = "productList";
    public static String BUNDLE_KEY_READ_ONLY = "readOnly";
    private List<SellProductBean> sellProductList;
    private boolean reayOnly = true;

    private Context mContext;
    private QMUIFloatLayout floatLayout;
    private Button addProductBtn;
    private QMUIAnimationListView mListView;
    private List<SellProductBean> mDataList;

    /** 当前选择产品的组件 */
    private EditText currentProductSelectView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_KEY_PRODUCT_LIST) && bundle.getSerializable(BUNDLE_KEY_PRODUCT_LIST) != null) {
                sellProductList = new ArrayList<>((List<SellProductBean>) bundle.getSerializable(BUNDLE_KEY_PRODUCT_LIST));
            }

            if (bundle.containsKey(BUNDLE_KEY_READ_ONLY)) {
                reayOnly = bundle.getBoolean(BUNDLE_KEY_READ_ONLY);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        View view = inflater.inflate(R.layout.fragment_sell_product_edit, container, false);
        floatLayout = view.findViewById(R.id.fragment_sell_product_floatlayout);
        addProductBtn = view.findViewById(R.id.fragment_sell_product_addBtn);
        mListView = view.findViewById(R.id.fragment_sell_product_edit_lv);

        initAddProductBtn();
        initListView();

        this.setData(sellProductList);
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
                        mDataList.add(new SellProductBean());
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
            public View getView(int position, View convertView, ViewGroup parent) {
                final SellProductBean bean = mDataList.get(position);
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_sell_product_edit, parent, false);

                TextView productNameTv = view.findViewById(R.id.item_sell_pruduct_edit_productNameLabel);
                productNameTv.setText("产品" + (position + 1));

                // 产品
                final EditText productNameEt = view.findViewById(R.id.item_sell_pruduct_edit_productName);
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
                            bundle.putString(ProductSelectorActivity.BUNDLE_KEY_PAGE_TYPE, ProductSelectorActivity.PAGE_TYPE_SELL);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 0);
                            currentProductSelectView = productNameEt;
                            v.clearFocus();
                        }
                    }
                });

                // 进价
                EditText stockAmountEt = view.findViewById(R.id.item_sell_pruduct_edit_stockAmount);
                stockAmountEt.setText(AppUtil.getString(bean.getStockAmount()));
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
                            bean.setStockAmount(null);
                        } else {
                            bean.setStockAmount(Double.valueOf(AppUtil.getString(s)));
                        }
                    }
                });

                // 售价
                EditText sellAmountEt = view.findViewById(R.id.item_sell_pruduct_edit_sellAmount);
                sellAmountEt.setText(AppUtil.getString(bean.getSellAmount()));
                sellAmountEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(AppUtil.getString(s))) {
                            bean.setSellAmount(null);
                        } else {
                            bean.setSellAmount(Double.valueOf(AppUtil.getString(s)));
                        }
                    }
                });

                // 数量
                EditText numberEt = view.findViewById(R.id.item_sell_pruduct_edit_number);
                numberEt.setText(AppUtil.getString(bean.getSellAmount()));
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
                    }
                });

                Button removeBtn = view.findViewById(R.id.item_sell_pruduct_edit_removeBtn);
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListView.manipulate(new QMUIAnimationListView.Manipulator<BaseAdapter>() {
                            @Override
                            public void manipulate(BaseAdapter adapter) {
                                int position = mListView.getFirstVisiblePosition();
                                mDataList.remove(position);
                            }
                        });
                    }
                });

                if (reayOnly) {
                    productNameEt.setInputType(InputType.TYPE_NULL);
                    stockAmountEt.setInputType(InputType.TYPE_NULL);
                    sellAmountEt.setInputType(InputType.TYPE_NULL);
                    numberEt.setInputType(InputType.TYPE_NULL);
                    removeBtn.setVisibility(View.INVISIBLE);
                } else {
                    productNameEt.setInputType(InputType.TYPE_CLASS_TEXT);
                    stockAmountEt.setInputType(InputType.TYPE_CLASS_TEXT);
                    sellAmountEt.setInputType(InputType.TYPE_CLASS_TEXT);
                    numberEt.setInputType(InputType.TYPE_CLASS_TEXT);
                    removeBtn.setVisibility(View.VISIBLE);
                }

                return view;
            }
        });
    }

    /**
     * 设置数据
     * @param sellProductList
     */
    public void setData(List<SellProductBean> sellProductList) {
        mDataList.clear();
        if (sellProductList == null || sellProductList.isEmpty()) {
            return;
        }
        // 复制一份数据，避免改变原来对象
        List<SellProductBean> list = new ArrayList<>(sellProductList);
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
    public List<SellProductBean> getData() {
        return new ArrayList<>(mDataList);
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
        if (resultCode == 0) {
            Bundle bundle = data.getExtras();
            String selectedProductJsonString = bundle.getString(ConstantUtil.BUNDLE_KEY_SELECTED_PRODUCT);
            if (!TextUtils.isEmpty(selectedProductJsonString)) {
                JSONObject json = JSON.parseObject(selectedProductJsonString);
                ProductBean selectedProductBean = json.toJavaObject(ProductBean.class);
                currentProductSelectView.setText(selectedProductBean.getTitle());
            }
        }
    }
}
