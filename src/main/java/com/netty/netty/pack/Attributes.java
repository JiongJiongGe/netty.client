package com.netty.netty.pack;

import com.netty.netty.domain.UserDo;
import io.netty.util.AttributeKey;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/16
 * @Time: 下午8:57
 */
public interface Attributes {

    /**
     * 用户登录成功保存的链接信息
     */
    AttributeKey<UserDo> USER_SESSION = AttributeKey.newInstance("user_session");
}
