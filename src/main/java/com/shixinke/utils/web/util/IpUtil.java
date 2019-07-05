package com.shixinke.utils.web.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP工具类
 * @author shixinke
 */
@Slf4j
public class IpUtil {

    /**
     * 获取客户端的IP
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        String comma = ",";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        } else if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取本机IP
     * @return
     */
    public static String getLocalIp() {
        String dockerSuffix = ".1";
        try {
            InetAddress candidateAddress = null;
            /**
             *  遍历所有的网络接口
             */
            for (Enumeration interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                /* 在所有的接口下再遍历IP */
                for (Enumeration inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    /* 排除loopback类型地址 */
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress.isSiteLocalAddress() && !inetAddress.getHostAddress().endsWith(dockerSuffix)) {
                            /* 如果是site-local地址，就是它了 */
                            return inetAddress.getHostAddress();
                        } else if (candidateAddress == null) {
                            /* site-local类型的地址未被发现，先记录候选地址 */
                            candidateAddress = inetAddress;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            /**
             *  如果没有发现 non-loopback地址.只能用最次选的方案
             */
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress != null) {
                return jdkSuppliedAddress.getHostAddress();
            }
        } catch (Exception e) {
            log.error("未获取到IP地址");
        }
        return null;
    }

}
