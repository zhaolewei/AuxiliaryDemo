package com.zlw.main.auxiliarydemo.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.zlw.main.auxiliarydemo.Logger;

import java.util.List;
import java.util.Locale;

/**
 * =======================无障碍服务工具类===========================
 * 1.前往开启辅助服务界面     @see {@link #goAccess}
 * 2.检查当前无障碍服务是否启用  @see {@link #isEnable}
 * 3.获取当前无障碍事件发生的Activity @see {@link #getActivityName}
 * 4.获取当前无障碍事件的发生控件类型  @see {@link #getActionName}
 * 5.遍历并打印View结构 @see {@link #showView}
 * 6.打开无障碍服务(系统权限)  @see {@link #showView}
 * 6.关闭所有的无障碍服务(系统权限) @see {@link #showView}
 * 6.打开无障碍服务(系统权限) @see {@link #showView}
 *
 *
 * @author zhaolewei on 2017/11/23.
 */
public class AccessibilityServiceHelper {

    private static final String TAG = AccessibilityServiceHelper.class.getSimpleName();

    /**
     * 前往开启辅助服务界面
     *
     * @param context Context
     */
    public static void goAccess(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检查当前无障碍服务是否启用
     *
     * @param context Context
     * @param clazz   AccessibilityService
     * @return true: 已启用
     */
    public static boolean isEnable(Context context, Class clazz) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null) {
            return false;
        }

        List<AccessibilityServiceInfo> serviceInfos = am.
                getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);

        for (AccessibilityServiceInfo info : serviceInfos) {
            if (info.getId().contains(context.getPackageName()) && info.getId().contains(clazz.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前Activity的名称（当不是Activity时 返回Null）
     *
     * @param event AccessibilityEvent
     * @return 当前Activity的名称
     */
    @Nullable
    public static String getActivityName(Context context, AccessibilityEvent event) {
        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(), event.getClassName().toString());
            ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(componentName, 0);
            if (activityInfo != null) {
                return componentName.flattenToShortString();
            }

        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 获取当前活动的名称
     *
     * @param event AccessibilityEvent
     * @return 当前活动的名称
     */
    public static String getActionName(AccessibilityEvent event) {
        ComponentName componentName = new ComponentName(
                event.getPackageName().toString(), event.getClassName().toString());

        return componentName.flattenToShortString();
    }

    /**
     * 遍历并打印View结构
     *
     * @param node 开始遍历的节点
     */
    public static void showView(AccessibilityNodeInfo node) {
        showView(node, 0, 0);
    }

    /**
     * 遍历View结构-广度优先遍历
     *
     * @param node 开始遍历的节点
     * @param deep 当前的深度
     */
    private static void showView(AccessibilityNodeInfo node, int deep, int index) {
        if (node == null || deep < 0) {
            Logger.e(TAG, "null");
            return;
        }

        String className = (String) node.getClassName();
        String text = null;
        if (TextUtils.equals(className, TextView.class.getName())
                || TextUtils.equals(className, Button.class.getName())) {
            text = String.format(Locale.getDefault(), "->> text: %s", (String) node.getText());
        }

        Logger.i(TAG, "%s   %s  %s", getTreeLogStr(deep, index), className, text == null ? "" : text);
        for (int i = 0; i < node.getChildCount(); i++) {
            showView(node.getChild(i), deep + 1, i);
        }
    }

    /**
     * 生成树形日志前缀
     *
     * @param deep  当前树的深度
     * @param index 当前说的
     * @return \t\t:
     */
    private static String getTreeLogStr(int deep, int index) {
        StringBuilder strLog = new StringBuilder();
        for (int i = 0; i < deep; i++) {
            strLog.append("   ");
        }
        strLog.append(index);
        strLog.append(": ");
        return strLog.toString();
    }

    /**
     * 界面树
     *
     * @deprecated 未完成
     */
    public static class ViewTree {
        public ViewTreeNode rootViewNode;
        private String showLog;
    }

    /**
     * 界面树节点
     *
     * @deprecated 未完成
     */
    public static class ViewTreeNode {
        public int deep;
        public String text;
        public String type;
        public List<ViewTreeNode> clildNodeList;


    }

    /**
     * 生成ViewTree树形结构
     *
     * @param rootNode
     * @deprecated 未完成
     */
    private ViewTree genViewTree(AccessibilityNodeInfo rootNode) {
        ViewTree viewTree = new ViewTree();
        getViewTree(viewTree.rootViewNode, rootNode, 0);
        return viewTree;
    }


    /**
     * 遍历View结构-广度优先遍历
     *
     * @param node 开始遍历的节点
     * @param deep 当前的深度
     * @deprecated 未完成
     */
    private void getViewTree(ViewTreeNode viewNode, AccessibilityNodeInfo node, int deep) {
        if (node == null || deep < 0) {
            Logger.e(TAG, "null");
            return;
        }

        String className = (String) node.getClassName();
        String text = (String) node.getText();

        viewNode.deep = deep;
        viewNode.type = className;
        viewNode.text = text;
        viewNode.clildNodeList = Lists.newArrayList();

        for (int i = 0; i < node.getChildCount(); i++) {
            getViewTree(viewNode.clildNodeList.get(i), node.getChild(i), deep + 1);
        }
    }

    //-------------------系统权限---------------------------------

    /**
     * 打开无障碍服务，需要系统权限
     *
     * @param context
     */
    public static void openAccessibilityService(Context context) {
        String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        ComponentName selfComponentName = new ComponentName(context.getPackageName(),
                "com.easyto.translator.services.EventService");
        String flattenToString = selfComponentName.flattenToString();

        Logger.d(TAG, "无障碍服务列表1： %s", enabledServicesSetting);
        Logger.d(TAG, "无障碍服务全称： %s", flattenToString);

        if (TextUtils.isEmpty(enabledServicesSetting) || enabledServicesSetting.contains("null")) {
            enabledServicesSetting = flattenToString;
            Logger.d(TAG, "重置无障碍服务");
        } else {
            String[] split = enabledServicesSetting.split(";");
            for (int i = 0; i < split.length; i++) {
                if (TextUtils.equals(split[i], flattenToString)) {
                    Logger.d(TAG, "当前应用已经添加无障碍服务");
                    return;
                }
            }
            Logger.d(TAG, "添加无障碍服务");
            enabledServicesSetting = String.format(Locale.getDefault(), "%s;%s", enabledServicesSetting, flattenToString);
        }

        Logger.d(TAG, "无障碍服务列表2： %s", enabledServicesSetting);
        Settings.Secure.putString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                enabledServicesSetting);
        Settings.Secure.putInt(context.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 1);
    }

    public static void clean(Context context) {
        String enabledServicesSetting = "";
        Settings.Secure.putString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                enabledServicesSetting);
        Settings.Secure.putInt(context.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 1);
    }

    /**
     * 判断无障碍服务是否开启
     *
     * @param context
     * @return isOpen
     */
    public static boolean isOpen(Context context, Class service) {
        String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        ComponentName selfComponentName = new ComponentName(context.getPackageName(),
                service.getClass().getCanonicalName());

        String flattenToString = selfComponentName.flattenToString();
        if (enabledServicesSetting == null ||
                !enabledServicesSetting.contains(flattenToString)) {
            return false;
        }
        return true;
    }


}
