package com.zlw.main.auxiliarydemo.service;

/**
 * @author zhaolewei on 2018/2/23.
 *         无障碍服务控制口令
 */
public class ServiceControlEvent {

    ControlType what;

    String param;

    public ServiceControlEvent(ControlType controlType) {
        this.what = controlType;
    }

    public ServiceControlEvent setWhat(ControlType controlType) {
        this.what = controlType;
        return this;
    }

    public ServiceControlEvent setParam(String param) {
        this.param = param;
        return this;
    }

    public enum ControlType {
        /**
         * 开启服务
         */
        START_SERVICE_BASE,

        /**
         * 关闭服务
         */
        CLOSE_SERVICE,

        /**
         * 模拟点击服务
         */
        TEST_CLICK,
    }
}
