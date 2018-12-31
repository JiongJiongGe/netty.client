package com.netty.netty;

import com.netty.netty.env.EnvProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = {"com.netty.netty", "com.netty.netty.handler.heart"})
public class NettyApplication {

	private static Logger logger = LoggerFactory.getLogger(NettyApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
		//properties中获取参数
		String applicationName = context.getEnvironment().getProperty("appliction.name");
		String applicationPassword = context.getEnvironment().getProperty("application.password");
		EnvProperties.applicationName = applicationName;
		EnvProperties.applicationPassword = applicationPassword;

	}
}
