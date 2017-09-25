package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dtr.zxing.activity.CaptureActivity;
import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.util.BarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionActivity extends Activity implements View.OnClickListener {
    private ImageView scanIcon;
    private ListView terminalsLV;
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
        scanIcon=findViewById(R.id.scanIcon);
        terminalsLV=findViewById(R.id.terminalsLV);
        scanIcon.setOnClickListener(this);
        List<Map<String,String>> listItem=new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String,String> item=new HashMap();
            item.put("terminalNameTV","aierxuan"+i);
            item.put("terminalTypeTV","Windows 10"+"  "+i);
            listItem.add(item);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.terminal_item,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                new String[] {"terminalNameTV","terminalTypeTV"},
                new int[] {R.id.terminalNameTV,R.id.terminalTypeTV}
            );
        terminalsLV.setAdapter(mSimpleAdapter);

    }

    @Override
    protected void onDestroy() {
        Log.e("test","onDestroy is running……");
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    public void onClick(View view) {
        if(view==scanIcon){
            startActivity(new Intent(this,CaptureActivity.class));
        }
    }
}
