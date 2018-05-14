package com.example.weixin.util;

import com.example.weixin.pojo.AccessToken;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class AccessTokenUtil {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = AccessToken.getInstance();
				accessToken.setToken(jsonObject.getString("access_token"));
				System.out.println(accessToken.getToken());
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				System.out.println("获取token失败 errcode:" + jsonObject.getInt("errcode") + " errmsg:"
						+ jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

}
