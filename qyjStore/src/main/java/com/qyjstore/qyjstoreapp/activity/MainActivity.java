package com.qyjstore.qyjstoreapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.qyjstore.qyjstoreapp.R;

/**
 * 主界面
 * @author shitl
 */
public class MainActivity extends AppCompatActivity {

    private FragmentManager fManager;
    private Fragment mainSell, mainStock, mainStatistics;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fTransaction = fManager.beginTransaction();
            hideAllFragment(fTransaction);
            switch (item.getItemId()) {
                case R.id.navigation_sell:
                    if (mainSell == null) {
                        mainSell = new MainSellFragment();
                        fTransaction.add(R.id.main_content, mainSell);
                    } else {
                        fTransaction.show(mainSell);
                    }
                    fTransaction.commit();
                    return true;
                case R.id.navigation_stock:
                    if (mainStock == null) {
                        mainStock = new MainStockFragment();
                        fTransaction.add(R.id.main_content, mainStock);
                    } else {
                        fTransaction.show(mainStock);
                    }
                    fTransaction.commit();
                    return true;
                case R.id.navigation_statistics:
                    if (mainStatistics == null) {
                        mainStatistics = new MainStatisticsFragment();
                        fTransaction.add(R.id.main_content, mainStatistics);
                    } else {
                        fTransaction.show(mainStatistics);
                    }
                    fTransaction.commit();
                    return true;
                default:
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fManager = getSupportFragmentManager();
        findViewById(R.id.navigation_sell).performClick();
    }

    /**
     * 隐藏所有Fragment
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mainSell != null) {
            fragmentTransaction.hide(mainSell);
        }
        if (mainStock != null) {
            fragmentTransaction.hide(mainStock);
        }
        if (mainStatistics != null) {
            fragmentTransaction.hide(mainStatistics);
        }
    }

}
