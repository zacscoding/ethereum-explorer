package org.explorer.util;

import java.util.Collection;
import org.springframework.util.CollectionUtils;

/**
 * @author zacconding
 * @Date 2018-04-25
 * @GitHub : https://github.com/zacscoding
 */
public class CollectionUtil extends CollectionUtils {

    public static int safeGetSize(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }
}
