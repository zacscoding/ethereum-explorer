package org.explorer.service;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.explorer.aop.annotation.Elapsed;
import org.explorer.entity.JsonRequestEntity;
import org.explorer.util.GsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-06-16
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class JsonRpcHttpService {

    @Elapsed
    public String request(String uri, String method, Integer id, Object... paramsArray) {
        CloseableHttpClient client = null;

        try {
            client = HttpClients.createDefault();

            String requestBody = generateJsonRequestString(method, id, paramsArray);
            CloseableHttpResponse response = client.execute(generateHttpPost(uri, requestBody));

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("failed to close http client");
                }
            }
        }
    }

    public Object requestAndGetResult(String uri, String method, Integer id, Object... paramsArray) {
        return extractResult(request(uri, method, id, paramsArray));
    }

    public Object extractResult(String resultJson) {
        if (!StringUtils.hasText(resultJson)) {
            return null;
        }

        Map<String, Object> resultMap = GsonUtil.GsonFactory.createDefaultGson().fromJson(resultJson, Map.class);

        if (resultMap.containsKey("result")) {
            return resultMap.get("result");
        }

        if (resultMap.containsKey("error")) {
            log.error("failed to response : " + resultJson);
            throw new RuntimeException(resultJson);
        }

        throw new RuntimeException(resultJson);
    }

    public String getInvalidJsonRequest(JsonRequestEntity entity) {
        String emptyValue = null;

        if (entity == null) {
            emptyValue = "JsonRequest";
        } else if (!StringUtils.hasText(entity.getUrl())) {
            emptyValue = "Request URL";
        } else if (!StringUtils.hasText(entity.getMethod())) {
            emptyValue = "Request METHOD";
        }

        return emptyValue == null ? null : emptyValue + " must be not null";
    }

    private String generateJsonRequestString(String method, Integer id, Object... paramsArray) {
        List<Object> params = null;

        if (id == null) {
            id = Integer.valueOf(1);
        }

        if (paramsArray == null) {
            params = Collections.emptyList();
        } else {
            params = Arrays.asList(paramsArray);
        }

        JSONRPC2Request request = new JSONRPC2Request(method, id);
        request.setPositionalParams(params);

        return request.toJSONString();
    }

    private HttpPost generateHttpPost(String uri, String body) {
        HttpPost httpPost = new HttpPost(uri);

        httpPost.setEntity(new StringEntity(body, "UTF-8"));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

        return httpPost;
    }
}
