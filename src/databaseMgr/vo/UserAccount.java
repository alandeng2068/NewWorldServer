package databaseMgr.vo;

public class UserAccount {
    private int id;
    private String name;
    private String psw;
    private int register;
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

    public void setPsw(String psw){
        this.psw = psw;
    }
    public String getPsw(){
        return this.psw;
    }

    public void setRegister(int flag){
        this.register = flag;
    }
    public int getRegister(){
        return this.register;
    }
    public String toString(){
        return this.id+","+this.name+","+this.psw;
    }
}
