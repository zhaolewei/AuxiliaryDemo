package com.zlw.main.auxiliarydemo.service;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;

import com.zlw.main.auxiliarydemo.utils.AccessibilityServiceHelper;
import com.zlw.main.auxiliarydemo.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

/**
 * @author zhaolewei on 2017/11/23.
 *         相关文档: https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent.html
 */

public class EventService extends BaseAccessibilityService {
    private static final String TAG = EventService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "=====EventService======onCreate()================");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDistributeEvent(ServiceControlEvent event) {
        Logger.i(TAG, "收到事件： %s", event.what.name());
        switch (event.what) {
            case TEST_CLICK:
                if (TextUtils.isEmpty(event.param)) {
                    Logger.w(TAG, "text is null");
                    return;
                }
                clickTextViewByText(event.param);
                break;
            default:
                break;
        }
    }

    @Override
    public void onInterrupt() {
        Logger.e(TAG, "无障碍服务断开！！！");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Logger.d(TAG, "==========Start==========");
        int eventType = event.getEventType();
        String eventTypeName = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //界面改变
                eventTypeName = "TYPE_WINDOW_STATE_CHANGED";
                Logger.d(TAG, "Action: %s", AccessibilityServiceHelper.getActionName(event));
                AccessibilityServiceHelper.showView(getRootInActiveWindow());
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //界面点击
                eventTypeName = "TYPE_VIEW_CLICKED";
                Logger.d(TAG, "Action: %s", AccessibilityServiceHelper.getActionName(event));
                AccessibilityServiceHelper.showView(event.getSource());
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //界面文字改动
                eventTypeName = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventTypeName = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventTypeName = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventTypeName = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventTypeName = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventTypeName = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventTypeName = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventTypeName = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventTypeName = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventTypeName = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            default:
                eventTypeName = String.format(Locale.getDefault(), "未知事件：eventType: %d", eventType);
                break;
        }

        Logger.d(TAG, "------------%s---------------END.\n.", eventTypeName);
    }

    /**
     * 分发无障碍时间
     * type: 界面更改
     *
     * @param event
     */
    private void distributeActicityEvent(AccessibilityEvent event, String eventTypeName) {
        if (event.getClassName() == null || event.getPackageName() == null) {
            Logger.w(TAG, "getClassName or getPackageName is null");
            return;
        }
        //-----获取当前类名
    }

    /**
     * 收到界面进入信息
     *
     * @param componentName 类名 例：
     */
    private void distributeActivityEvent(AccessibilityEvent event, String componentName) {
        //-----获取界面信息
        Logger.w(TAG, "Activity: %s", componentName);
        AccessibilityNodeInfo source = event.getSource();
        Logger.i(TAG, "root: %s", source.getClassName());

        for (int i = 0; i < source.getChildCount(); i++) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (child == null) {
                return;
            }
            Logger.i(TAG, "---> %d . child  %s", i, child.getClassName());

            try {
                if (TextUtils.equals(TextView.class.getName(), child.getClassName())) {
                    Logger.i(TAG, ">>>>>text: %s", child.getText());
                }

                if (TextUtils.equals(Button.class.getName(), child.getClassName())) {
                    Logger.i(TAG, ">>>>>text: %s", child.getText());
                }
            } catch (Exception e) {
                Logger.e(e, TAG, e.getMessage());
            }
        }
        Logger.d(TAG, "-----------------");
    }


    private void getText(AccessibilityEvent event) {
        Logger.i(TAG, "------TYPE_VIEW_CLICKED------");
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        int childCount = rootInActiveWindow.getChildCount();
        Logger.i(TAG, "childCount: %s", childCount);
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = rootInActiveWindow.getChild(i);
            String text = (String) child.getText();
            if (!TextUtils.isEmpty(text)) {
                Logger.i(TAG, "%d text: %s", i, text);
            }
        }

        AccessibilityNodeInfo source = event.getSource();
        Rect rect = new Rect();
        if (source == null) {
            Logger.e(TAG, "source is null");
            return;
        }
        source.getBoundsInScreen(rect);
        String s = rect.toString();
        Logger.i(TAG, "位置: %s", s);
        Logger.i(TAG, "-------------------");
    }
}
