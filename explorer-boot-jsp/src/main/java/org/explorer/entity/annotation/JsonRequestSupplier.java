package org.explorer.entity.annotation;

import java.io.IOException;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@FunctionalInterface
public interface JsonRequestSupplier<T> {

    T get() throws IOException;
}
