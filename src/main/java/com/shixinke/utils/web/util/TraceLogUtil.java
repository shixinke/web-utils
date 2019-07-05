package com.shixinke.utils.web.util;



import com.shixinke.utils.web.common.TraceLogId;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 追踪日志工具类
 * @author shixinke
 */
@Slf4j
public class TraceLogUtil {

    /**
     * 自增ID
     */
    private static AtomicLong lastId  = new AtomicLong();
    /**
     * 启动加载的时间戳
     */
    private static final long NOW = System.currentTimeMillis();

    /**
     * ip段分隔符
     */
    private static final String IP_SEPARATOR_PATTERN = "\\.";
    /**
     * traceId分割符
     */
    private static final String TRACE_ID_SEPARATOR = "-";

    /**
     * 获取唯一的请求追踪ID
     * @return String
     */
    public static String getTraceId() {
        String ip = IpUtil.getLocalIp();
        if (ip == null) {
            ip = "127.0.0.1";
        }
        return ipToHex(ip) + TRACE_ID_SEPARATOR + Long.toString(NOW, Character.MAX_RADIX) + TRACE_ID_SEPARATOR + lastId.incrementAndGet();
    }

    /**
     * 将IP转换成16进制数据
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
     * 将16进制转化为IP
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
     * 解析traceId
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
                log.error("解析追踪日志出错:", ex);
            }
        }

        return traceLogId;
    }

}
