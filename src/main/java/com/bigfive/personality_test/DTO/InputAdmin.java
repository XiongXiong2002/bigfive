package com.bigfive.personality_test.DTO;


public class InputAdmin {
    private String userName;
    private String passWord;

    public InputAdmin(String userName,String passWord){
        this.passWord =passWord;
        this.userName =userName;
    }

    public void setUserName(String userName){
        this.userName =userName;
    }

    public void setPassWord(String passWord){
        this.passWord =passWord;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassWord(){
        return passWord;
    }
    

}
