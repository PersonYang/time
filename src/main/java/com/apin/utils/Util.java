package com.apin.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/12/16.
 */
public class Util {

    private final static Logger logger= LoggerFactory.getLogger(Util.class);

    public static String getTrace(Throwable t){
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        StringBuffer sb=stringWriter.getBuffer();
        return sb.toString();
    }

    public static boolean isBlank(JSONObject paramsJson,String...params){

        for(String param:params){
            if(!paramsJson.has(param)){
                logger.error("The key of param:{} doesnot exist",param);
                return true;
            }
            Object object=paramsJson.get(param);
            if(null==object){
                logger.error("The value of param:{} doesnot exist",param);
                return true;
            }
            if(object instanceof String){
                if(StringUtils.isBlank((String) object)){
                    logger.error("The value of param:{} is ''",param);
                    return true;
                }
            }
        }

        return false;
    }

    public static HashMap<String,String> toHashMap(JSONObject json){
        HashMap<String,String> map=new HashMap<>();
        Iterator it=json.keys();
        while(it.hasNext()){
            String key=String.valueOf(it.next());
            map.put(key,json.getString(key));
        }
        return map;
    }
}
