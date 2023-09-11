package me.zhengjie.access;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class RedisJsonUtil {

    public static String dataToJsonString(Object data){
        return JSONObject.toJSONString(data);
    }

    /**
     *功能描述 string 转 对象
     * @author qqg
     * @date 2021/4/22
     * @param jsonString, clazz
     * @return T
     */
    public static <T> T jsonStringToData(String jsonString, Class<T> clazz){
        return JSONObject.parseObject(jsonString,clazz);
    }

    /**
     *功能描述  list 对象转 list jsonString
     * @author qqg
     * @date 2021/4/22
     * @param objects
     * @return java.util.List<java.lang.String>
     */
    public static List<String> listObjToListString(List<Object> objects){
        List<String> returnList=objects.stream().map(p-> JSONObject.toJSONString(p)).collect(Collectors.toList());
        return returnList;
    }
    /**
     *功能描述 list jsonString 转list 对象
     * @author qqg
     * @date 2021/4/22
     * @param strings, clazz
     * @return java.util.List<T>
     */
    public static  <T> List<T> listStringToListObj(List<String> strings, Class<T> clazz){
        List<T> returnList=strings.stream().map(p-> JSONObject.parseObject(p,clazz)).collect(Collectors.toList());
        return returnList;
    }
    /**
     *功能描述 list对象转String
     * @author qqg
     * @date 2021/4/22
     * @param objects
     * @return java.lang.String
     */
    public static String listObjToString(List<Object> objects){
        String jsonString= JSON.toJSONString(objects);
        return jsonString;
    }

    public static <T> String listToString(List<T> list){
        String jsonString= JSON.toJSONString(list);
        return jsonString;
    }

    /**
     *功能描述 list对象转String
     * @author qqg
     * @date 2021/4/22
     * @param
     * @return java.lang.String
     */
    public static <T> List<T> stringToList(String json, Class<T> tClass){
        JSONArray jsonString= JSONArray.parseArray(json);
        return jsonString.toJavaList(tClass);
    }
}
