package com.netty.netty.pack;

import lombok.Data;

/**
 * @Intro: 基础packet
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/15
 * @Time: 下午3:09
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte x_version = 1;

    public abstract Byte getCommand();
}
