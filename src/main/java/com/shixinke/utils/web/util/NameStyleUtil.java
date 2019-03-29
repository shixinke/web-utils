package com.shixinke.utils.web.util;


/**
 * naming style util
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午3:06
 */
public class NameStyleUtil {

    private static final String UNDERLINE = "_";

    /***
     * 下划线命名转为驼峰命名
     * @param source 下划线命名的字符串
     */

    public static String underlineToCamel(String source) {
        StringBuffer result = new StringBuffer();
        String[] sourceArr = source.split(UNDERLINE);
        for (String sourceEle : sourceArr) {
            if (result.length() == 0) {
                result.append(sourceEle.toLowerCase());
            } else {
                result.append(sourceEle.substring(0, 1).toUpperCase());
                result.append(sourceEle.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     * @param source 驼峰命名的字符串
     *
     */
    public static String camelToUnderline(String source) {
        StringBuffer stringBuffer = new StringBuffer(source);
        int temp = 0;
        for (int i = 0; i < source.length(); i++) {
            if (Character.isUpperCase(source.charAt(i))) {
                stringBuffer.insert(i + temp, UNDERLINE);
                temp += 1;
            }
        }
        return stringBuffer.toString().toUpperCase();
    }

}
