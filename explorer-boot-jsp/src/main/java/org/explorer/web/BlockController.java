package org.explorer.web;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.service.Web3jService;
import org.explorer.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/blocks/index")
    public String index() {
        return "blockMain";
    }

    // == data request

    /**
     * @return blocks          : block list
     * startBlockNum   : start block number
     */
    @GetMapping("/blocks/{startBlockNum}/{size}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBlocks(@PathVariable("startBlockNum") String startBlockNumVal,
        @PathVariable("size") String sizeVal) {

        log.info("#request blocks");
        try {
            // check start number
            startBlockNumVal = StringUtil.getStringIfNumberFormat(startBlockNumVal, "");
            int size = StringUtil.parseInt(sizeVal, 10);
            BigInteger startBlockNum = null;

            try {
                startBlockNum = new BigInteger(startBlockNumVal);
            } catch(Exception e) {
                startBlockNum = web3jService.findLastBlockNum();
            }

            Map<String, Object> ret = new HashMap<>();
            ret.put("blocks", web3jService.findAllBlock(startBlockNum, startBlockNum.subtract(BigInteger.valueOf(size))));
            ret.put("startBlockNum", startBlockNum);

            return ResponseEntity.ok().body(ret);
        } catch (Exception e) {
            log.error("## Failed to search blocks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/block/{keyword}")
    @ResponseBody
    public ResponseEntity<BlockChainDTO> getBlock(@PathVariable("keyword") String keyword) {
        log.info("#request block");
        try {
            return ResponseEntity.ok().body(web3jService.findBlockByKeyword(keyword));
        } catch(Exception e) {
            log.error("## Failed to search block", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}