package com.netty.netty.pack.stop;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;

/**
 * @Intro: 客户端应用程序停止时 通知服务端packet
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/8
 * @Time: 下午4:14
 */
public class ClientStopRequestPacket extends Packet{

    @Override
    public Byte getCommand() {
        return CommandInterface.CLIENT_STOP;
    }
}
