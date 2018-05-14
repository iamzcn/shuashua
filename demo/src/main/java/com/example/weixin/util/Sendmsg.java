package com.example.weixin.util;

import com.example.weixin.pojo.Template;

import net.sf.json.JSONObject;

public class Sendmsg {

	
	public static boolean sendTemplateMsg(String token,Template template) {  
        
        boolean flag=false;  
          
        String requestUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";  
        requestUrl=requestUrl.replace("ACCESS_TOKEN", token);  
        
        JSONObject jsonResult=CommonUtil.httpsRequest(requestUrl, "POST", template.toJSON());  
        if(jsonResult!=null){  
            int errorCode=jsonResult.getInt("errcode");  
            String errorMessage=jsonResult.getString("errmsg");  
            if(errorCode==0){  
                flag=true;  
            }else{  
                System.out.println("模板消息发送失败:"+errorCode+","+errorMessage);  
                flag=false;  
            }  
        }  
        return flag;  
          
          
          
}

	
	
}
