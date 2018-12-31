package com.netty.netty.controller.config;

import com.netty.netty.env.EnvProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Intro: 获取配置信息
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/8
 * @Time: 下午3:12
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    /**
     * 客户端获取配置信息
     *
     * @return
     */
    @RequestMapping(value = "/get")
    public String get() {
        return EnvProperties.nettyConfig;
    }
}
