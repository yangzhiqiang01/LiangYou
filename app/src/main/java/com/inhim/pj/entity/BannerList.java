package com.inhim.pj.entity;

import java.util.List;

public class BannerList {
    public class Data {
        private int bannerId;

        private String imgUrl;

        private String link;

        private int type;

        private int doubSort;

        private int weight;

        private int intStatus;

        private String createTime;

        private String updateTime;

        private String typeText;

        public void setBannerId(int bannerId){
            this.bannerId = bannerId;
        }
        public int getBannerId(){
            return this.bannerId;
        }
        public void setImgUrl(String imgUrl){
            this.imgUrl = imgUrl;
        }
        public String getImgUrl(){
            return this.imgUrl;
        }
        public void setLink(String link){
            this.link = link;
        }
        public String getLink(){
            return this.link;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setDoubSort(int doubSort){
            this.doubSort = doubSort;
        }
        public int getDoubSort(){
            return this.doubSort;
        }
        public void setWeight(int weight){
            this.weight = weight;
        }
        public int getWeight(){
            return this.weight;
        }
        public void setIntStatus(int intStatus){
            this.intStatus = intStatus;
        }
        public int getIntStatus(){
            return this.intStatus;
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
/**=================================*/

        private String msg;

        private int code;

        private List<Data> data ;

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
        public void setData(List<Data> data){
            this.data = data;
        }
        public List<Data> getData(){
            return this.data;
        }
}
