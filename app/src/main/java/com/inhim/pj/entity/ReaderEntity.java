package com.inhim.pj.entity;

import java.io.Serializable;

public class ReaderEntity implements Serializable {
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

    private String collectionStatus;

    private String tagName;

    private String timeText;

    private String readerAttrList;

    private String readerAttachmentList;

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
    public void setCollectionStatus(String collectionStatus){
        this.collectionStatus = collectionStatus;
    }
    public String getCollectionStatus(){
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
    public void setReaderAttrList(String readerAttrList){
        this.readerAttrList = readerAttrList;
    }
    public String getReaderAttrList(){
        return this.readerAttrList;
    }
    public void setReaderAttachmentList(String readerAttachmentList){
        this.readerAttachmentList = readerAttachmentList;
    }
    public String getReaderAttachmentList(){
        return this.readerAttachmentList;
    }

}