package com.shixinke.utils.web.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP util
 * @author shixinke
 */
@Slf4j
public class IpUtil {

    /**
     * Get the IP Address of Client
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
     * Get local server IP address
     * @return
     */
    public static String getLocalIp() {
        String dockerSuffix = ".1";
        try {
            InetAddress candidateAddress = null;
            for (Enumeration interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                for (Enumeration inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress.isSiteLocalAddress() && !inetAddress.getHostAddress().endsWith(dockerSuffix)) {
                            return inetAddress.getHostAddress();
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddress;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress != null) {
                return jdkSuppliedAddress.getHostAddress();
            }
        } catch (Exception e) {
            log.error("cannot get the address:", e);
        }
        return null;
    }

}
