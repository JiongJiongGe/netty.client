package com.netty.netty.handler;

import com.netty.netty.pack.Packet;
import com.netty.netty.util.EncodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/5
 * @Time: 下午7:35
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    private static Logger logger = LoggerFactory.getLogger(PacketCodecHandler.class);

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    /**
     * 加密
     *
     * @param channelHandlerContext
     * @param packet
     * @param list
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> list) throws Exception {
        ByteBuf buf = channelHandlerContext.alloc().buffer();
        EncodeUtil.encode(buf, packet);
        list.add(buf);
    }

    /**
     * 解密
     *
     * @param channelHandlerContext
     * @param buf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        list.add(EncodeUtil.decode(buf));
    }
}
