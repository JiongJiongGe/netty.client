package com.netty.netty.env;

import java.io.Serializable;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/7
 * @Time: 上午9:43
 */
public class EnvProperties implements Serializable{

    /**
     * 应用名称
     */
    public static String applicationName;

    /**
     * 密码
     */
    public static String applicationPassword;

    /**
     * 应用程序在服务端的Id
     */
    public static String applicationId;

    public static String nettyConfig = "";

    /**
     * zk服务地址
     */
    public static volatile String ZK_REGISTRY_ADDRESS;
}
