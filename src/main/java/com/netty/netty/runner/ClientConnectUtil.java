package com.netty.netty.runner;

import com.netty.netty.constant.ZKConstant;
import com.netty.netty.handler.PacketCodecHandler;
import com.netty.netty.handler.Spliter;
import com.netty.netty.handler.config.ConfigRequestHandler;
import com.netty.netty.handler.heart.IMIdleStateHandler;
import com.netty.netty.handler.login.LoginAdapter;
import com.netty.netty.handler.login.LoginResponseHandler;
import com.netty.netty.zk.ZKServiceDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Intro: 客户端连接服务端 connect方法
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/8
 * @Time: 下午2:50
 */
public class ClientConnectUtil {

    private static Logger logger = LoggerFactory.getLogger(ClientConnectUtil.class);

    /**
     * 轮询登录，5s一次
     */
    private static final int TIME_LOGIN = 5;

    /**
     * 客户端连接服务端
     */
    public static Channel connect(String host, int port) {

        /**
         * 线程组(线程模型)
         */
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        /**
         * 客户端启动类，负责启动客户端以及连接服务端
         */
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                //.group 配置线程组
                .group(workerGroup)
                //.channel 指定IO模型 NioSocketChannel为NIO模型
                .channel(NioSocketChannel.class)
                //.handler 定义连接的业务处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //ch.pipeline()返回和此条连接相关的责任链
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(LoginAdapter.INSTANCE);
                        ch.pipeline().addLast(LoginResponseHandler.INSTANCE);
                        ch.pipeline().addLast(new ConfigRequestHandler());
                    }
                })
                //.option设置tcp底层相关属性，CONNECT_TIMEOUT_MILLIS表示连接时间，超过这个时间则连不上
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap.connect(host, port)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            logger.info("连接成功");
                        } else {
                            logger.error(future.cause().getMessage());
                            logger.info("连接失败");
                            future.channel().eventLoop().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    String serverAddress;
                                    //优先遍历存在的节点
                                    Set<String> dataAddressList =  ZKServiceDiscovery.dataAddressList;
                                    if (!dataAddressList.isEmpty()) {
                                        serverAddress = provideServerAddress(dataAddressList);
                                    } else {
                                        //遍历不可使用的点，说不定就开启了。
                                        Set<String> notUserAddressList = ZKServiceDiscovery.notUserAddressList;
                                        serverAddress = provideServerAddress(notUserAddressList);
                                    }
                                    ZKConstant.ZK_CURRENT_SERVER_NODE = serverAddress;
                                    logger.info("serverAddress = {}", serverAddress);
                                    if (!StringUtils.isEmpty(serverAddress)) {
                                        String[] addressArr = serverAddress.split(":");
                                        if (addressArr != null && addressArr.length > 0) {
                                            connect(addressArr[0], Integer.parseInt(addressArr[1]));
                                        }
                                    }
                                }
                            }, TIME_LOGIN, TimeUnit.SECONDS);
                        }
                    }
                }).channel();
    }

    /**
     * 随机提供服务者的地址
     *
     * @param addressList
     * @return
     */
    public static String provideServerAddress(Set<String> addressList) {
        List<String> list = new ArrayList<>(addressList);
        String serverAddress = list.get(ThreadLocalRandom.current().nextInt(addressList.size()));
        logger.info("随机 serverAddress = {}", serverAddress);
        return serverAddress;
    }
}
