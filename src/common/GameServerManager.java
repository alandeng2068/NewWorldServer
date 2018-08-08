package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websockets.BaseSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GameServerManager {
    private static GameServerManager _instance;
    private static final Logger logger = LoggerFactory.getLogger(GameServerManager.class);
    private static Collection<BaseSocket> serverList = Collections.synchronizedCollection(new ArrayList<BaseSocket>());
    public  static GameServerManager getInstance(){
        if(_instance == null){
            _instance = new GameServerManager();
        }
        return _instance;
    }

    public void startUp(){
        logger.info("游戏服务器启动中。。。");
    }

    public void stop(){
        logger.info("游戏服务器正在关闭。。。");
    }

    public void add(BaseSocket wSocket){
        serverList.add(wSocket);
        logger.info("添加新连接："+wSocket.getUrl()+"---连接总数是："+serverList.size());
    }

    public void remove(BaseSocket wSocket){
        serverList.remove(wSocket);
        logger.info("移除连接："+wSocket.getUrl()+"---连接总数是："+serverList.size());
    }

    public void broadCast(String msg){
        for (BaseSocket wSocket : serverList) {
           wSocket.sendMessage(msg);
        }
    }
}
