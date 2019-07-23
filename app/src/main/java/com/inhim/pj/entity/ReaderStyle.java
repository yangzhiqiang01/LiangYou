package com.inhim.pj.entity;

import java.io.Serializable;

public class ReaderStyle {
    public class ReaderStyleValue implements Serializable{
        private int readerStyleValueId;

        private int readerStyleId;

        private String readerStyleName;

        private String createTime;

        private String updateTime;

        private String value;

        private String synopsis;

        private String cover;

        private String type;

        public void setReaderStyleValueId(int readerStyleValueId){
            this.readerStyleValueId = readerStyleValueId;
        }
        public int getReaderStyleValueId(){
            return this.readerStyleValueId;
        }
        public void setReaderStyleId(int readerStyleId){
            this.readerStyleId = readerStyleId;
        }
        public int getReaderStyleId(){
            return this.readerStyleId;
        }
        public void setReaderStyleName(String readerStyleName){
            this.readerStyleName = readerStyleName;
        }
        public String getReaderStyleName(){
            return this.readerStyleName;
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
        public void setValue(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
        public void setSynopsis(String synopsis){
            this.synopsis = synopsis;
        }
        public String getSynopsis(){
            return this.synopsis;
        }
        public void setCover(String cover){
            this.cover = cover;
        }
        public String getCover(){
            return this.cover;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }

    }
/*=================================*/
    public class List implements Serializable {
        private int total;

        private ReaderStyleValue readerStyleValue;

        public void setTotal(int total){
            this.total = total;
        }
        public int getTotal(){
            return this.total;
        }
        public void setReaderStyleValue(ReaderStyleValue readerStyleValue){
            this.readerStyleValue = readerStyleValue;
        }
        public ReaderStyleValue getReaderStyleValue(){
            return this.readerStyleValue;
        }

    }
/*=================================*/

        private String msg;

        private int code;

        private java.util.List<List> list ;

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
        public void setList(java.util.List<List> list){
            this.list = list;
        }
        public java.util.List<List> getList(){
            return this.list;
        }
}
