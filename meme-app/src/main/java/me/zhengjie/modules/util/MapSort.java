package me.zhengjie.modules.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapSort {
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        Map<String,String> sortMap =new TreeMap<String,String>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return o1.toString().compareTo(o2.toString());
            }
        });
        for (String key: map.keySet()){
            sortMap.put(key,map.get(key));
        }


        return sortMap;

    }
}
