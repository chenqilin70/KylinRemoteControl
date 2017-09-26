package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;
import com.huwl.oracle.kylinremotecontrol.util.MessageHandle;

public class ValidateActivity extends Activity {
    private TextView terminalInfoTV;
    private Button agreeBtn,disagreeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        Bundle bundle=this.getIntent().getExtras();
        final NetMessage netMessage= (NetMessage) bundle.get("netMessage");
        Terminal terminal= (Terminal) netMessage.getMap().get("terminal");
        terminalInfoTV=findViewById(R.id.terminalInfoTV);
        agreeBtn=findViewById(R.id.agreeBtn);
        disagreeBtn=findViewById(R.id.disagreeBtn);
        terminalInfoTV.setText(getInfoStr(terminal));
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        netMessage.send(MessageHandle.getServer());
                        ValidateActivity.this.finish();
                    }
                }.start();
            }
        });
        disagreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateActivity.this.finish();
            }
        });
    }
    private String getInfoStr(Terminal terminal){
        StringBuffer sb=new StringBuffer("");
        sb.append("终端ID:"+terminal.getId()+"\r\n计算机名："+terminal.getName()+"\r\n系统："+terminal.getSystemType()+"\r\nIP地址："+terminal.getIp()+"\r\n");
        return sb.toString();
    }
}
