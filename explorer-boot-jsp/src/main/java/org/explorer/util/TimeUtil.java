package org.explorer.util;

import java.util.concurrent.TimeUnit;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
public class TimeUtil {

    public static String displayElapsedString(long elapsed) {
        return new StringBuilder().append(elapsed).append(" [ms] | ")
                                  .append(TimeUnit.SECONDS.convert(elapsed, TimeUnit.MILLISECONDS)).append(" [sec] || ")
                                  .append(TimeUnit.MINUTES.convert(elapsed, TimeUnit.MILLISECONDS)).append(" [min]").toString();
    }

}
