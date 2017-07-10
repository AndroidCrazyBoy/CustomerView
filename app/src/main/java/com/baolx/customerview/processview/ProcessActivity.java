package com.baolx.customerview.processview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by 10312 on 2017/7/10.
 */

public class ProcessActivity extends AppCompatActivity {

    private ProcessView processView;

    private int process;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processView = new ProcessView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        params.setMargins(30, 30, 30, 0);
        processView.setLayoutParams(params);
        setContentView(processView);

        processView();
    }

    private void processView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        process += 1;
                        process = process > 100 ? 0 : process;
                        processView.setProcess(process);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
