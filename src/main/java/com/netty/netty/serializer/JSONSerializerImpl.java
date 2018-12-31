package com.netty.netty.serializer;


import com.alibaba.fastjson.JSON;

import static com.netty.netty.serializer.SerializerAlgorithmInterface.JSON_ALGORITHM;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/15
 * @Time: 下午7:59
 */
public class JSONSerializerImpl implements SerializerInterface{

    @Override
    public byte getSerializerAlgorithm() {
        return JSON_ALGORITHM;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
