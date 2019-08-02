package com.inhim.pj.entity;

public class AboutEntity {
    public class AppAbout {
        private int appAboutId;

        private String name;

        private String type;

        private String content;

        private String createTime;

        private String updateTime;

        private String typeText;

        public void setAppAboutId(int appAboutId){
            this.appAboutId = appAboutId;
        }
        public int getAppAboutId(){
            return this.appAboutId;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
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
        public void setTypeText(String typeText){
            this.typeText = typeText;
        }
        public String getTypeText(){
            return this.typeText;
        }

    }
/*=================================*/

        private String msg;

        private AppAbout appAbout;

        private int code;

        public void setMsg(String msg){
            this.msg = msg;
        }
        public String getMsg(){
            return this.msg;
        }
        public void setAppAbout(AppAbout appAbout){
            this.appAbout = appAbout;
        }
        public AppAbout getAppAbout(){
            return this.appAbout;
        }
        public void setCode(int code){
            this.code = code;
        }
        public int getCode(){
            return this.code;
        }

    }
