package com.zlw.main.auxiliarydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zlw.main.auxiliarydemo.utils.AccessibilityServiceHelper;

/**
 * @author zhaolewei
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d(TAG, "MainActivity onCreate");

        Button mBt = findViewById(R.id.mBt);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "点击内容：Hello World");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean open = AccessibilityServiceHelper.isEnable(this.getApplicationContext(), EventService.class);
        Logger.w(TAG, "无障碍服务: %s", open);
        if (!open) {
            AccessibilityServiceHelper.goAccess(getApplicationContext());
        }
    }
}
