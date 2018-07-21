package org.explorer.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * It is used for jsp view page
 *
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
public class PageController {

    /**
     * Main page request
     */
    @GetMapping("/")
    public String index() {
        return "common/index";
    }

    /**
     * block infos main page request
     */
    @GetMapping("/{nodeName}/blocks")
    public String blockIndex() {
        return "blockchain/blockInfos";
    }

    /**
     * block info main page request
     */
    @GetMapping("/{nodeName}/block/{query}")
    public String blockDetail(@PathVariable("query") String query) {
        return "blockchain/blockDetail";
    }

    /**
     * transaction infos main page request
     */
    @GetMapping("/{nodeName}/tx/{query}")
    public String txDetail(@PathVariable("query") String query) {
        return "blockchain/txDetail";
    }

    /**
     * Account infos main page request
     */
    @GetMapping("/{nodeName}/accounts")
    public String accountIndex() {
        return "blockchain/accountInfos";
    }
}