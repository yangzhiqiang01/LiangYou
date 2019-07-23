package com.inhim.pj.entity;

public class LoginEntity {
    private String msg;

    private int code;

    private long expire;

    private String token;

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setExpire(long expire){
        this.expire = expire;
    }
    public long getExpire(){
        return this.expire;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
}
