package com.inhim.pj.entity;

public class HistoryEntity {
    public class ReaderEntity {
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
    }
/*=================================*/

    public class List {
        private int vipBrowsingHistoryId;

        private int readerId;

        private int vipUserId;

        private String createTime;

        private String updateTime;

        private String timeText;

        private ReaderEntity readerEntity;

        public void setVipBrowsingHistoryId(int vipBrowsingHistoryId){
            this.vipBrowsingHistoryId = vipBrowsingHistoryId;
        }
        public int getVipBrowsingHistoryId(){
            return this.vipBrowsingHistoryId;
        }
        public void setReaderId(int readerId){
            this.readerId = readerId;
        }
        public int getReaderId(){
            return this.readerId;
        }
        public void setVipUserId(int vipUserId){
            this.vipUserId = vipUserId;
        }
        public int getVipUserId(){
            return this.vipUserId;
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
        public void setTimeText(String timeText){
            this.timeText = timeText;
        }
        public String getTimeText(){
            return this.timeText;
        }
        public void setReaderEntity(ReaderEntity readerEntity){
            this.readerEntity = readerEntity;
        }
        public ReaderEntity getReaderEntity(){
            return this.readerEntity;
        }

    }
/*=================================*/

    public class Page {
        private int totalCount;

        private int pageSize;

        private int totalPage;

        private int currPage;

        private java.util.List<List> list ;

        public void setTotalCount(int totalCount){
            this.totalCount = totalCount;
        }
        public int getTotalCount(){
            return this.totalCount;
        }
        public void setPageSize(int pageSize){
            this.pageSize = pageSize;
        }
        public int getPageSize(){
            return this.pageSize;
        }
        public void setTotalPage(int totalPage){
            this.totalPage = totalPage;
        }
        public int getTotalPage(){
            return this.totalPage;
        }
        public void setCurrPage(int currPage){
            this.currPage = currPage;
        }
        public int getCurrPage(){
            return this.currPage;
        }
        public void setList(java.util.List<List> list){
            this.list = list;
        }
        public java.util.List<List> getList(){
            return this.list;
        }

    }
/*=================================*/
        private String msg;

        private int code;

        private Page page;

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
        public void setPage(Page page){
            this.page = page;
        }
        public Page getPage(){
            return this.page;
        }
}
