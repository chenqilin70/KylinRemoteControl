package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.google.gson.reflect.TypeToken;
import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;
import com.huwl.oracle.kylinremotecontrol.util.BarUtils;
import com.huwl.oracle.kylinremotecontrol.util.MessageHandle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class OptionActivity extends Activity implements View.OnClickListener {
    private ImageView scanIcon;
    private ListView terminalsLV;
    private RelativeLayout noTerminalRL;
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
        noTerminalRL=findViewById(R.id.noTerminalRL);
        scanIcon.setOnClickListener(this);

        initListView();

    }

    private void initListView() {
        final List<Map<String,Object>> listItem=new ArrayList<>();
        String terStr=getIntent().getExtras().getString("otherTerminal");
        Type founderSetType = new TypeToken<HashSet<Terminal>>(){}.getType();
        HashSet<Terminal> set=MessageHandle.GSON.fromJson(terStr,founderSetType);
        if(set.size()!=0){
            for(Terminal ter:set){
                if(ter.getSystemType().toLowerCase().contains("window")){
                    Map<String,Object> item=new HashMap();
                    item.put("terminalNameTV",ter.getName());
                    item.put("terminalTypeTV",ter.getSystemType());
                    item.put("radioImg",R.drawable.radio_unselect);
                    item.put("selected",false);
                    item.put("id",ter.getId());
                    listItem.add(item);
                }
            }
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.terminal_item,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                new String[] {"terminalNameTV","terminalTypeTV","radioImg"},
                new int[] {R.id.terminalNameTV,R.id.terminalTypeTV,R.id.radioImg}
        );
        terminalsLV.setAdapter(mSimpleAdapter);
        terminalsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView radioImg=view.findViewById(R.id.radioImg);
                boolean flag= (boolean) listItem.get(i).get("selected");
                if(flag){
                    radioImg.setImageResource(R.drawable.radio_unselect);
                    listItem.get(i).put("selected",false);
                }else{
                    radioImg.setImageResource(R.drawable.radio_select);
                    listItem.get(i).put("seleted",true);
                }

            }
        });
        Toast.makeText(this,(listItem.size()>0)+"",Toast.LENGTH_SHORT).show();
        if(listItem.size()>0){
            terminalsLV.setVisibility(View.VISIBLE);
            noTerminalRL.setVisibility(View.GONE);
        }else{
            terminalsLV.setVisibility(View.GONE);
            noTerminalRL.setVisibility(View.VISIBLE);
        }
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
