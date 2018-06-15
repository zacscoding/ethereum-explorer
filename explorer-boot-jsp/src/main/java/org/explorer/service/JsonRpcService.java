package org.explorer.service;

import java.util.List;
import org.explorer.entity.JsonRequestEntity;
import org.explorer.entity.JsonRpcSpec;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
public interface JsonRpcService {

    List<JsonRpcSpec> getJsonRpcSpecs();

    List<String> getJsonRpcModules();

    List<String> getMethods(String module);

    String jsonRequest(JsonRequestEntity entity);
}