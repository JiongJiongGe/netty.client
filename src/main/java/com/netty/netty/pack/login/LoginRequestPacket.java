package com.netty.netty.pack.login;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;
import lombok.Data;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/15
 * @Time: 下午7:48
 */
@Data
public class LoginRequestPacket extends Packet {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public Byte getCommand() {
        return CommandInterface.LOGIN_REQUEST;
    }
}
