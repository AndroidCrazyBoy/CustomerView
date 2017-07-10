package com.baolx.customerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baolx.customerview.bottombar.BottomBarActivity;
import com.baolx.customerview.customer_switch.CustomerSwitchActivity;
import com.baolx.customerview.processview.ProcessActivity;
import com.baolx.customerview.slide_item.SlideItemActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button custom_switch, custom_slide, bottom_bar, process_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView(){
        custom_switch = (Button) findViewById(R.id.custom_switch);
        custom_slide = (Button) findViewById(R.id.custom_slide);
        bottom_bar = (Button) findViewById(R.id.custom_bottombar);
        process_view = (Button) findViewById(R.id.custom_process);
    }

    private void initEvent(){
        custom_switch.setOnClickListener(this);
        custom_slide.setOnClickListener(this);
        bottom_bar.setOnClickListener(this);
        process_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.custom_switch:
                intent = new Intent(MainActivity.this, CustomerSwitchActivity.class);
                break;
            case R.id.custom_slide:
                intent = new Intent(MainActivity.this, SlideItemActivity.class);
                break;
            case R.id.custom_bottombar:
                intent = new Intent(MainActivity.this, BottomBarActivity.class);
                break;
            case R.id.custom_process:
                intent = new Intent(MainActivity.this, ProcessActivity.class);
                break;
        }

        startActivity(intent);
    }
}
