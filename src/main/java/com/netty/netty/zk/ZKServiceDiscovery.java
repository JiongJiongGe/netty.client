package com.netty.netty.zk;

import com.google.gson.Gson;
import com.netty.netty.constant.ZKConstant;
import com.netty.netty.domain.AddressDo;
import com.netty.netty.env.EnvProperties;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Intro:  zk服务发现
 *
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/11/13
 * @Time: 下午4:59
 */
@Component
public class ZKServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZKServiceDiscovery.class);

    public static volatile ZKServiceDiscovery zkServiceDiscovery;

    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 可连接的提供者
     */
    public static volatile Set<String> dataAddressList = new HashSet<String>();

    /**
     * 无法连接的提供者
     */
    public static volatile Set<String> notUserAddressList = new HashSet<String>();

    private ZooKeeper zk;

    @Autowired
    private Environment env;

    /**
     * 服务发现
     */
    public String discover() {
        String dataAddress = "";
        if (zk != null) {
            int size = dataAddressList.size();
            if (size > 0) {
                if (size == 1) {
                    dataAddress = dataAddressList.iterator().next();
                } else {
                    List<String> list = new ArrayList<>(dataAddressList);
                    dataAddress = list.get(ThreadLocalRandom.current().nextInt(size));
                }
            }
        }
        return dataAddress;
    }

    /**
     * 建立连接
     *
     * @return
     */
    public ZooKeeper connectServer() {
        ZooKeeper zk = null;
        EnvProperties.ZK_REGISTRY_ADDRESS = env.getProperty("zk.registry.address");
        try {
            zk = new ZooKeeper(EnvProperties.ZK_REGISTRY_ADDRESS, ZKConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            logger.error("", e);
        }
        catch (InterruptedException ex){
            logger.error("", ex);
        }
        this.zk = zk;
        return zk;
    }

    /**
     * 查找服务端节点

     */
    public void getNode() {
        try {
            List<String> nodeList = zk.getChildren(ZKConstant.ZK_NETTY_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    //Event.EventType.NodeCreated 和 Event.EventType.NodeDeleted针对的是节点的创建和删除
                    //Event.EventType.NodeChildrenChanged 针对子节点的更新，所以加入提供者/移除提供者都触发此事件
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        getNode();
                    }
                }
            });
            Set<String> dataAddressList = new HashSet<String>();
            for (String node : nodeList) {
                byte[] bytes = zk.getData(ZKConstant.ZK_NETTY_REGISTRY_PATH + "/" + node, false, null);
                String address = new String(bytes);
                dataAddressList.add(address);
                //如果不可用中存在则移除
                if (notUserAddressList.contains(address)) {
                    notUserAddressList.remove(address);
                }
            }
            this.dataAddressList = dataAddressList;
            logger.info("use size = {}, not use size = {}", dataAddressList.size(), notUserAddressList.size());
        } catch (KeeperException | InterruptedException e) {
            logger.error("", e);
        }
    }
}
