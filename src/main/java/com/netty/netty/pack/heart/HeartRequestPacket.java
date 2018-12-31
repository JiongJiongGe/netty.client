package com.netty.netty.pack.heart;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;

/**
 * @Intro:  心跳 request pack
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/6
 * @Time: 上午10:24
 */
public class HeartRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return CommandInterface.HEART_REQUEST;
    }
}
