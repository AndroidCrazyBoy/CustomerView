package com.baolx.customerview.customer_switch;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baolx.customerview.R;

/**
 * Created by BaoLongxiang on 2016/7/13.
 */
public class CustomerSwitchActivity extends AppCompatActivity {

    private SwitchView switchView;
    private Button do_switch;

    private boolean switch_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_view);
        switchView = (SwitchView) findViewById(R.id.switch_view);
        do_switch = (Button) findViewById(R.id.do_switch);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerSwitchActivity.this, switchView.isOpen() ? "开" : "关", Toast.LENGTH_SHORT).show();
            }
        });

        do_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_ = !switchView.isOpen();
                switchView.setSwitchState(switch_);
            }
        });
    }
}
