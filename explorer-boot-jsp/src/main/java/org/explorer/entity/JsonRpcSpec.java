package org.explorer.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
public class JsonRpcSpec {

    // WEB3 , NET, ETH ..
    private String module;
    private String url;
    private List<String> methods;
}