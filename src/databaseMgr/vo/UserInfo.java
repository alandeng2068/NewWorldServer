package databaseMgr.vo;

public class UserInfo {
    /**玩家id**/
    private int id;
    /**玩家名字*/
    private String name;
    /**玩家等级*/
    private int level;
    /**新手引导状态，从0开始，-1表示完成新手引导**/
    private int guide;
    private int exp;
    /**宠物id**/
    private int id_pet;
    private int bag_maxnum;
    /**技能id列表**/
    private int[] ids_skill;
    /**游戏获得的金币**/
    private int gold;
    /**玩家充值的金额**/
    private int money;
    /**当前所在的地图**/
    private int id_map;
    /**当前拥有的任务的列表**/
    private int[] id_tasks;
    private int attack;
    private int m_attack;
    private int def;
    private int m_def;
    private int equip_head;
    private int equip_body;
    private int equip_pants;
    private int equip_shoes;
    private int equip_weapon;

    public void setUserInfo(int id,String name,int level,int guide,int exp,int id_pet,int bag_maxnum,int[] ids_skill,int gold,int money,int id_map,int[] id_tasks,
                            int attack,int m_attack,int def,int m_def,int equip_head,int equip_body,int equip_pants,int equip_shoes,int equip_weapon){
        this.id = id;
        this.name = name;
        this.level = level;
        this.guide = guide;
        this.exp = exp;
        this.id_pet = id_pet;
        this.bag_maxnum = bag_maxnum;
        this.ids_skill = ids_skill;
        this.gold = gold;
        this.money = money;
        this.id_map = id_map;
        this.id_tasks = id_tasks;
        this.attack = attack;
        this.m_attack = m_attack;
        this.def = def;
        this.m_def = m_def;
        this.equip_head = equip_head;
        this.equip_body = equip_body;
        this.equip_pants = equip_pants;
        this.equip_shoes = equip_shoes;
        this.equip_weapon = equip_weapon;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return this.level;
    }
    public void setGuide(int guide){
        this.guide = guide;
    }
    public int getGuide(){
        return this.guide;
    }
    public void setExp(int exp){
        this.exp = exp;
    }
    public int getExp(){
        return this.exp;
    }
    public void setId_pet(int id_pet){
        this.id_pet = id_pet;
    }
    public int getId_pet(){
        return this.id_pet;
    }

    public void setBag_maxnum(int bag_maxnum) {
        this.bag_maxnum = bag_maxnum;
    }

    public int getBag_maxnum() {
        return bag_maxnum;
    }

    public void setIds_skill(int[] ids_skill) {
        this.ids_skill = ids_skill;
    }

    public int[] getIds_skill() {
        return ids_skill;
    }
    public void setGold(int gold){
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setId_map(int id_map) {
        this.id_map = id_map;
    }

    public int getId_map() {
        return id_map;
    }

    public void setId_tasks(int[] id_tasks) {
        this.id_tasks = id_tasks;
    }

    public int[] getId_tasks() {
        return id_tasks;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setM_attack(int m_attack) {
        this.m_attack = m_attack;
    }

    public int getM_attack() {
        return m_attack;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getDef() {
        return def;
    }

    public void setM_def(int m_def) {
        this.m_def = m_def;
    }

    public int getM_def() {
        return m_def;
    }

    public void setEquip_body(int equip_body) {
        this.equip_body = equip_body;
    }

    public int getEquip_body() {
        return equip_body;
    }

    public void setEquip_head(int equip_head) {
        this.equip_head = equip_head;
    }

    public int getEquip_head() {
        return equip_head;
    }

    public void setEquip_pants(int equip_pants) {
        this.equip_pants = equip_pants;
    }

    public int getEquip_pants() {
        return equip_pants;
    }

    public void setEquip_shoes(int equip_shoes) {
        this.equip_shoes = equip_shoes;
    }

    public int getEquip_shoes() {
        return equip_shoes;
    }

    public void setEquip_weapon(int equip_weapon) {
        this.equip_weapon = equip_weapon;
    }

    public int getEquip_weapon() {
        return equip_weapon;
    }

    @Override
    public String toString() {
        return this.id +","+this.name+","+this.level+","+this.guide+","+this.exp+","+this.id_pet+","+this.bag_maxnum +","+this.ids_skill  +","+this.gold+","+this.money +","+this.id_map+","+this.id_tasks +","+this.attack +","+this.m_attack+","+this.def +","+this.m_def+","+this.equip_head+","+this.equip_body +","+this.equip_pants +","+this.equip_shoes +","+this.equip_weapon;
    }
}
