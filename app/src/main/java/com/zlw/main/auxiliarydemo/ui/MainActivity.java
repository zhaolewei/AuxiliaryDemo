package com.zlw.main.auxiliarydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zlw.main.auxiliarydemo.R;
import com.zlw.main.auxiliarydemo.service.EventService;
import com.zlw.main.auxiliarydemo.service.ServiceControlEvent;
import com.zlw.main.auxiliarydemo.utils.AccessibilityServiceHelper;
import com.zlw.main.auxiliarydemo.utils.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhaolewei
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.mTv)
    TextView mTv;
    @BindView(R.id.btnTestClick)
    Button btnTestClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Logger.d(TAG, "MainActivity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean open = AccessibilityServiceHelper.isEnable(this.getApplicationContext(), EventService.class);
        mTv.setText(String.format("无障碍服务: %s", open));
        btnTestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d(TAG, "点击成功！！！");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btnCheckService, R.id.btnClick, R.id.btnTestClick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCheckService:
                Logger.d(TAG, "点击内容：检测并开启无障碍服务");
                boolean open = AccessibilityServiceHelper.isEnable(this.getApplicationContext(), EventService.class);
                if (!open) {
                    AccessibilityServiceHelper.goAccess(getApplicationContext());
                }
                break;
            case R.id.btnClick:
                Logger.d(TAG, "开始模拟点击");
                ServiceControlEvent event = new ServiceControlEvent(ServiceControlEvent.ControlType.TEST_CLICK).setParam(btnTestClick.getText().toString());
                EventBus.getDefault().post(event);
                break;
            default:
                break;
        }
    }
}
