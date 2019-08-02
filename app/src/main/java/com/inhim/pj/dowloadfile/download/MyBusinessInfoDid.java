package com.inhim.pj.dowloadfile.download;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * Created by renpingqing on 17/1/19.
 */
public class MyBusinessInfoDid extends LitePalSupport implements Serializable{
    //readerId是唯一的，
    @Column(unique = true)
    private String readerId;

    private String cover;
    private long total;
    private String title;

    private String synopsis;

    private String content;

    private int readerTypeId;
    private String url;
    private String filePath;

    //用来判断是否播放及播放进度是多少
    //用来判断是否播放及播放进度是多少
    private String progressText;
    private int progress;
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
    public void setReaderId(String readerId){
        this.readerId = readerId;
    }
    public String getReaderId(){
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
