package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.util.BarUtils;

public class OptionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        BarUtils.setWindowBarColor(this,R.color.colorBack,BarUtils.STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4以上
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
