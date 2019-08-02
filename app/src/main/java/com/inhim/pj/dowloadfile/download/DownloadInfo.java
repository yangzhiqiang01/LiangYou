package com.inhim.pj.dowloadfile.download;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by zs
 * Date：2018年 09月 12日
 * Time：13:50
 * —————————————————————————————————————
 * About: 下载管理
 * —————————————————————————————————————
 */
public class DownloadInfo extends LitePalSupport {

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
    @Column(unique = true)
    private String url;
    private String fileName;
    private String downloadStatus;
    private long total;
    private long progress;
    //readerId是唯一的，
    @Column(unique = true)
    private String readerId;

    private String cover;

    private String title;

    private String synopsis;

    private String content;
    private int readerTypeId;
    private String filePath;
    //用来判断是否播放及播放进度是多少
    private String progressText;
    private String collectionStatus;
    private int type;
    public DownloadInfo() {

    }
    public DownloadInfo(String url) {
        this.url = url;
    }

    public DownloadInfo(String url, String downloadStatus) {
        this.url = url;
        this.downloadStatus = downloadStatus;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReaderTypeId() {
        return readerTypeId;
    }

    public void setReaderTypeId(int readerTypeId) {
        this.readerTypeId = readerTypeId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
