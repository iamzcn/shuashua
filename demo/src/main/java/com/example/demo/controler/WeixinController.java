package com.example.demo.controler;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.util.TaskUtil;
import com.example.demo.weixin.service.core.CoreService;
import com.example.weixin.pojo.AccessToken;
import com.example.weixin.pojo.Template;
import com.example.weixin.pojo.TemplateParam;
import com.example.weixin.util.Sendmsg;

@RestController
public class WeixinController {

	private String TOKEN = "royzrliang";
	
	@Autowired
    private CoreService coreService;
	
	public static final Logger log = LoggerFactory.getLogger(WeixinController.class);
	
	@RequestMapping(value = "/sell/test",method = RequestMethod.POST, produces = "application/xml; charset=UTF-8")
    public  void post(HttpServletRequest req, HttpServletResponse response){

        String respMessage = coreService.processRequest(req);
        
        if(respMessage != null) {
	        response.setCharacterEncoding("utf-8");  
	        PrintWriter out = null;  
	        try {  
	            out = response.getWriter();  
	            out.write(respMessage);  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        out.close();  
        }else {
        	//log.warn("respMessage is null, no message sent out.");
        }
    }
	
	@RequestMapping(value = "/guahao/send",method=RequestMethod.GET)
    public Boolean sendmsg(@RequestParam("wxid") String wxid, @RequestParam("text") String text) {
        Boolean flag = true;
		Template tem=new Template();  
		tem.setTemplateId("nM6Kj6ux7e6XHfVMwzzQbqbTjkQOjzltWblTn4lyOBw");  
		tem.setTopColor("#00DD00");  
		tem.setToUser("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");  
		tem.setUrl("");  
		          
		List<TemplateParam> paras=new ArrayList<TemplateParam>();  
		paras.add(new TemplateParam("first","有号啦","#FF3333"));  
		paras.add(new TemplateParam("keyword1",text,"#0044BB"));  
		paras.add(new TemplateParam("remark","Remark","#AAAAAA"));  
		          
		tem.setTemplateParamList(paras);  
		String accessToken = AccessToken.getInstance().getToken();
		
		if(wxid != null && wxid.length() > 0) {
			String ids[] = wxid.split(",");
			for(int i=0;i<ids.length;i++) {
//				String id[] = ids[i].split("_");
//				String one = "oeM4n1D0CwGMiEbat7BhRtQ7wE8A"; //default send to me
//				if(id.length == 1) one = id[0];
//				if(id.length == 2) one = id[1];
				String one = ids[i];
				tem.setToUser(one);  
				Boolean tmp = false;
				try {
					tmp = Sendmsg.sendTemplateMsg(accessToken,tem);
				}catch(Exception e) {
					
				}
				if(!tmp) flag = false;
				
			}
		}
		
		return flag;  
    }
	
	@RequestMapping(value = "/sell/test",method=RequestMethod.GET)
    public String test(@RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("echostr") String echostr) {
        
        //排序
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String myString = sha1(sortString);
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("signature passed");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            System.out.println("signature failed");
            return "";
        }
    }

    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
