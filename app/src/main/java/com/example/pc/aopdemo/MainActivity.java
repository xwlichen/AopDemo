package com.example.pc.aopdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.smart.aoplibrary.annotation.Permission;
import com.smart.aoplibrary.annotation.SingleClick;
import com.smart.aoplibrary.annotation.TimeCost;
import com.smart.aoplibrary.utils.LogUtils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 *
 */

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {


    TextView tv_click;

    @TimeCost
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_click = findViewById(R.id.tv_click);
        tv_click.setOnClickListener(this);
    }


    @SingleClick
    @Permission(values = {WRITE_EXTERNAL_STORAGE, RECORD_AUDIO})
    @Override
    public void onClick(View v) {
        LogUtils.i("operate");
        computeTime();
        Toast.makeText(MainActivity.this,"计算完成",Toast.LENGTH_LONG).show();
    }

    @TimeCost
    public void computeTime() {
        int a = 0;
        for (int i = 0; i < 10000000; i++) {
            a = i;
        }
    }
}
