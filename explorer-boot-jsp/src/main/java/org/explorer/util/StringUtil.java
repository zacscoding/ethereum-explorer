package org.explorer.util;

/**
 * @author zacconding
 * @Date 2018-04-17
 * @GitHub : https://github.com/zacscoding
 */
public class StringUtil {

    public int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getStringIfNumberFormat(String value, String defaultValue) {
        try {
            Integer.parseInt(value);
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
