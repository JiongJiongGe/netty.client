package com.netty.netty.handler.heart;

import com.netty.netty.constant.ZKConstant;
import com.netty.netty.runner.ClientConnectUtil;
import com.netty.netty.util.SessionUtil;
import com.netty.netty.zk.ZKServiceDiscovery;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Intro: 服务端空闲检测
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/6
 * @Time: 上午9:55
 */
public class IMIdleStateHandler extends IdleStateHandler{

    private static Logger logger = LoggerFactory.getLogger(IMIdleStateHandler.class);

    /**
     * 空闲时间
     */
    private static final int TIME_DE = 15;

    public IMIdleStateHandler() {
        super(TIME_DE, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        logger.info("{} 秒内未读取到数据，关闭连接", TIME_DE);
        ctx.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("由于服务端关闭，客户端连接被关闭！");
        //清除
        SessionUtil.unBindSession(ctx.channel());
        //轮询
        scheduleLogin(ctx);
    }

    /**
     * 轮询登录
     *
     * @param ctx
     */
    private void scheduleLogin(ChannelHandlerContext ctx) {
        ZKServiceDiscovery.dataAddressList.remove(ZKConstant.ZK_CURRENT_SERVER_NODE);
        ZKServiceDiscovery.notUserAddressList.add(ZKConstant.ZK_CURRENT_SERVER_NODE);
        Set<String> dataAddressList =  ZKServiceDiscovery.dataAddressList;
        int size = dataAddressList.size();
        String serverAddress;
        if (size > 0) {
            if (size == 1) {
                serverAddress = ZKServiceDiscovery.dataAddressList.iterator().next();
            } else {
               serverAddress = ClientConnectUtil.provideServerAddress(dataAddressList);
            }
            ZKConstant.ZK_CURRENT_SERVER_NODE = serverAddress;
        } else {
            serverAddress = ZKConstant.ZK_CURRENT_SERVER_NODE;
        }
        //重新获取节点
        logger.info("serverAddress = {}", serverAddress);
        if (!StringUtils.isEmpty(serverAddress)) {
            String[] addressArr = serverAddress.split(":");
            if (addressArr != null && addressArr.length > 0) {
                ClientConnectUtil.connect(addressArr[0], Integer.parseInt(addressArr[1]));
            }
        }
    }
}
