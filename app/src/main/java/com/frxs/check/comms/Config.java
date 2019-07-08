package com.frxs.check.comms;

/**
 * Created by ewu on 2016/3/23.
 */
public class Config {
    public static final String DB_NAME = "dbname.db";

    public static final String PREFS_NAME = "MyFrefsFile";

    public static final String KEY_USER = "key_user";

    public static final String KEY_CHECK_DATE = "key_check_date"; //检测版本日期，每天只请求一次

    // 服务器网络 (0:线上环境、1:测试站点、2：演示环境、3：迁移环境、4：TF本机)
    public static int networkEnv = 0;

    public static final int TYPE_BASE = 0;
    public static final int TYPE_UPDATE = 1;

    public static String getBaseUrl(int typeUrl) {
        return getBaseUrl(typeUrl, networkEnv);
    }

    public static String getBaseUrl(int typeUrl, int networkEnv) {
        String BASE_URL = "";
        if (networkEnv == 0) { // 正式环境
            if (typeUrl == TYPE_BASE) {
//                BASE_URL = "http://apiwh.erp2.frxs.com/";
                BASE_URL = "http://124.232.138.162:5005/";
            } else {
                BASE_URL = "http://orderapi.erp2.frxs.com/api/";
            }
        } else if (networkEnv == 1) {
            if (typeUrl == TYPE_BASE) {
//                BASE_URL = "http://webapitest.erp2.frxs.cn/";
                BASE_URL = "http://113.247.234.76:5005/";
            } else {
                BASE_URL = "http://b2btest.frxs.cn/api/";
            }
        } else if (networkEnv == 2) {
            if (typeUrl == TYPE_BASE) {
                BASE_URL = "http://yfbapiwh.erp2.frxs.com/";// 预发布环境
            } else {
                BASE_URL = "http://yfbapiwh.erp2.frxs.com/api/";
            }
        } else if (networkEnv == 3) {
            if (typeUrl == TYPE_BASE) {
                BASE_URL = "http://api_wh.erp3.frxs.com/";
            } else {
                BASE_URL = "http://192.168.8.154:8089/api/";
            }
        } else if (networkEnv == 4) {
            if (typeUrl == TYPE_BASE) {
                BASE_URL = "http://192.168.8.242:8086/";
            } else {
                BASE_URL = "http://orderapi.erp2.frxs.com/api/";
            }
        } else if (networkEnv == 5) {
            if (typeUrl == TYPE_BASE) {
                //BASE_URL = "http://f3web_api.frxs.com/";// 演示环境
                //BASE_URL = "http://192.168.8.142:8090/";
                BASE_URL = "http://192.168.8.125:8086/";
                //BASE_URL = "http://192.168.8.63:8086/";
            } else {
                BASE_URL = "http://192.168.6.131:8089/api/";
            }
        }

        return BASE_URL;
    }
}
