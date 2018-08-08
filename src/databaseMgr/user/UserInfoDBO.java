package databaseMgr.user;

import databaseMgr.DBPoolMgr;
import databaseMgr.SingleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDBO {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoDBO.class);
    public ResultSet queryUserInfoById(int userId){
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement state = conn.getConnection().prepareStatement("SELECT * FROM user_info where id=?");
            state.setInt(1,userId);
            rs = state.executeQuery();
        }catch (SQLException e)
        {
            logger.error("通过玩家id查询数据库表user_info出错"+e.getMessage());
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return  rs;
    }
    //通过userId和账号名初始化玩家数据
    public int insertIntoUserInfo(int id,String name) {
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        int result = 0;
        try{
            PreparedStatement state = conn.getConnection().prepareStatement("insert into user_info(id,name) values(?,?)");
            state.setInt(1,id);
            state.setString(2,name);
            result = state.executeUpdate();
        }catch (SQLException e){
            logger.error("插入数据库表user_info出错："+e.getMessage());
            return -1;
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return result;
    }
    //改变玩家新手引导状态
    public int changeUserNewGuide(int userId,int guide){
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        int result = 0;
        try{
            PreparedStatement state = conn.getConnection().prepareStatement("update user_info set guide=? where id = ?");
            state.setInt(1,guide);
            state.setInt(2,userId);
            result = state.executeUpdate();
        }catch (SQLException e){
            logger.error("修改数据库表user_info出错："+e.getMessage());
            return -1;
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return result;
    }
    //改变角色名字
    public int changeUserName(int userId,String name){
        SingleConnection conn = DBPoolMgr.getInstance().getConnection();
        int result = 0;
        try{
            PreparedStatement state = conn.getConnection().prepareStatement("update user_info set name =? where id = ?");
            state.setString(1,name);
            state.setInt(2,userId);
            result = state.executeUpdate();
        }catch (SQLException e){
            logger.error("修改数据库表user_info出错："+e.getMessage());
            return -1;
        }
        DBPoolMgr.getInstance().dropConnection(conn);
        return result;
    }
}
