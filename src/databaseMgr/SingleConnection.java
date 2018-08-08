package databaseMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Class;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnection {
    public static String URL = "jdbc:mysql://localhost:3306/new_world?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=GMT%2B8";
    public static String USER = "root";
    public static String PSW = "root";
    public boolean isBusy = false;
    private Connection _connection;
    private static final Logger logger = LoggerFactory.getLogger(SingleConnection.class);
    public SingleConnection(){
        initConnection();
    }

    private void initConnection(){
        if(_connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                logger.error("加载数据库驱动失败"+e.getMessage());
            }
            try {
                //获取数据库连接
                _connection = DriverManager.getConnection(URL, USER, PSW);
            } catch (SQLException e) {
                logger.error("创建数据库连接失败！"+e.getMessage());
                _connection = null;
            }
        }
    }

    public void setInBusy(boolean isBusy){
        this.isBusy = isBusy;
    }

    public boolean isBusy(){
        return this.isBusy;
    }

    public Connection getConnection(){
        return _connection;
    }
}
