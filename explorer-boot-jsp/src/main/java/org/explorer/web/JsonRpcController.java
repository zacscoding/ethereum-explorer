package org.explorer.web;

import com.google.gson.Gson;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.entity.EthNode;
import org.explorer.entity.JsonRequestEntity;
import org.explorer.entity.enums.ClientType;
import org.explorer.service.JsonRpcService;
import org.explorer.service.ParityJsonRpcService;
import org.explorer.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Delegate json rpc
 *
 * @author zacconding
 * @Date 2018-06-13
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@RequestMapping("/json-rpc/**")
@Slf4j
public class JsonRpcController {

    @Autowired
    private ParityJsonRpcService parityJsonRpcService;

    @GetMapping("/{typeName}")
    public String index(@PathVariable("typeName") String typeName, Model model) {
        ClientType type = getTypeWithValid(typeName);

        return "jsonrpc/" + type.type() + "-json-rpc";
    }

    @GetMapping("/{typeName}/specs")
    @ResponseBody
    public ResponseEntity<String> getSpecs(@PathVariable("typeName") String typeName) {
        JsonRpcService rpcService = getJsonRpcServiceBeanByTypeName(typeName);
        Gson gson = GsonUtil.GsonFactory.createDefaultGson();

        return ResponseEntity.ok(gson.toJson(rpcService.getJsonRpcSpecs()));
    }

    @PostMapping("/{typeName}/request")
    @ResponseBody
    public ResponseEntity<String> jsonRequest(@PathVariable("typeName") String typeName, @RequestBody JsonRequestEntity jsonRequestEntity) {
        log.info("## Request json rpc \n" + GsonUtil.toString(jsonRequestEntity));
        String result = null;
        try {
            result = getJsonRpcServiceBeanByTypeName(typeName).jsonRequest(jsonRequestEntity);
        } catch (Exception e) {
            log.error("failed to request", e);
            result = e.getMessage();
        }

        return ResponseEntity.ok(result);
    }

    private JsonRpcService getJsonRpcServiceBeanByTypeName(String typeName) {
        ClientType type = getTypeWithValid(typeName);

        switch (type) {
            case PARITY:
                return parityJsonRpcService;
            // TODO :: add geth and so on
        }

        throw new RuntimeException("Unsupported rpc type : " + typeName);
    }

    private ClientType getTypeWithValid(String typeName) {
        ClientType type = ClientType.getClientType(typeName);

        if (type == null) {
            throw new RuntimeException("Unsupported rpc type : " + typeName);
        }

        switch (type) {
            // TODO :: add geth and so on
            case PARITY:
                return type;
        }

        throw new RuntimeException("Unsupported rpc type : " + typeName);
    }
}