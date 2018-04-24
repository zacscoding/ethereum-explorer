package org.explorer.web;

import java.math.BigInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.entity.BlockWrapper;
import org.explorer.service.Web3jService;
import org.explorer.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zacconding
 * @Date 2018-04-24
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
public class BlockController {

    @Autowired
    private Web3jService web3jService;

    // == page request

    @GetMapping("/index")
    public String index() {
        return "blockMain";
    }

    // == data request

    @GetMapping("/blocks/{startBlockNum}/{size}")
    public ResponseEntity<List<BlockWrapper>> getBlocks(@PathVariable("startBlockNum") String startBlockNumVal, @PathVariable("size") String sizeVal) {
        try {
            // check start number
            startBlockNumVal = StringUtil.getStringIfNumberFormat(startBlockNumVal, "");
            int size = StringUtil.parseInt(sizeVal, 10);
            BigInteger startBlockNum = null;
            if (StringUtils.hasText(startBlockNumVal)) {
                startBlockNum = new BigInteger(startBlockNumVal);
            } else {
                startBlockNum = web3jService.findLastBlockNum();
            }

            return ResponseEntity.ok().body(web3jService.findAllBlock(startBlockNum, startBlockNum.subtract(BigInteger.valueOf(size))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}