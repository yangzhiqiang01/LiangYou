package com.inhim.pj.dowloadvedio.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


/**
 * Created by renpingqing on 17/1/19.
 */
public class MyBusinessInfoDid extends DataSupport implements Serializable{
    private int readerId;

    private String cover;

    private String title;

    private String synopsis;

    private String content;

    private int readerTypeId;
    private String url;
    private String filePath;

    //用来判断是否播放及播放进度是多少
    private String progress;
    private String collectionStatus;
    private int type;
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setCollectionStatus(String collectionStatus){
        this.collectionStatus = collectionStatus;
    }
    public String getCollectionStatus(){
        return this.collectionStatus;
    }
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

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

}
