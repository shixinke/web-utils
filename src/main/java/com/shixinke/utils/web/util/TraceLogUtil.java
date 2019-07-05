package com.shixinke.utils.web.util;



import com.shixinke.utils.web.common.TraceLogId;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.atomic.AtomicLong;

/**
 * trace log util
 * @author shixinke
 */
@Slf4j
public class TraceLogUtil {

    /**
     * atomic number
     */
    private static AtomicLong lastId  = new AtomicLong();

    /**
     * the separator of ip address
     */
    private static final String IP_SEPARATOR_PATTERN = "\\.";
    /**
     * the separator of trace id
     */
    private static final String TRACE_ID_SEPARATOR = "-";

    /**
     * get trace id
     * @return String
     */
    public static String getTraceId() {
        String ip = IpUtil.getLocalIp();
        if (ip == null) {
            ip = "127.0.0.1";
        }
        return ipToHex(ip) + TRACE_ID_SEPARATOR + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + TRACE_ID_SEPARATOR + lastId.incrementAndGet();
    }

    /**
     * transfer IP address to hex
     * @param ip
     * @return String
     */
    private static String ipToHex(String ip) {
        StringBuilder sb = new StringBuilder();
        for (String seg : ip.split(IP_SEPARATOR_PATTERN)) {
            String h = Integer.toHexString(Integer.parseInt(seg));
            if (h.length() == 1) {
                sb.append("0");
            }
            sb.append(h);
        }
        return sb.toString();
    }

    /**
     * transfer hex to IP address
     * @param hexString
     * @return
     */
    private static String hexToIp(String hexString) {
        int size = 2;
        int total = hexString.length();
        StringBuilder ipBuilder = new StringBuilder();
        for (int i = 0; i < total; i += size) {
            String part = hexString.substring(i, i+size);
            ipBuilder.append(Integer.valueOf(part, 16));
            if ( i < 6) {
                ipBuilder.append(".");
            }
        }
        return ipBuilder.toString();
    }


    /**
     * parse the trace id
     * @param traceId
     * @return
     */
    public static TraceLogId parseTraceId(String traceId) {
        TraceLogId traceLogId = new TraceLogId();
        String[] traceArr = traceId.split(TRACE_ID_SEPARATOR);
        int normalLength = 3;
        if (traceArr.length == normalLength) {
            try {
                traceLogId.setLastId(Long.valueOf(traceArr[2]));
                traceLogId.setIp(hexToIp(traceArr[0]));
                traceLogId.setCreateTime(Long.valueOf(traceArr[1], Character.MAX_RADIX));
            } catch (Exception ex) {
                log.error("parse trace id error:", ex);
            }
        }

        return traceLogId;
    }

}
