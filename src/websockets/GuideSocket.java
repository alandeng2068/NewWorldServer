package websockets;

import common.*;
import databaseMgr.user.UserInfoDBO;
import databaseMgr.vo.UserInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/ws/guide")
public class GuideSocket extends BaseSocket {
    private Session session;
    private static final Logger logger = LoggerFactory.getLogger(GuideSocket.class);

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        GameServerManager.getInstance().add(this);
    }
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("收到客户端"+getUrl()+"消息："+message);
        JSONObject jsonobject = JSONObject.fromObject(message);
        String status = jsonobject.getString("status");
        int userId = jsonobject.getInt("id");
        UserInfo user = UsersMgr.getInstance().getUser(userId);
        if(user == null){
            logger.error("新手引导过程中出错：user不存在，客户端发来的id为："+userId);
            return;
        }
        if(user.getGuide()<(StaticVariableDef.MAX_GUIDE_STEP-1) && status.equals(StaticVariableDef.GUIDE_NEXT))
        {
            user.setGuide(user.getGuide()+1);
        }else{
            user.setGuide(-1);
        }
        UserInfoDBO userInfoDBO = new UserInfoDBO();
        userInfoDBO.changeUserNewGuide(user.getId(),user.getGuide());
        JSONObject result = new JSONObject();
        result.put("guide",user.getGuide());
        sendMessage(result.toString());
    }
    @OnClose
    public void onClose(){
        logger.info("关闭客户端连接："+ getUrl());
        GameServerManager.getInstance().remove(this);
    }
    @OnError
    public void onError(Session session, Throwable error){
        logger.warn("客户端"+getUrl()+"发送错误："+error.getMessage());
    }
    public String getUrl(){
        return this.session.getId().toString();
    }
    @Override
    public void sendMessage(String message){
        try{
            this.session.getBasicRemote().sendText(message);
            logger.info("发送消息到客户端"+getUrl()+"："+message);
        }catch (IOException e)
        {
            logger.error("发送消息到客户端"+getUrl()+"出错："+e.getMessage());
        }
    }
}
