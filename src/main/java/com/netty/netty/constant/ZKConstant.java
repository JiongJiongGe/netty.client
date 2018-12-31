package com.netty.netty.constant;

/**
 * @Intro: zk constant
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/14
 * @Time: 下午7:29
 */
public class ZKConstant {

    /**
     * ZK session超时时间
     */
    public static final int ZK_SESSION_TIMEOUT = 5000;

    /**
     * netty 注册地址
     */
    public static final String ZK_NETTY_REGISTRY_PATH = "/netty";

    /**
     * netty 服务端注册地址
     */
    public static final String ZK_NETTY_SERVER_REGISTRY_PATH = ZK_NETTY_REGISTRY_PATH + "/server";

    public static volatile String ZK_CURRENT_SERVER_NODE = "";

}
