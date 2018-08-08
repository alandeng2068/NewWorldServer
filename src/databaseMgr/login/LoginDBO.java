package databaseMgr.login;

import databaseMgr.DBPoolMgr;
import databaseMgr.SingleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websockets.LoginSocket;

import java.sql.*;

public class LoginDBO {
    private static final Logger logger = LoggerFactory.getLogger(LoginDBO.class);

    public ResultSet queryAccount(String name, String psw){
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement state = conn.getConnection().prepareStatement("SELECT * FROM user_account where user_account=? and user_psw=?");
            state.setString(1,name);
            state.setString(2,psw);
            rs = state.executeQuery();
        }catch (SQLException e)
        {
            logger.error("查询数据库表user_account出错"+e.getMessage());
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return  rs;
    }

    public int insertIntoAccount(String name, String psw) {
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        int result = 0;
        try{
            PreparedStatement state = conn.getConnection().prepareStatement("insert into user_account(user_account,user_psw) values(?,?)");
            state.setString(1,name);
            state.setString(2,psw);
            logger.info(state.toString());
            result = state.executeUpdate();
        }catch (SQLException e){
            logger.error("插入数据库表user_account出错："+e.getMessage());
            return -1;
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return result;
    }
}
