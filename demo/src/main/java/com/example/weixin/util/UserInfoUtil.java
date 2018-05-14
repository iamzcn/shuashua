package com.example.weixin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.pojo.Constant;
import com.example.weixin.pojo.WXUser;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class UserInfoUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);

	//获取code的请求地址
    public static String Get_Code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";
    //替换字符串
    public static String getCode(String APPID, String REDIRECT_URI,String SCOPE) {
        return String.format(Get_Code,APPID,REDIRECT_URI,SCOPE);
    }

    //获取Web_access_token https的请求地址
    public static String Web_access_tokenhttps = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    //替换字符串
    public static String getWebAccess(String APPID, String SECRET,String CODE) {
        return String.format(Web_access_tokenhttps, APPID, SECRET,CODE);
    }
    
  //拉取用户信息的请求地址
    public static String User_Message = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    //替换字符串
    public static String getUserMessage(String access_token, String openid) {
        return String.format(User_Message, access_token,openid);
    }
    
    //拉取用户信息的请求地址
    public static String User_info = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    //替换字符串
    public static String getUserInfo(String access_token, String openid) {
        return String.format(User_info, access_token,openid);
    }
    
    
    public static WXUser getWXUser(String CODE) {
    	
    	String WebAccessToken = "";
    	String openId = "";
    	WXUser user = new WXUser();
    	
    	// 替换字符串，获得请求access token URL
        String tokenUrl = UserInfoUtil.getWebAccess(Constant.appId, Constant.appSecret, CODE);
        logger.info("第二步:get Access Token URL:{}", tokenUrl);

        // 通过https方式请求获得web_access_token
        String response = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

        JSONObject jsonObject = JSONObject.fromObject(response);
        logger.info("请求到的Web Access Token:{}", jsonObject);
        
        if (null != jsonObject) {
            try {

                WebAccessToken = jsonObject.getString("access_token");
                openId = jsonObject.getString("openid");
                logger.info("获取web access_token成功!");
                logger.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);

                // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                logger.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                // 通过https方式请求获得用户信息响应
                String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

                JSONObject userMessageJsonObject = JSONObject.fromObject(userMessageResponse);

                logger.info("用户信息:{}", userMessageJsonObject.toString());

                if (userMessageJsonObject != null) {
                    
                	try {
	                    //用户昵称
	                    String nickName = userMessageJsonObject.getString("nickname");
	                    user.setNickname(nickName);
                	}catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}
                	try {
	                    //用户性别
	                    String sex = userMessageJsonObject.getString("sex");
	                    sex = (sex.equals("1")) ? "男" : "女";
                	}catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}    

                	try {
	                    //用户唯一标识
	                    openId = userMessageJsonObject.getString("openid");
	                    user.setOpenid(openId);
                	}catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}
                    try {
                    	user.setCity(userMessageJsonObject.getString("city"));
                    }catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}
                    
                    
                    
                    try {
                    	user.setSubscribe_scene(userMessageJsonObject.getString("subscribe_scene"));
                    }catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}
                    try {
                    	user.setSubscribe(userMessageJsonObject.getString("subscribe"));
                    }catch(JSONException e1) {
                		logger.warn("获取微信用户信息失败, error: [" + e1.getMessage() + "]");
                	}
                }
            } catch (Exception e) {
                logger.error("获取微信用户信息失败, error: [" + e.getMessage() + "]");
            }
        }

        return user;

    }
}
