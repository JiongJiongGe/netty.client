package com.netty.netty.pack.login;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;
import lombok.Data;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/16
 * @Time: 下午4:29
 */
@Data
public class LoginResponsePacket extends Packet {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 是否成功
     */
    private Boolean sucess;

    /**
     * 提示
     */
    private String message;

    @Override
    public Byte getCommand() {
        return CommandInterface.LOGIN_REQUEST_RETURN;
    }
}
