package com.netty.netty.util;

import com.netty.netty.domain.UserDo;
import com.netty.netty.pack.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/29
 * @Time: 下午1:59
 */
public class SessionUtil {

    private static Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    private static final Map<String, Channel> existMap = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> existGroupMap = new ConcurrentHashMap<>();

    /**
     * 用户登录成功后，加入map
     *
     * @param userDo
     * @param channel
     */
    public static void addMapUser(UserDo userDo, Channel channel) {
        existMap.put(userDo.getId(), channel);
        channel.attr(Attributes.USER_SESSION).set(userDo);
    }

    /**
     * 判断用户是否已登录
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return getUserDo(channel) != null;
    }

    /**
     * 获取userDo对象
     *
     * @param channel
     * @return
     */
    public static UserDo getUserDo(Channel channel) {
        return channel.attr(Attributes.USER_SESSION).get();
    }

    /**
     * 根据userId获取channel
     *
     * @param userId
     * @return
     */
    public static Channel getChannel(String userId) {
        return existMap.get(userId);
    }

    /**
     * 退出登录
     *
     * @param channel
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            UserDo userDo = getUserDo(channel);
            existMap.remove(userDo.getId());
            channel.attr(Attributes.USER_SESSION).set(null);
            logger.info("{} 退出登录", userDo.getName());
        }
    }

    /**
     * 群聊创建成功，放入map
     *
     * @param groupId
     * @param channelGroup
     */
    public static void buildGroup(String groupId, ChannelGroup channelGroup) {
        existGroupMap.put(groupId, channelGroup);
    }

    /**
     * 根据groupId获取channelGroup
     *
     * @param groupId
     * @return
     */
    public static ChannelGroup getGroupById(String groupId) {
        return existGroupMap.get(groupId);
    }
}
