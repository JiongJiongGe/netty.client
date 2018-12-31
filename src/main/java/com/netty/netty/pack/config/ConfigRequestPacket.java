package com.netty.netty.pack.config;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;
import lombok.Data;

/**
 * @Intro: 配置 request pack
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/7
 * @Time: 下午7:10
 */
@Data
public class ConfigRequestPacket extends Packet {

    /**
     * 配置信息
     */
    private String configParam;

    /**
     * 消息版本
     */
    private String configVersion;

    @Override
    public Byte getCommand() {
        return CommandInterface.CONFIG_REQUEST;
    }
}
