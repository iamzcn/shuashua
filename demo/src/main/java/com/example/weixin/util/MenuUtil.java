package com.example.weixin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.controler.WeixinController;
import com.example.demo.pojo.Constant;
import com.example.weixin.pojo.menu.Button;
import com.example.weixin.pojo.menu.ClickButton;
import com.example.weixin.pojo.menu.Menu;
import com.example.weixin.pojo.menu.ViewButton;

import net.sf.json.JSONObject;

public class MenuUtil {
	
	public static final Logger log = LoggerFactory.getLogger(MenuUtil.class);
	
	//create menu的请求地址
    public static String  CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    //替换字符串
    public static String getCreateMenuURL(String token) {
        return String.format(CREATE_MENU_URL, token);
    }
    
  //Query menu的请求地址
    public static String  QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
    //替换字符串
    public static String getQueryMenuURL(String token) {
        return String.format(QUERY_MENU_URL, token);
    }
    
    
    

	public static Menu initMenu(){
        Menu menu = new Menu();
//        ClickButton clickButton = new ClickButton();
//        clickButton.setKey("clickButton");
//        clickButton.setType("click");
//        clickButton.setName("clickMenu");
        
        

        ViewButton doctorShuaButton = new ViewButton();
        doctorShuaButton.setName("刷医生号源");
        doctorShuaButton.setType("view");
        doctorShuaButton.setUrl("http://" + Constant.dns + "/doctor?TARGET=home");
        
        ViewButton myShuaButton = new ViewButton();
        myShuaButton.setName("我的刷刷");
        myShuaButton.setType("view");
        myShuaButton.setUrl("http://" + Constant.dns + "/doctor?TARGET=myShuaShua");

        ClickButton clickButton1 = new ClickButton();
        clickButton1.setKey("scanButton");
        clickButton1.setType("scancode_push");
        clickButton1.setName("scanButton");

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setKey("locationButton");
        clickButton2.setType("location_select");
        clickButton2.setName("locationButton");

        Button button = new Button();
        button.setName("Menu");
        button.setSub_button(new Button[]{clickButton1,clickButton2});

        menu.setButton(new Button[]{doctorShuaButton,myShuaButton,button});
        return menu;
    }
    //创建菜单的url拼接
    public static int createMenu(String menu, String token){
        String url = getCreateMenuURL(token);
        int result = 0;
        JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(url, "POST", menu));
        if(jsonObject != null){
            result = jsonObject.getInt("errcode");
            if(result != 0)
            	log.error("Error in creating menu, error : " + jsonObject.getString("errmsg"));
        }
        return result;
    }
    //查询菜单的url的拼接
    public static JSONObject queryMenu(String token){
        String url = getQueryMenuURL(token);
        JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpRequest(url, "GET", null));
        return jsonObject;
    }
    
}
