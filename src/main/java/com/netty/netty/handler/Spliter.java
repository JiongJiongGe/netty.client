package com.netty.netty.handler;

import com.netty.netty.util.EncodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/17
 * @Time: 下午7:45
 */
public class Spliter extends LengthFieldBasedFrameDecoder{

    private static Logger logger = LoggerFactory.getLogger(Spliter.class);

    /**
     * 长度域偏移量
     */
    private static final int LENGTH_FIELD_OFFSET = 7;

    /**
     * 长度域长度
     */
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //屏蔽非法消息，判断魔数
        if (in.getInt(in.readerIndex()) != EncodeUtil.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
