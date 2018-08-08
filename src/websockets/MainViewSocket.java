package websockets;

import common.GameServerManager;
import common.ProtocolDef;
import common.UsersMgr;
import databaseMgr.user.UserInfoDBO;
import databaseMgr.vo.UserInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/ws/mainview")
public class MainViewSocket extends BaseSocket{
    private Session session;
    private static final Logger logger = LoggerFactory.getLogger(MainViewSocket.class);

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        GameServerManager.getInstance().add(this);
    }
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("收到客户端"+getUrl()+"消息："+message);
        JSONObject msg = JSONObject.fromObject(message);
        int cmd = msg.getInt("cmd");
        int userId = msg.getInt("id");
        UserInfo user = UsersMgr.getInstance().getUser(userId);
        if(user == null){
            logger.error("主界面查询出错：user不存在，客户端发来的id为："+userId);
            return;
        }
        JSONObject obj = new JSONObject();
        if(cmd == ProtocolDef.CMD_QUERY_USER_INFO)
        {
            obj.put("cmd",ProtocolDef.CMD_QUERY_USER_INFO);
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            obj.put("level",user.getLevel());
            obj.put("exp",user.getExp());
            obj.put("id_pet",user.getId_pet());
            obj.put("bag_maxnum",user.getBag_maxnum());
            obj.put("gold",user.getGold());
            obj.put("money",user.getMoney());
            obj.put("id_map",user.getId_map());
            sendMessage(obj.toString());
        }else if(cmd == ProtocolDef.CMD_QUERY_TASKS_ATTRS){
            obj.put("cmd",ProtocolDef.CMD_QUERY_TASKS_ATTRS);
            obj.put("id_tasks",user.getId_tasks());
            obj.put("attack",user.getAttack());
            obj.put("m_attack",user.getM_attack());
            obj.put("def",user.getDef());
            obj.put("m_def",user.getM_def());
            sendMessage(obj.toString());
        }else if( cmd == ProtocolDef.CMD_QUERY_SKILL_EQUIPS){
            obj.put("cmd",ProtocolDef.CMD_QUERY_SKILL_EQUIPS);
            obj.put("ids_skill",user.getIds_skill());
            obj.put("equip_head",user.getEquip_head());
            obj.put("equip_body",user.getEquip_body());
            obj.put("equip_pants",user.getEquip_pants());
            obj.put("equip_shoes",user.getEquip_shoes());
            obj.put("equip_weapon",user.getEquip_weapon());
            sendMessage(obj.toString());
        }else  if( cmd == ProtocolDef.CMD_CHANGE_NAME){
            String name = msg.getString("name");
            UserInfoDBO userDbo = new UserInfoDBO();
            int result = userDbo.changeUserName(userId,name);
            user.setName(name);
            obj.put("cmd",ProtocolDef.CMD_CHANGE_NAME);
            obj.put("result",result);
            obj.put("name",name);
            sendMessage(obj.toString());
        }
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
