package com.inhim.pj.entity;

import java.io.Serializable;
import java.util.List;

public class ReaderInfo implements Serializable {
    public class ReaderAttrList implements Serializable {
        private int readerAttrId;

        private int readerStyleId;

        private int readerStyleValueId;

        private int readerId;

        private String readerStyleValue;

        private int type;

        private String createTime;

        private String updateTime;

        public void setReaderAttrId(int readerAttrId){
            this.readerAttrId = readerAttrId;
        }
        public int getReaderAttrId(){
            return this.readerAttrId;
        }
        public void setReaderStyleId(int readerStyleId){
            this.readerStyleId = readerStyleId;
        }
        public int getReaderStyleId(){
            return this.readerStyleId;
        }
        public void setReaderStyleValueId(int readerStyleValueId){
            this.readerStyleValueId = readerStyleValueId;
        }
        public int getReaderStyleValueId(){
            return this.readerStyleValueId;
        }
        public void setReaderId(int readerId){
            this.readerId = readerId;
        }
        public int getReaderId(){
            return this.readerId;
        }
        public void setReaderStyleValue(String readerStyleValue){
            this.readerStyleValue = readerStyleValue;
        }
        public String getReaderStyleValue(){
            return this.readerStyleValue;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
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
    public class Reader implements Serializable {
        private int readerId;

        private String cover;

        private String title;

        private String synopsis;

        private String content;

        private int readerTypeId;

        private String readerTypeIdLink;

        private String readerStyleIdLink;

        private String readerStyleValueIdLink;

        private int readAmount;

        private String createTime;

        private String updateTime;

        private boolean intRecommend;

        private String url;

        private int type;

        private String doubSort;

        private boolean collectionStatus;

        private String tagName;

        private String timeText;

        private String readerTypeText;

        private List<ReaderAttrList> readerAttrList ;

        public void setReaderId(int readerId){
            this.readerId = readerId;
        }
        public int getReaderId(){
            return this.readerId;
        }
        public void setCover(String cover){
            this.cover = cover;
        }
        public String getCover(){
            return this.cover;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setSynopsis(String synopsis){
            this.synopsis = synopsis;
        }
        public String getSynopsis(){
            return this.synopsis;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setReaderTypeId(int readerTypeId){
            this.readerTypeId = readerTypeId;
        }
        public int getReaderTypeId(){
            return this.readerTypeId;
        }
        public void setReaderTypeIdLink(String readerTypeIdLink){
            this.readerTypeIdLink = readerTypeIdLink;
        }
        public String getReaderTypeIdLink(){
            return this.readerTypeIdLink;
        }
        public void setReaderStyleIdLink(String readerStyleIdLink){
            this.readerStyleIdLink = readerStyleIdLink;
        }
        public String getReaderStyleIdLink(){
            return this.readerStyleIdLink;
        }
        public void setReaderStyleValueIdLink(String readerStyleValueIdLink){
            this.readerStyleValueIdLink = readerStyleValueIdLink;
        }
        public String getReaderStyleValueIdLink(){
            return this.readerStyleValueIdLink;
        }
        public void setReadAmount(int readAmount){
            this.readAmount = readAmount;
        }
        public int getReadAmount(){
            return this.readAmount;
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
        public void setIntRecommend(boolean intRecommend){
            this.intRecommend = intRecommend;
        }
        public boolean getIntRecommend(){
            return this.intRecommend;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setDoubSort(String doubSort){
            this.doubSort = doubSort;
        }
        public String getDoubSort(){
            return this.doubSort;
        }
        public void setCollectionStatus(boolean collectionStatus){
            this.collectionStatus = collectionStatus;
        }
        public boolean getCollectionStatus(){
            return this.collectionStatus;
        }
        public void setTagName(String tagName){
            this.tagName = tagName;
        }
        public String getTagName(){
            return this.tagName;
        }
        public void setTimeText(String timeText){
            this.timeText = timeText;
        }
        public String getTimeText(){
            return this.timeText;
        }
        public void setReaderTypeText(String readerTypeText){
            this.readerTypeText = readerTypeText;
        }
        public String getReaderTypeText(){
            return this.readerTypeText;
        }
        public void setReaderAttrList(List<ReaderAttrList> readerAttrList){
            this.readerAttrList = readerAttrList;
        }
        public List<ReaderAttrList> getReaderAttrList(){
            return this.readerAttrList;
        }

    }
/*=================================*/

        private String msg;

        private int code;

        private Reader reader;

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
        public void setReader(Reader reader){
            this.reader = reader;
        }
        public Reader getReader(){
            return this.reader;
        }

    }