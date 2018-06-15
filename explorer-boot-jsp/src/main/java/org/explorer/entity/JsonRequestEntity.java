package org.explorer.entity;

import lombok.Data;

/**
 * @author zacconding
 * @Date 2018-06-16
 * @GitHub : https://github.com/zacscoding
 */
@Data
public class JsonRequestEntity {

    private String url;
    private Object[] params;
    private String method;
    private Integer id;
    private boolean pretty;
}
