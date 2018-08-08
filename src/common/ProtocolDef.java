package common;

public class ProtocolDef {
    /**账号建立出错**/
    public static int MSG_REGISTER_ERROR = 1;
    /**账号不存在**/
    public static int MSG_LOGIN_NONE = 2;

    /**----------通讯协议-----------**/
    /**查询玩家个人信息**/
    public static int CMD_QUERY_USER_INFO = 1;
    /**查询玩家任务和属性信息**/
    public static int CMD_QUERY_TASKS_ATTRS = 2;
    /**查询玩家技能和装备信息**/
    public static int CMD_QUERY_SKILL_EQUIPS = 3;
    /**改名**/
    public static int CMD_CHANGE_NAME = 4;
}
