package com.inhim.pj.dowloadvedio.domain;



import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * Created by renpingqing on 17/1/19.
 */
public class MyBusinessInfo extends LitePalSupport implements Serializable{
    /**
     * 下载状态
     */
    public static final String DOWNLOAD = "download";    // 下载中
    public static final String DOWNLOAD_PAUSE = "pause"; // 下载暂停
    public static final String DOWNLOAD_WAIT = "wait";  // 等待下载
    public static final String DOWNLOAD_CANCEL = "cancel"; // 下载取消
    public static final String DOWNLOAD_OVER = "over";    // 下载结束
    public static final String DOWNLOAD_ERROR = "error";  // 下载出错

    public static final long TOTAL_ERROR = -1;//获取进度失败
    private int readerId;

    private String cover;

    private String title;

    private String synopsis;

    private String content;

    private int readerTypeId;
    private String url;
    private String filePath;
    //用来判断是否播放及播放进度是多少
    private long progress;
    private String collectionStatus;
    private int type;
    private String fileName;
    public MyBusinessInfo(String url) {
        this.url = url;
    }
    public MyBusinessInfo(String url, String downloadStatus) {
        this.url = url;
        this.downloadStatus = downloadStatus;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    private String downloadStatus;
    private long total;
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

    public long getProgress() {
      return progress;
    }

    public void setProgress(long progress) {
      this.progress = progress;
    }
    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}