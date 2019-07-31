package com.inhim.pj.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
    public class WechatUser  implements Serializable {
        private int wechatUserId;

        private String country;

        private String province;

        private String city;

        private String headimgurl;

        private String language;

        private String nickname;

        private int sex;

        private String openId;

        private String unionId;

        private String createTime;

        private String updateTime;

        public void setWechatUserId(int wechatUserId){
            this.wechatUserId = wechatUserId;
        }
        public int getWechatUserId(){
            return this.wechatUserId;
        }
        public void setCountry(String country){
            this.country = country;
        }
        public String getCountry(){
            return this.country;
        }
        public void setProvince(String province){
            this.province = province;
        }
        public String getProvince(){
            return this.province;
        }
        public void setCity(String city){
            this.city = city;
        }
        public String getCity(){
            return this.city;
        }
        public void setHeadimgurl(String headimgurl){
            this.headimgurl = headimgurl;
        }
        public String getHeadimgurl(){
            return this.headimgurl;
        }
        public void setLanguage(String language){
            this.language = language;
        }
        public String getLanguage(){
            return this.language;
        }
        public void setNickname(String nickname){
            this.nickname = nickname;
        }
        public String getNickname(){
            return this.nickname;
        }
        public void setSex(int sex){
            this.sex = sex;
        }
        public int getSex(){
            return this.sex;
        }
        public void setOpenId(String openId){
            this.openId = openId;
        }
        public String getOpenId(){
            return this.openId;
        }
        public void setUnionId(String unionId){
            this.unionId = unionId;
        }
        public String getUnionId(){
            return this.unionId;
        }
        public void setCreateTime(String createTime){
            this.createTime = createTime;
        }
        public String getCreateTime(){
            return this.createTime;
        }
        public void setUpdateTime(String updateTime){
            this.updateTime = updateTime;
        }
        public String getUpdateTime(){
            return this.updateTime;
        }

    }
/*=================================*/

    public class User  implements Serializable {
        private int vipUserId;

        private String username;

        private String mobile;

        private String createTime;

        private String realname;

        private String headimgurl;

        private String nation;

        private String mail;

        private String faithStatus;

        private String faithTime;

        private String baptismTime;

        private String maritalStatus;

        private String occupation;

        private String educationLevel;

        private String childrenStatus;

        private String openId;

        private WechatUser wechatUser;

        public void setVipUserId(int vipUserId){
            this.vipUserId = vipUserId;
        }
        public int getVipUserId(){
            return this.vipUserId;
        }
        public void setUsername(String username){
            this.username = username;
        }
        public String getUsername(){
            return this.username;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setCreateTime(String createTime){
            this.createTime = createTime;
        }
        public String getCreateTime(){
            return this.createTime;
        }
        public void setRealname(String realname){
            this.realname = realname;
        }
        public String getRealname(){
            return this.realname;
        }
        public void setHeadimgurl(String headimgurl){
            this.headimgurl = headimgurl;
        }
        public String getHeadimgurl(){
            return this.headimgurl;
        }
        public void setNation(String nation){
            this.nation = nation;
        }
        public String getNation(){
            return this.nation;
        }
        public void setMail(String mail){
            this.mail = mail;
        }
        public String getMail(){
            return this.mail;
        }
        public void setFaithStatus(String faithStatus){
            this.faithStatus = faithStatus;
        }
        public String getFaithStatus(){
            return this.faithStatus;
        }
        public void setFaithTime(String faithTime){
            this.faithTime = faithTime;
        }
        public String getFaithTime(){
            return this.faithTime;
        }
        public void setBaptismTime(String baptismTime){
            this.baptismTime = baptismTime;
        }
        public String getBaptismTime(){
            return this.baptismTime;
        }
        public void setMaritalStatus(String maritalStatus){
            this.maritalStatus = maritalStatus;
        }
        public String getMaritalStatus(){
            return this.maritalStatus;
        }
        public void setOccupation(String occupation){
            this.occupation = occupation;
        }
        public String getOccupation(){
            return this.occupation;
        }
        public void setEducationLevel(String educationLevel){
            this.educationLevel = educationLevel;
        }
        public String getEducationLevel(){
            return this.educationLevel;
        }
        public void setChildrenStatus(String childrenStatus){
            this.childrenStatus = childrenStatus;
        }
        public String getChildrenStatus(){
            return this.childrenStatus;
        }
        public void setOpenId(String openId){
            this.openId = openId;
        }
        public String getOpenId(){
            return this.openId;
        }
        public void setWechatUser(WechatUser wechatUser){
            this.wechatUser = wechatUser;
        }
        public WechatUser getWechatUser(){
            return this.wechatUser;
        }

    }
/*=================================*/

        private String msg;

        private int code;

        private User user;

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
        public void setUser(User user){
            this.user = user;
        }
        public User getUser(){
            return this.user;
        }


    }
