package com.example.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.dao.SourceConfigRepository;
import com.example.demo.pojo.SourceConfig;
import com.example.demo.pojo.SourceParameter;

public class FileUtil {
	
	public static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
     *  以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *  一次性读取一个字节
     * @param @param fileName
     * @return void
     * @author jiangxueyou
     */
    
   public static void readFileByBytes(String fileName) {
       File file = new File(fileName);
       InputStream in = null;
       try {
           System.out.println("以字节为单位读取文件内容，一次读一个字节：");
           // 一次读一个字节
           in = new FileInputStream(file);
           int tempbyte;
           while ((tempbyte = in.read()) != -1) {
               System.out.write(tempbyte);
           }
           in.close();
       } catch (IOException e) {
           e.printStackTrace();
           return;
       }
       
       
      
   }
   /**
    * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
    * 一次性读取多个字节
    * @param @param fileName
    * @return void
    * @author jiangxueyou
    */
   public static void readFileByBytes2(String fileName) {
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
   }

   /**
    * 以字符为单位读取文件，常用于读文本，数字等类型的文件
    */
   public static void readFileByChars(String fileName) {
       File file = new File(fileName);
       Reader reader = null;
       try {
           System.out.println("以字符为单位读取文件内容，一次读一个字节：");
           // 一次读一个字符
           reader = new InputStreamReader(new FileInputStream(file));
           int tempchar;
           while ((tempchar = reader.read()) != -1) {
               // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
               // 但如果这两个字符分开显示时，会换两次行。
               // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
               if (((char) tempchar) != '\r') {
                   System.out.print((char) tempchar);
               }
           }
           reader.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
       
       
       try {
           System.out.println("以字符为单位读取文件内容，一次读多个字节：");
           // 一次读多个字符
           char[] tempchars = new char[30];
           int charread = 0;
           reader = new InputStreamReader(new FileInputStream(fileName));
           // 读入多个字符到字符数组中，charread为一次读取字符数
           while ((charread = reader.read(tempchars)) != -1) {
               // 同样屏蔽掉\r不显示
               if ((charread == tempchars.length)
                       && (tempchars[tempchars.length - 1] != '\r')) {
                   System.out.print(tempchars);
               } else {
                   for (int i = 0; i < charread; i++) {
                       if (tempchars[i] == '\r') {
                           continue;
                       } else {
                           System.out.print(tempchars[i]);
                       }
                   }
               }
           }

       } catch (Exception e1) {
           e1.printStackTrace();
       } finally {
           if (reader != null) {
               try {
                   reader.close();
               } catch (IOException e1) {
               }
           }
       }
       
   }

   /**
    * 以行为单位读取文件，常用于读面向行的格式化文件
    */
   @Transactional
   public static boolean convertSourceConfigFile(String fileName, SourceConfigRepository scRepo) {
       File file = new File(fileName);
       boolean status = false;
       String flag = "C";
       
       BufferedReader reader = null;
       try {
           log.info("以行为单位读取文件内容，一次读一整行：");
           InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
           reader = new BufferedReader(isr);
           String tempString = null;
           int line = 1;
           Map<String, String> columnSeq = new HashMap<String, String>();
           Map<String, String> sourceParamPosition = new HashMap<String, String>();
           List<SourceParameter> spList = new ArrayList<SourceParameter>();
           Set<String> sourceParamSet = new HashSet<String>();
           
           
           
           // 一次读入一行，直到读入null为文件结束
           while ((tempString = reader.readLine()) != null) {
        	   
        	   String[] spStrs = tempString.split("--------");
        	   
        	   if(line == 1) {
        		   for(int i=0;i<spStrs.length;i++) { 
            		   columnSeq.put(flag + i, "0");
            	   }
        	   }
        	   
        	   for(int i=0;i<spStrs.length;i++) {
        		   String spStr = spStrs[i].replaceAll(System.lineSeparator(), "").replaceAll("\\p{C}", "").trim();
        		   if(!StringUtils.isEmpty(spStr)) {
	        		   if(sourceParamSet.add(spStr)) {
	        		   
		        		   String[] sps = spStr.split("\\|");
		        		   SourceParameter sp = new SourceParameter();
		            	   sp.setDescription(sps[0]);
		            	   sp.setName(sps[1]);
		            	   sp.setParameter(sps[2]);
		            	   sp.setValue(sps[3]);
		            	   sp.setDisplay(sps[4]);
		            	   sp.setDataType(sps[5]);
		            	   sp.setCallBack(sps.length==7?sps[6]:"");
		            	   
		            	   StringBuffer sbPosition = new StringBuffer();
		            	   String currentColumnMaxValueStr = columnSeq.get(flag + i);
		            	   int currentColumnMaxValue = Integer.parseInt(currentColumnMaxValueStr);
		            	   
		            	   
		            	   sourceParamPosition.put(spStr, String.valueOf(currentColumnMaxValue + 1));
		            	   columnSeq.put(flag + i, String.valueOf(currentColumnMaxValue + 1));
		            	   
		            	   String previousSPPosition = "";
		            			   
		            	   if(i > 0) {
		            		   previousSPPosition = sourceParamPosition.get(spStrs[i-1].replaceAll(System.lineSeparator(), "").replaceAll("\\p{C}", "").trim());
		            	   }
		            	   
		            	   if(!StringUtils.isEmpty(previousSPPosition)) {
		            		   sbPosition.append(previousSPPosition).append("-");
		            	   }
		            	   
		            	   sbPosition.append(sourceParamPosition.get(spStr));
		            	   
		            	   sp.setPosition(sbPosition.toString());
		            	   sourceParamPosition.put(spStr, sp.getPosition());
		            	   
		            	   spList.add(sp);
	        		   
	        		   }
        		   }
        	   }
        	   
               line++;
           }
           reader.close();
           
           String id = file.getName().split("\\.")[0];
           SourceConfig sc = scRepo.findById(id);
           sc.setActive("N");
           scRepo.save(sc);
           log.info("Source Config " + sc + " is unactive now.");
           
           sc.setId(null);
           sc.setActive("Y");
           sc.setSourceParameter(spList);
           scRepo.save(sc);
           
           log.info("Source Config " + sc + " is created and active now.");
           
           status = true;
           
       } catch (IOException e) {
           e.printStackTrace();
           log.error("Failed to convert file [" + fileName + "].");
       } finally {
           if (reader != null) {
               try {
                   reader.close();
               } catch (IOException e1) {
               }
           }
       }
       
       return status;
   }

   /**
    * 随机读取文件内容
    */
   public static void readFileByRandomAccess(String fileName) {
       RandomAccessFile randomFile = null;
       try {
           System.out.println("随机读取一段文件内容：");
           // 打开一个随机访问文件流，按只读方式
           randomFile = new RandomAccessFile(fileName, "r");
           // 文件长度，字节数
           long fileLength = randomFile.length();
           // 读文件的起始位置
           int beginIndex = (fileLength > 4) ? 4 : 0;
           // 将读文件的开始位置移到beginIndex位置。
           randomFile.seek(beginIndex);
           byte[] bytes = new byte[10];
           int byteread = 0;
           // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
           // 将一次读取的字节数赋给byteread
           while ((byteread = randomFile.read(bytes)) != -1) {
               System.out.write(bytes, 0, byteread);
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (randomFile != null) {
               try {
                   randomFile.close();
               } catch (IOException e1) {
               }
           }
       }
   }

   /**
    * 显示输入流中还剩的字节数
    */
   private static void showAvailableBytes(InputStream in) {
       try {
           System.out.println("当前字节输入流中的字节数为:" + in.available());
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   
 //获取readFile 目录下的子目录
     public static void getFileName(String path) {
         //String path = "D:/readFile"; // 路径
         File f = new File(path);
         if (!f.exists()) {
             System.out.println(path + " not exists");
             return;
         }
         File fa[] = f.listFiles();
         for (int i = 0; i < fa.length; i++) {
             File fs = fa[i];
             if (fs.isDirectory()) {
                 System.out.println(fs.getName() + " [目录]");
             } else {
                 System.out.println(fs.getName());
             }
         }
     }
     
}
