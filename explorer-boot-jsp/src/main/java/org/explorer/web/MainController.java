package org.explorer.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockChainDTO;
import org.explorer.observer.BlockConsumer;
import org.explorer.service.Web3jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */

@Slf4j
@Controller
public class MainController {

    @Autowired
    private Web3jService web3jService;
    @Autowired
    private BlockConsumer blockConsumer;

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }

    @GetMapping(value = "/last-block")
    public ResponseEntity<BlockChainDTO> getLatestBlock() {
        try {
            return ResponseEntity.ok().body(web3jService.findLastBlock());
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/subscribe")
    @ResponseBody
    public DeferredResult<BlockChainDTO> subscribe() {
        final DeferredResult<BlockChainDTO> deferredResult = new DeferredResult<>();

        blockConsumer.subscribe(deferredResult);

        deferredResult.onCompletion(() -> blockConsumer.unsubscribe(deferredResult));
        deferredResult.onError((throwable) -> blockConsumer.unsubscribe(deferredResult));
        deferredResult.onTimeout(() -> blockConsumer.unsubscribe(deferredResult));

        return deferredResult;
    }
}
