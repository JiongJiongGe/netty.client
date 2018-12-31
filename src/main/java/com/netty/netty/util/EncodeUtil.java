package com.netty.netty.util;

import com.netty.netty.pack.CommandInterface;
import com.netty.netty.pack.Packet;
import com.netty.netty.pack.config.ConfigRequestPacket;
import com.netty.netty.pack.heart.HeartResponsePacket;
import com.netty.netty.pack.login.LoginResponsePacket;
import com.netty.netty.serializer.JSONSerializerImpl;
import com.netty.netty.serializer.SerializerAlgorithmInterface;
import com.netty.netty.serializer.SerializerInterface;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/16
 * @Time: 下午3:31
 */
public class EncodeUtil {

    private static Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    public static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> packetMap;

    private static final Map<Byte, SerializerInterface> serializerMap;

    static {
        //请求pack
        packetMap = new HashMap<>();
        packetMap.put(CommandInterface.LOGIN_REQUEST_RETURN, LoginResponsePacket.class);
        packetMap.put(CommandInterface.HEART_REQUEST_RETURN, HeartResponsePacket.class);
        packetMap.put(CommandInterface.CONFIG_REQUEST, ConfigRequestPacket.class);

        serializerMap = new HashMap<>();
        SerializerInterface serializerInterface = new JSONSerializerImpl();
        serializerMap.put(SerializerAlgorithmInterface.JSON_ALGORITHM, serializerInterface);

    }

    /**
     * 编码
     *
     * @param byteBuf
     * @param packet
     * @return
     */
    public static ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        // 2. 序列化 Java 对象，这种方式很独特 SerializerInterface.DEFAULT
        byte[] bytes = SerializerInterface.DEFAULT.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getX_version());
        byteBuf.writeByte(SerializerInterface.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 编码
     *
     * @param packet
     * @return
     */
    public static ByteBuf encode(Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化 Java 对象，这种方式很独特 SerializerInterface.DEFAULT
        byte[] bytes = SerializerInterface.DEFAULT.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getX_version());
        byteBuf.writeByte(SerializerInterface.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     *
     * @param byteBuf
     * @return
     */
    public static Packet decode(ByteBuf byteBuf) {
        //跳过部分如果需要验证，可自行处理的
        //跳过魔数验证，4字节
        byteBuf.skipBytes(4);
        //跳过版本号验证，1子节
        byteBuf.skipBytes(1);
        //获取序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        //数据长度
        int length = byteBuf.readInt();
        //数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        //验证指令和序列化算法
        Class<? extends Packet> requestCommond = getRequestType(command);
        SerializerInterface serializerInterface = getSerializer(serializeAlgorithm);
        if (requestCommond != null && serializerInterface != null) {
            return SerializerInterface.DEFAULT.deserialize(requestCommond, bytes);
        } else {
            logger.info("指令或者序列化算法不存在，请检查数据！");
        }
        return null;
    }

    /**
     *
     * @param serializeAlgorithm
     * @return
     */
    private static SerializerInterface getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    /**
     *
     * @param command
     * @return
     */
    private static Class<? extends Packet> getRequestType(byte command) {
        return packetMap.get(command);
    }

}
