package databaseMgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websockets.LoginSocket;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

public class DBPoolMgr {
    private static DBPoolMgr _instance;
    private static final Logger logger = LoggerFactory.getLogger(DBPoolMgr.class);
    private int _maxConnectionsNum = 9999;
    private Vector _connectionPool;
    public DBPoolMgr(){
        _connectionPool = new Vector();
    }
    public static DBPoolMgr getInstance(){
        if(_instance == null)
        {
            _instance = new DBPoolMgr();
        }
        return _instance;
    }

    public synchronized SingleConnection getConnection(){
        SingleConnection conn = getFreeConnection(); // 获得一个可用的数据库连接
        // 如果目前没有可以使用的连接，即所有的连接都在使用中
        while (conn == null) {
            // 等一会再试
            try {
                wait(250);
                conn = getFreeConnection();
            }catch (InterruptedException e)
            {
                logger.error("等待连接数据的过程中出错："+e.getMessage());
            }
        }
        return conn;// 返回获得的可用的连接

    }
    private void createConnection(){
        if (this._maxConnectionsNum > 0 && this._connectionPool.size() >= this._maxConnectionsNum){
            return;
        }
        SingleConnection singleConn = new SingleConnection();
        if(_connectionPool.size() == 0)
        {
            Connection conn = singleConn.getConnection();
            try{
                DatabaseMetaData metaData = conn.getMetaData();
                // 数据库返回的 driverMaxConnections 若为 0 ，表示此数据库没有最大
                // 连接限制，或数据库的最大连接限制不知道
                // driverMaxConnections 为返回的一个整数，表示此数据库允许客户连接的数目
                // 如果连接池中设置的最大连接数量大于数据库允许的连接数目 , 则置连接池的最大
                // 连接数目为数据库允许的最大数目
                int driverMaxConnections = metaData.getMaxConnections();
                if (driverMaxConnections > 0
                        && this._maxConnectionsNum > driverMaxConnections) {
                    this._maxConnectionsNum = driverMaxConnections;
                }
            }catch (SQLException e){
                logger.warn("获取数据库连接信息失败");
            }
        }
        _connectionPool.addElement(singleConn);
    }
    /*
     * @return 返回一个可用的数据库连接
     */
    private SingleConnection getFreeConnection(){
        // 从连接池中获得一个可用的数据库连接
        SingleConnection conn = findFreeConnection();
        if (conn == null) {
            // 如果目前连接池中没有可用的连接,创建一些连接
            createConnection();
            // 重新从池中查找是否有可用连接
            conn = findFreeConnection();
            if (conn == null) {
                // 如果创建连接后仍获得不到可用的连接，则返回 null
                return null;
            }
        }
        return conn;
    }
    /**
     * 查找连接池中所有的连接，查找一个可用的数据库连接， 如果没有可用的连接，返回 null
     *
     * @return 返回一个可用的数据库连接
     */

    private SingleConnection findFreeConnection(){
        SingleConnection pConn = null;
        // 获得连接池向量中所有的对象
        Enumeration enumerate = _connectionPool.elements();
        // 遍历所有的对象，看是否有可用的连接
        while (enumerate.hasMoreElements()) {
            pConn = (SingleConnection) enumerate.nextElement();
            if (!pConn.isBusy()) {
                // 如果此对象不忙，则获得它的数据库连接并把它设为忙
                pConn.setInBusy(true);
                try {
                    // 测试此连接是否可用
                    if (pConn.getConnection().isClosed() || !pConn.getConnection().isValid(0)) {
                        // 如果此连接不可再用了，则创建一个新的连接，
                        // 并替换此不可用的连接对象，如果创建失败，返回 null
                        _connectionPool.removeElement(pConn);
                        pConn = new SingleConnection();
                        _connectionPool.addElement(pConn);
                    }
                }catch (SQLException e){
                    logger.error("判断连接不可用时出错"+e.getMessage());
                }
                break; // 己经找到一个可用的连接，退出
            }
        }
        return pConn;// 返回找到到的可用连接
    }

    public void dropConnection(SingleConnection conn){
        conn.setInBusy(false);
    }
}
