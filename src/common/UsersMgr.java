package common;

import databaseMgr.vo.UserInfo;

import java.util.Dictionary;
import java.util.Hashtable;

public class UsersMgr {
    private Dictionary<Integer,UserInfo> _usersDic = new Hashtable<Integer,UserInfo>();
    private static UsersMgr _instance;

    public static UsersMgr getInstance(){
        if(_instance==null){
            _instance = new UsersMgr();
        }
        return _instance;
    }

    public void addUser(UserInfo user){
        _usersDic.put(user.getId(),user);
    }

    public UserInfo getUser(int id){
        return _usersDic.get(id);
    }
}
