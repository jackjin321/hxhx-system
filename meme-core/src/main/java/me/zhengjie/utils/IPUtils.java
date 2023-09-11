package me.zhengjie.utils;


import cn.hutool.core.util.ObjectUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * IP地址
 *
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2019年01月14日
 */
public class IPUtils {
    private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = Strings.EMPTY;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (ObjectUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ObjectUtil.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ObjectUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ObjectUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ObjectUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            //使用代理，则获取第一个IP地址
            if (ObjectUtil.isNotEmpty(ip) && ip.contains(",")) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }
        return ip;
    }

    public static class CityInfo implements Serializable {
        private String country;
        private String province;
        private String city;
        private String provider;

        public CityInfo() {
        }

        public CityInfo(String cityStr) {
            String[] info = cityStr.split("\\|");
            if (info.length != 5) {
                logger.error("cityStr unsupported type format");
            } else {
                this.country = this.getStr(info[0]);
                this.province = this.getStr(info[2]);
                this.city = this.getStr(info[3]);
                this.provider = this.getStr(info[4]);
            }

        }

        private String getStr(String cityInfo) {
            return cityInfo.equals("0") ? "" : cityInfo;
        }

        @Override
        public String toString() {
            return String.format("%s \n---||\n\t country:[%s],\n\tprovince:[%s],\n\tcity:[%s],\n\tprovider:[%s]\n---||\n", this.getClass().getName(), this.country, this.province, this.city, this.provider);
        }

        public String getCountry() {
            return this.country;
        }

        public String getProvince() {
            return this.province;
        }

        public String getCity() {
            return this.city;
        }

        public String getProvider() {
            return this.provider;
        }

        public CityInfo setCountry(final String country) {
            this.country = country;
            return this;
        }

        public CityInfo setProvince(final String province) {
            this.province = province;
            return this;
        }

        public CityInfo setCity(final String city) {
            this.city = city;
            return this;
        }

        public CityInfo setProvider(final String provider) {
            this.provider = provider;
            return this;
        }
    }
}
