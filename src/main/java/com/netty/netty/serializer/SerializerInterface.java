package com.netty.netty.serializer;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/15
 * @Time: 下午7:54
 */
public interface SerializerInterface {

    /**
     * 序列化算法
     *
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * Java对象转换成二进制
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转为Java对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    SerializerInterface DEFAULT = new JSONSerializerImpl();
}
