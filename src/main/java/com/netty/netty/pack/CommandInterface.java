package com.netty.netty.pack;

/**
 * @Intro: 指令集
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/15
 * @Time: 下午7:46
 */
public interface CommandInterface {

    /**
     * 登录请求指令
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录请求返回指令
     */
    Byte LOGIN_REQUEST_RETURN = 11;

    /**
     * 心跳请求指令
     */
    Byte HEART_REQUEST = 2;

    /**
     * 心跳请求返回指令
     */
    Byte HEART_REQUEST_RETURN = 21;

    /**
     * 配置请求给客户端指令
     */
    Byte CONFIG_REQUEST = 3;

    /**
     * 客户端停止应用程序时发送给服务指令
     */
    Byte CLIENT_STOP = 4;


}
