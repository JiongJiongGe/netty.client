package com.netty.netty.handler.config;

import com.netty.netty.constant.NettyConstant;
import com.netty.netty.env.EnvProperties;
import com.netty.netty.pack.config.ConfigRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Intro: 配置 request handler
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/7
 * @Time: 下午7:25
 */
public class ConfigRequestHandler extends SimpleChannelInboundHandler<ConfigRequestPacket>{

    private static Logger logger = LoggerFactory.getLogger(ConfigRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConfigRequestPacket configRequestPacket) throws Exception {
        //验证消息版本，避免出现消息延迟导致内容不是最新的
        if (Integer.parseInt(configRequestPacket.getConfigVersion()) > NettyConstant.MESSAGE_CONFIG_VERSION) {
            NettyConstant.MESSAGE_CONFIG_VERSION = Integer.parseInt(configRequestPacket.getConfigVersion());
            String configParam = configRequestPacket.getConfigParam();
            EnvProperties.nettyConfig = configParam;
            logger.info("configParam = {}", configParam);
        } else {
            logger.error("版本信息已过期...");
        }
    }
}
