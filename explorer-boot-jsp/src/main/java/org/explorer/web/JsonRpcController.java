package org.explorer.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zacconding
 * @Date 2018-06-13
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@RequestMapping("/json-rpc/**")
@Slf4j
public class JsonRpcController {

    @GetMapping("/")
    public String index() {
        return "";
    }
}
