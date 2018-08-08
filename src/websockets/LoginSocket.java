package websockets;
import common.*;
import databaseMgr.login.LoginDBO;
import databaseMgr.user.UserInfoDBO;
import databaseMgr.vo.ErrorMsg;
import databaseMgr.vo.UserAccount;
import databaseMgr.vo.UserInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.ResultSet;

@ServerEndpoint("/ws/login")
public class LoginSocket extends BaseSocket{
    private Session session;
    private static final Logger logger = LoggerFactory.getLogger(LoginSocket.class);

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        GameServerManager.getInstance().add(this);
    }
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("收到客户端"+getUrl()+"消息："+message);
        JSONObject jsonobject = JSONObject.fromObject(message);
        ErrorMsg errorObj = new ErrorMsg();
        UserAccount account = (UserAccount) JSONObject.toBean(jsonobject,UserAccount.class);
        if(account.getName()!= ""&&account.getName()!=null){
            LoginDBO login = new LoginDBO();
            UserInfoDBO userInfoDBO = new UserInfoDBO();
            UserAccount accountResult = queryAcountInfo(login,account,account.getRegister());
            if(accountResult == null)
            {
                if(account.getRegister() == StaticVariableDef.IS_REGISTER) {
                    int result = login.insertIntoAccount(account.getName(), account.getPsw());
                    if (result == 1) {
                        accountResult = queryAcountInfo(login, account, 0);
                        if(accountResult == null)
                        {
                            logger.error("建立账号之后查询出错："+account.toString());
                        }else{
                            result = userInfoDBO.insertIntoUserInfo(accountResult.getId(),accountResult.getName());
                            if(result == 1)
                            {
                                sendUserInfo(userInfoDBO,accountResult);
                            }else{
                                sendRegisterError(errorObj,jsonobject);
                            }
                        }
                    } else {
                        sendRegisterError(errorObj,jsonobject);
                    }
                }
                else{
                    errorObj.setErrorId(ProtocolDef.MSG_LOGIN_NONE);
                    jsonobject = JSONObject.fromObject(errorObj);
                    sendMessage(jsonobject.toString());
                }
            }else{
                sendUserInfo(userInfoDBO,accountResult);
            }
            login = null;
        }
    }
    /**发送玩家新手引导信息**/
    private void sendUserInfo(UserInfoDBO userInfoDBO,UserAccount accountResult){
        UserInfo userInfo = queryUserInfo(userInfoDBO,accountResult);
        UsersMgr.getInstance().addUser(userInfo);
        JSONObject result = new JSONObject();
        result.put("id",userInfo.getId());
        result.put("name",accountResult.getName());
        result.put("psw",accountResult.getPsw());
        result.put("guide",userInfo.getGuide());
        sendMessage(result.toString());
    }
    /**发送账号注册出错信息**/
    private void sendRegisterError(ErrorMsg errorObj,JSONObject jsonobject){
        errorObj.setErrorId(ProtocolDef.MSG_REGISTER_ERROR);
        jsonobject = JSONObject.fromObject(errorObj);
        sendMessage(jsonobject.toString());
        logger.error("账号注册出错");
    }
    private UserAccount queryAcountInfo( LoginDBO login, UserAccount account,int isRegister){
        ResultSet rs = login.queryAccount(account.getName(),account.getPsw());
        UserAccount info = null;
        try {
            if(rs.next())
            {
                info = new UserAccount();
                info.setId(rs.getInt(1));
                info.setName(rs.getString(2));
                info.setPsw(rs.getString(3));
                info.setRegister(isRegister);
            }
        }catch (Exception e){
            logger.error("处理数据库表user_account的返回结果出错："+e.getMessage());
        }
        return info;
    }

    private UserInfo queryUserInfo(UserInfoDBO dbo,UserAccount account){
        ResultSet rs = dbo.queryUserInfoById(account.getId());
        UserInfo info = null;
        try {
            if(rs.next())
            {
                info = new UserInfo();
                String skills = rs.getString(8);
                int[] ids_skill=new int[6];
                if(skills!=null&&skills!="")
                {
                    ids_skill = StringUtils.FromStringArrToIntArr(skills.split(","));
                }
                String tasks = rs.getString(12);
                int[] ids_task = new int[10];
                if(tasks!=null&&tasks!="")
                {
                    ids_task = StringUtils.FromStringArrToIntArr(tasks.split(","));
                }
                info.setUserInfo(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),
                        rs.getInt(7),ids_skill,rs.getInt(9),rs.getInt(10),rs.getInt(11),ids_task,rs.getInt(13),rs.getInt(14),
                        rs.getInt(15),rs.getInt(16),rs.getInt(17),rs.getInt(18),rs.getInt(19),rs.getInt(20),rs.getInt(21));
            }
        }catch (Exception e){
            logger.error("处理数据库表user_info的返回结果出错："+e.getMessage());
        }
        return info;
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
    @Override
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
