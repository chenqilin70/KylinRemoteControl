package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Intent;
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
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        text_icon=findViewById(R.id.text_icon);
        text_icon.setAnimation(animation);


    }
}
