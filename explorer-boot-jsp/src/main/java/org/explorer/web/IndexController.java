package org.explorer.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zacconding
 * @Date 2018-06-13
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@Slf4j
public class IndexController {

    @GetMapping("/page-test/{name}")
    public String test(@PathVariable("name") String viewName) {
        return viewName;
    }
}
