package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.huwl.oracle.kylinremotecontrol.R;

public class MainActivity extends Activity {
    private Animation animation;
    private ImageView text_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation=AnimationUtils.loadAnimation(this,R.anim.anim);
        text_icon=findViewById(R.id.text_icon);
        text_icon.setAnimation(animation);

    }
}
