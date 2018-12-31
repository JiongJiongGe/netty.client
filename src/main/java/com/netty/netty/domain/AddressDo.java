package com.netty.netty.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/30
 * @Time: 下午3:15
 */
@Data
public class AddressDo implements Serializable{

    /**
     * 服务提供者地址
     */
    private String address;

    /**
     * 是否可用 1、可用;0、关闭
     */
    private Integer isUse;
}
