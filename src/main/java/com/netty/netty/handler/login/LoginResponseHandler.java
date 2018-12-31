package com.netty.netty.handler.login;

import com.netty.netty.domain.UserDo;
import com.netty.netty.env.EnvProperties;
import com.netty.netty.pack.heart.HeartRequestPacket;
import com.netty.netty.pack.login.LoginResponsePacket;
import com.netty.netty.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Intro: 登录返回handler
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/19
 * @Time: 下午5:33
 */
@ChannelHandler.Sharable
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket>{

    private static Logger logger = LoggerFactory.getLogger(LoginResponseHandler.class);

    public static final LoginResponseHandler INSTANCE = new LoginResponseHandler();

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.getSucess()) {
            logger.info("用户Id: {}, 登录成功", loginResponsePacket.getUserId());
            EnvProperties.applicationId = loginResponsePacket.getUserId();
            SessionUtil.addMapUser(new UserDo(loginResponsePacket.getUserId(), loginResponsePacket.getName()), channelHandlerContext.channel());
            //登录成功后，定时发送心跳
            scheduleSendHeartBeat(channelHandlerContext);
        } else {
            logger.info("时间: {}, 登录失败，失败原因: {}", new Date(), loginResponsePacket.getMessage());
            channelHandlerContext.channel().close();
        }
    }

    /**
     * 定时发送心跳
     *
     * @param ctx
     */
    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                logger.info("客户端发了一个心跳");
                ctx.writeAndFlush(new HeartRequestPacket());
                scheduleSendHeartBeat(ctx);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
