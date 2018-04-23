package org.explorer.util;

import com.google.gson.GsonBuilder;

/**
 * @author zacconding
 * @Date 2018-04-18
 * @GitHub : https://github.com/zacscoding
 */
public class GsonUtil {

    public static String toString(Object inst) {
        if (inst == null) {
            return "null";
        }

        return new GsonBuilder().serializeNulls().create().toJson(inst);
    }

    public static String toStringPretty(Object inst) {
        if (inst == null) {
            return "{}";
        }

        return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(inst);
    }
}
