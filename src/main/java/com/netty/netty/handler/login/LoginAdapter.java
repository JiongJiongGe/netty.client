package com.netty.netty.handler.login;

import com.netty.netty.env.EnvProperties;
import com.netty.netty.pack.login.LoginRequestPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @Intro: 客户端登录发起
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/6
 * @Time: 下午8:15
 */
@ChannelHandler.Sharable
public class LoginAdapter extends ChannelInboundHandlerAdapter{

    private static Logger logger = LoggerFactory.getLogger(LoginAdapter.class);

    public static final LoginAdapter INSTANCE = new LoginAdapter();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoginRequestPacket packet = new LoginRequestPacket();
        String applicationName = EnvProperties.applicationName;
        String applicationPassword = EnvProperties.applicationPassword;
        packet.setUsername(applicationName);
        packet.setPassword(applicationPassword);
        ctx.writeAndFlush(packet);
    }
}
