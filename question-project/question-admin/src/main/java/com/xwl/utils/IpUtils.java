package com.xwl.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class IpUtils {
    private static final Map<String, String> IP_MAP = new ConcurrentHashMap<>();

    /**
     * {"address":"CN|安徽省|合肥市|None|None|100|100",
     * "content":{"address_detail":{"province":"安徽省","city":"合肥市","district":"","street":"","street_number":"",
     * "city_code":xxx,"adcode":""},"address":"安徽省合肥市","point":{"x":"xxx.14","y":"xxx.44"}},"status":0}
     */
    public static Dict getIPAndCity() {
        Dict dict = Dict.create();
        try {
            Document document = Jsoup.connect("https://ip.900cha.com/").get();
            // /html/body/div/div/div/div[1]/div[1]/h3
            String ip = document.selectXpath("/html/body/div/div/div/div[1]/div[1]/h3").get(0).text().trim(); // 通过 Xpath 直接获取了网页里面的内容
            dict.set("ip", ip);
            String city = "";
            if (IP_MAP.get(ip) != null) {
                city = IP_MAP.get(ip);
                dict.set("city", city);
                return dict;
            }
            String url = "https://api.map.baidu.com/location/ip?ip=" + ip + "&ak=bmvg8yeOopwOB4aHl5uvx52rgIa3VrPO";
            String res = HttpUtil.createRequest(Method.GET, url).execute().body();
            String json = UnicodeUtil.toString(res);
            JSON jsonObject = JSONUtil.parseObj(json);
            city = jsonObject.getByPath("content.address", String.class);
            dict.set("city", city);
        } catch (Exception e) {
            log.error("获取IP失败", e);
        }
        return dict;
    }
}


//import java.util.regex.Pattern;
//import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.lang3.text.StrTokenizer;
//
///**
// * IpV4 获取真实ip地址
// */
//public class IpUtils {
//
//    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
//    public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
//
//    /**
//     * String类型ip转为Long类型
//     *
//     * @param longIp
//     * @return String
//     */
//    public static String longToIpV4(long longIp) {
//        int octet3 = (int) ((longIp >> 24) % 256);
//        int octet2 = (int) ((longIp >> 16) % 256);
//        int octet1 = (int) ((longIp >> 8) % 256);
//        int octet0 = (int) ((longIp) % 256);
//        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
//    }
//
//    /**
//     * Long类型ip转为String类型
//     *
//     * @param longIp
//     * @return Long
//     */
//    public static long ipV4ToLong(String longIp) {
//        String[] octets = longIp.split("\\.");
//        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
//                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
//    }
//
//    /**
//     * @param ip
//     * @return boolean
//     */
//    public static boolean isIPv4Private(String ip) {
//        long longIp = ipV4ToLong(ip);
//        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
//                || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
//                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
//    }
//
//    public static boolean isIPv4Valid(String ip) {
//        return pattern.matcher(ip).matches();
//    }
//
//    /**
//     * 获取String类型真实ip地址，基于反向代理。
//     *
//     * @param request
//     * @return
//     * 在反向代理中将X-Forward-For替换为remote_addr，即，真实的IP地址。
//     */
//    public static String getIpFromRequest(HttpServletRequest request) {
//        String ip;
//        boolean found = false;
//        if ((ip = request.getHeader("x-forwarded-for")) != null) {
//            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
//            while (tokenizer.hasNext()) {
//                ip = tokenizer.nextToken().trim();
//                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
//                    found = true;
//                    break;
//                }
//            }
//        }
//        if (!found) {
//            ip = request.getRemoteAddr();// 获得ip地址
//        }
//        return ip;
//    }
//}