package org.explorer.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.explorer.entity.JsonRequestEntity;
import org.explorer.entity.JsonRpcSpec;
import org.explorer.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class ParityJsonRpcService implements JsonRpcService {

    private List<JsonRpcSpec> jsonRpcSpec;

    @Autowired
    private JsonRpcHttpService jsonRpcHttpService;

    @PostConstruct
    private void setUp() {
        initializeJsonSpec();
    }

    @Override
    public List<JsonRpcSpec> getJsonRpcSpecs() {
        return jsonRpcSpec;
    }

    @Override
    public List<String> getJsonRpcModules() {
        return jsonRpcSpec.stream().map(spec -> spec.getModule()).collect(Collectors.toList());
    }

    @Override
    public List<String> getMethods(String module) {
        if (!StringUtils.hasText(module)) {
            return Collections.emptyList();
        }

        for (JsonRpcSpec spec : jsonRpcSpec) {
            if (module.equals(spec.getModule())) {
                return spec.getMethods();
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String jsonRequest(JsonRequestEntity entity) {
        String error = jsonRpcHttpService.getInvalidJsonRequest(entity);
        if (error != null) {
            return error;
        }

        String result = jsonRpcHttpService.request(entity.getUrl(), entity.getMethod(), entity.getId(), entity.getParams());
        if (entity.isPretty()) {
            try {
                result = GsonUtil.jsonStringToPretty(result);
            } catch (Exception e) {
                // ignore & return origin value
            }
        }
        return result;
    }

    private void initializeJsonSpec() {
        try {
            Type listType = new TypeToken<ArrayList<JsonRpcSpec>>() {
            }.getType();
            Gson gson = GsonUtil.GsonFactory.createDefaultGson();
            jsonRpcSpec = gson.fromJson(new FileReader(new ClassPathResource("parity-json-spec.json").getFile()), listType);
        } catch (Exception e) {
            log.error("failed to initialize parity json rpc spec", e);
            System.exit(-1);
        }
    }
}
