package com.inhim.pj.entity;

import java.util.List;

public class CollectionTypeList {
    public class TypeList {
        private int readerTypeId;

        private String name;

        private String pyFull;

        private String pyShort;

        private int parentId;

        private String parentName;

        private String parentIdLink;

        private int level;

        private double doubSort;

        private String icon;

        private int type;

        private int isShow;

        private String createTime;

        private String updateTime;

        private String open;

        public void setReaderTypeId(int readerTypeId){
            this.readerTypeId = readerTypeId;
        }
        public int getReaderTypeId(){
            return this.readerTypeId;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setPyFull(String pyFull){
            this.pyFull = pyFull;
        }
        public String getPyFull(){
            return this.pyFull;
        }
        public void setPyShort(String pyShort){
            this.pyShort = pyShort;
        }
        public String getPyShort(){
            return this.pyShort;
        }
        public void setParentId(int parentId){
            this.parentId = parentId;
        }
        public int getParentId(){
            return this.parentId;
        }
        public void setParentName(String parentName){
            this.parentName = parentName;
        }
        public String getParentName(){
            return this.parentName;
        }
        public void setParentIdLink(String parentIdLink){
            this.parentIdLink = parentIdLink;
        }
        public String getParentIdLink(){
            return this.parentIdLink;
        }
        public void setLevel(int level){
            this.level = level;
        }
        public int getLevel(){
            return this.level;
        }
        public void setDoubSort(double doubSort){
            this.doubSort = doubSort;
        }
        public double getDoubSort(){
            return this.doubSort;
        }
        public void setIcon(String icon){
            this.icon = icon;
        }
        public String getIcon(){
            return this.icon;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setIsShow(int isShow){
            this.isShow = isShow;
        }
        public int getIsShow(){
            return this.isShow;
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
        public void setOpen(String open){
            this.open = open;
        }
        public String getOpen(){
            return this.open;
        }

    }
/*=================================*/

        private String msg;

        private int code;

        private List<TypeList> typeList ;

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
        public void setTypeList(List<TypeList> typeList){
            this.typeList = typeList;
        }
        public List<TypeList> getTypeList(){
            return this.typeList;
        }
}
