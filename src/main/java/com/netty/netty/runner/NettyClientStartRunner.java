package com.netty.netty.runner;

import com.netty.netty.constant.ZKConstant;
import com.netty.netty.zk.ZKServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @Intro: 项目启动时，启动netty客户端
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/6
 * @Time: 下午7:06
 */
@Component
public class NettyClientStartRunner implements CommandLineRunner{

    private static Logger logger = LoggerFactory.getLogger(NettyClientStartRunner.class);

    @Autowired
    private ZKServiceDiscovery zkServiceDiscovery;

    @Override
    public void run(String... args) throws Exception {
        ZKServiceDiscovery.zkServiceDiscovery = zkServiceDiscovery;
        zkServiceDiscovery.connectServer();
        zkServiceDiscovery.getNode();
        String serverAddress = zkServiceDiscovery.discover();
        if (!StringUtils.isEmpty(serverAddress)) {
            ZKConstant.ZK_CURRENT_SERVER_NODE = serverAddress;
            String[] addressArr = serverAddress.split(":");
            if (addressArr != null && addressArr.length > 0) {
                ClientConnectUtil.connect(addressArr[0], Integer.parseInt(addressArr[1]));
            }
        }
    }
}
