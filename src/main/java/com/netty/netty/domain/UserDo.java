package com.netty.netty.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Intro: User对象
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/29
 * @Time: 下午2:03
 */
@Data
public class UserDo implements Serializable{

    private static final long serialVersionUID = -3338269793315462271L;

    private String id;

    private String name;

    public UserDo() {

    }

    public UserDo(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
