package com.inhim.pj.entity;


public class CommonDictEntity {
    public class List {
        private int id;

        private String name;

        private int parentId;

        private String code;

        private String value;

        private int orderNum;

        private String remark;

        private int delFlag;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setParentId(int parentId){
            this.parentId = parentId;
        }
        public int getParentId(){
            return this.parentId;
        }
        public void setCode(String code){
            this.code = code;
        }
        public String getCode(){
            return this.code;
        }
        public void setValue(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
        public void setOrderNum(int orderNum){
            this.orderNum = orderNum;
        }
        public int getOrderNum(){
            return this.orderNum;
        }
        public void setRemark(String remark){
            this.remark = remark;
        }
        public String getRemark(){
            return this.remark;
        }
        public void setDelFlag(int delFlag){
            this.delFlag = delFlag;
        }
        public int getDelFlag(){
            return this.delFlag;
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
