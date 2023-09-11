package me.zhengjie.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import me.zhengjie.exception.BadConfigurationException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

public final class StringUtil {
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    public StringUtil() {
    }

    public static String byteToString(byte[] byteArray) {
        return new String(byteArray, UTF_8);
    }

    public static byte[] toByteArray(String data) {
        return data.getBytes(UTF_8);
    }

    public static byte[] encode(String data) {
        return encode(toByteArray(data));
    }

    public static String encodeToStr(String data) {
        return byteToString(encode(toByteArray(data)));
    }

    public static String encodeToStrNoWarp(String data) {
        return byteToString(encode(toByteArray(data))).replaceAll("[\\s*\t\n\r]", "");
    }

    public static byte[] encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static byte[] encodeNoWarp(byte[] data) {
        byte[] noWarpByte = new byte[data.length];
        int count = 0;
        byte[] var3 = data;
        int var4 = data.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if (b != 9 && b != 10 && b != 13) {
                noWarpByte[noWarpByte.length] = b;
            } else {
                ++count;
            }
        }

        return Base64.getEncoder().encode(Arrays.copyOf(noWarpByte, noWarpByte.length - count));
    }

    public static String encodeToStr(byte[] data) {
        return byteToString(Base64.getEncoder().encode(data));
    }

    public static String encodeToStrNoWarp(byte[] data) {
        return encodeToStr(data).replaceAll("[\\s*\t\n\r]", "");
    }

    public static byte[] decode(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

    public static byte[] decode(String data) {
        return decode(toByteArray(data));
    }

    public static String urlBuilder(String url, Map<?, ?> map) {
        if (map.isEmpty()) {
            return url;
        } else {
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            Iterator var3 = map.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<?, ?> entry = (Entry)var3.next();
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(urlEncode(String.valueOf(entry.getValue())));
                builder.append("&");
            }

            if (!map.isEmpty()) {
                builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
        }
    }

    public static String get(String str) {
        return (String)getDefVal(str, "");
    }

    public static <T> T getDefVal(T t, T def) {
        return t == null ? def : t;
    }

    public static boolean isNotEmpty(Object... objs) {
        return !isEmpty(objs);
    }

    public static String urlEncode(String data) {
        return urlEncode(data, "UTF-8");
    }

    public static String urlEncode(String data, String charset) {
        try {
            return URLEncoder.encode(data, charset);
        } catch (Exception var3) {
            throw new BadConfigurationException(var3);
        }
    }

    public static boolean isEmpty(Object... objs) {
        for(int index = 0; index < objs.length; ++index) {
            Object obj = objs[index];
            if (isEmpty(obj)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String)obj).isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static String toString(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static String format(String format, Object... obj) {
        for(int index = 0; index < obj.length; ++index) {
            if (obj[index] == null) {
                obj[index] = "";
            }
        }

        return String.format(format, obj);
    }

    public static String signMap(Map map) {
        List<String> keys = new ArrayList(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        Iterator var3 = keys.iterator();

        while(true) {
            while(var3.hasNext()) {
                String key = (String)var3.next();
                Object v = map.get(key);
                if (!(v instanceof Map) && !(v instanceof Collection)) {
                    sb.append(v == null ? "" : v.toString());
                } else {
                    sb.append(JSON.toJSONString(v, new SerializerFeature[]{SerializerFeature.MapSortField}));
                }
            }

            return sb.toString();
        }
    }

    public static String moneyFormat(String money) {
        int index = money.indexOf(".");
        if (index == -1) {
            return money + ".00";
        } else {
            int difference = money.length() - index;
            if (difference == 1) {
                return money + "00";
            } else if (difference == 2) {
                return money + "0";
            } else if (difference == 3) {
                return money;
            } else {
                String suffix = money.substring(index + 1);
                Integer suffixIntVal = new Integer(suffix);
                if (suffixIntVal > 0) {
                    suffixIntVal = (new BigDecimal(money.substring(index + 1, index + 3))).add(new BigDecimal(1)).intValue();
                    String prefix = money.substring(0, index);
                    if (suffixIntVal < 10) {
                        return prefix + ".0" + suffixIntVal;
                    } else {
                        return suffixIntVal >= 10 && suffixIntVal < 100 ? prefix + "." + suffixIntVal : (new BigDecimal(prefix)).add(new BigDecimal(1)).toString() + ".00";
                    }
                } else {
                    return money.substring(0, index + 3);
                }
            }
        }
    }

    public static String genReqid() {
        String format = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String numbers = RandomUtil.randomNumbers(5);
        String reqid = format + numbers;
        return reqid;
    }

    public static String formatUrlMapByValue(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Entry<String, String>> infoIds = new ArrayList<Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Entry<String, String>>() {
                @Override
                public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    Object val = item.getValue();
                    buf.append(val);
//                    buf.append(key + "=" + val);
//                    buf.append("&");
                }
            }
            buff = buf.toString();
//            if (buff.isEmpty() == false) {
//                buff = buff.substring(0, buff.length() - 1);
//            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

}
