package org.explorer.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main";
    }
}
