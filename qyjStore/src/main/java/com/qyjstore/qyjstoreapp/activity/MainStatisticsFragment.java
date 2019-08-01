package com.qyjstore.qyjstoreapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 主界面-统计
 * @date 2019-05-17
 */
public class MainStatisticsFragment extends Fragment {

    private Context mContext;
    private QMUIGroupListView mGroupListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_statistics, container, false);
        mContext = this.getContext();
        mGroupListView = view.findViewById(R.id.fragment_main_statistics_groupListView);

        initGroupListView();

        return view;
    }

    private void initGroupListView() {
        QMUICommonListItemView storeItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_store),
                "库存数据",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        storeItem.setOrientation(QMUICommonListItemView.VERTICAL);
        storeItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView personItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(mContext, R.mipmap.ic_person),
                "购买人",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        personItem.setOrientation(QMUICommonListItemView.VERTICAL);
        personItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        int size = QMUIDisplayHelper.dp2px(mContext, 35);
        QMUIGroupListView.newSection(getContext())
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(storeItem, new PageForwarListener(mContext, StoreActivity.class))
                .addItemView(personItem, new PageForwarListener(mContext, UserActivity.class))
                .addTo(mGroupListView);
    }

    class PageForwarListener implements View.OnClickListener {

        private Context context;
        private Class toClass;
        public PageForwarListener() {}


        public PageForwarListener(Context context, Class toClass) {
            this.context = context;
            this.toClass = toClass;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, toClass);
            startActivity(intent);
        }
    }
}
