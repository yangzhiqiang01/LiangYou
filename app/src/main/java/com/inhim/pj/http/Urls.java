package com.inhim.pj.http;

import android.os.Environment;

import java.io.File;

public class Urls {
    public static String getFilePath() {
        //String name=getExternalSdCardPath()+"/Liangyuan/";
        String name;
        String filename1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (filename1 == null) {
            String filename2 = Environment.getRootDirectory().getAbsolutePath();//获取手机根目录
            name = filename2 + "/Liangyou/";
        } else {
            name = filename1 + "/Liangyou/";
        }
        return name;
    }
    public static void getSDPath(){
        File sdDir = null;
        File sdDir1 = null;
        File sdDir2 = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            sdDir1 = Environment.getDataDirectory();
            sdDir2 =Environment.getRootDirectory();
        }
        System.out.println("getExternalStorageDirectory(): "+sdDir.toString());
        System.out.println("getDataDirectory(): "+sdDir1.toString());
        System.out.println("getRootDirectory(): "+sdDir2.toString());
    }
    private static String urlPath = "http://39.105.154.75:9091/api/";
    //登录
    public static String onLogin = urlPath + "login";
    //退出登录
    public static String onLogout = urlPath + "logout";
    //注册
    public static String onRegister = urlPath + "register";

    //发送验证码
    public static String sendSMS(String mobile) {
        return urlPath + "common/sendSMS/" + mobile;
    }

    /**
     * POST /reader/list/{page}/{limit}
     * {
     * "readerStyleId": "string",
     * "readerStyleValueId": "string",
     * <p>
     * 搜索使用可以不传
     * "readerTypeId": "string",
     * "title": "string"
     * }
     * 获取读物列表
     */
    public static String getReaderList(int page, int limit, String order) {
        return urlPath + "reader/list/" + page + "/" + limit + "?order=create_time=" + order;
    }

    /**
     * GET /banner/list/{type}
     * 根据类型查询banner列表
     * <p>
     * 类型 {1: 欢迎页, 2:阅读, 3: 视频, 4: 聆听}
     */
    public static String getBannerList(int type) {
        return urlPath + "banner/list/" + type;
    }

    /**
     * GET /reader/style/value/list/{code}
     * 根据编码查询属性值
     */
    public static String getReaderStyle(String code) {
        return urlPath + "reader/style/value/list/" + code;
    }

    /**
     * http://39.105.154.75:9091/api/reader/type/list/1/10?type=2
     */
    public static String getReaderTypeList(int page, int limit, String type) {
        if (type.equals("")) {
            return urlPath + "reader/type/list/" + page + "/" + limit;
        } else {
            return urlPath + "reader/type/list/" + page + "/" + limit + "?type=" + type;
        }
    }

    /**
     * GET /mine/userInfo
     * 获取用户信息
     */
    public static String getUserInfo = urlPath + "mine/userInfo";
    /**
     * 个人收藏接口 : Api Collection Controller Show/Hide List Operations Expand Operations
     * DELETE /collection/delete
     * 删除个人收藏
     */
    public static String collectionDelete = urlPath + "collection/delete";

    /**
     * POST /collection/list/{page}/{limit}
     * 获取个人收藏列表
     */
    public static String getCollectionList(int page, int limit, String type) {
        return urlPath + "collection/list/" + page + "/" + limit + "?type=" + type;
    }

    /**
     * POST /collection/reader/{readerId}
     * 收藏与取消
     */
    public static String collectionReader(int readerId) {
        return urlPath + "collection/reader/" + readerId;
    }

    /**
     * GET /collection/type/list
     * 获取个人浏览记录类型列表
     */
    public static String collectionTypeList = urlPath + "collection/type/list";

    /*get  /browsingHistory/list/{page}/{limit}
    获取个人浏览记录列表*/
    public static String browsingHistory(int page, int limit) {
        return urlPath + "browsingHistory/list/" + page + "/" + limit;
    }

    /**
     * app/about/{type}
     * app用户协议
     */
    public static String appAbout(String type) {
        return urlPath + "app/about/" + type;
    }

    /**
     * GET /reader/info/{readerId}
     * 根据ID获取读物
     */
    public static String getReaderInfo(int readerId, String token) {
        return urlPath + "reader/info/" + readerId + "?token=" + token;
    }

    /**
     * put  /mine/updateMobile
     * 修改手机号
     * http://39.105.154.75:9091/api/mine/updateMobile?mobile=13772914588&validate=122
     */
    public static String updateMobile(String mobile, String validate) {
        return urlPath + "mine/updateMobile?mobile=" + mobile + "&validate=" + validate;
    }

    /**
     * PUT /mine/userInfo/update
     * 修改用户信息
     */
    public static String updateUserInfo = urlPath + "mine/userInfo/update";
    /**
     * browsingHistory/delete/all
     * 清空个人浏览记录列表
     */
    public static String deleteAllHistory = urlPath + "browsingHistory/delete/all";

    /**DELETE /browsingHistory/delete
            删除个人浏览记录*/
    public static String deleteHistory = urlPath + "browsingHistory/delete";
    /**POST /common/file/upload
            文件上传*/
    public static String fileUpload = urlPath + "common/file/upload";
}
