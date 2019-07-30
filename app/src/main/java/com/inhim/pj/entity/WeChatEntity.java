package com.inhim.pj.entity;

public class WeChatEntity {
    public class Data {
        private String openId;

        public void setOpenId(String openId){
            this.openId = openId;
        }
        public String getOpenId(){
            return this.openId;
        }

    }
/*=================================*/

    public class Map {
        private Data data;

        private boolean bindVipUser;

        public void setData(Data data){
            this.data = data;
        }
        public Data getData(){
            return this.data;
        }
        public void setBindVipUser(boolean bindVipUser){
            this.bindVipUser = bindVipUser;
        }
        public boolean getBindVipUser(){
            return this.bindVipUser;
        }

    }
/*=================================*/

        private String msg;

        private int code;

        private Map map;

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
        public void setMap(Map map){
            this.map = map;
        }
        public Map getMap(){
            return this.map;
        }
}
