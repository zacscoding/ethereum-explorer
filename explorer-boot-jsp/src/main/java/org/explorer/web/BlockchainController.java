package org.explorer.web;

import java.util.List;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.explorer.aop.annotation.NoLogging;
import org.explorer.aop.annotation.PreValidate;
import org.explorer.dto.BlockchainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.PageListRequest;
import org.explorer.service.BlockchainService;
import org.explorer.subscribe.BlockNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
public class BlockchainController {

    @Value("${eth.block.time}")
    private long blockTime;
    @Value("${eth.newblock.subscribe}")
    private boolean subscribeBlocks;

    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private BlockNotificationListener blockNotificationListener;

    // tag eth client info
    @GetMapping(value = "/client-version")
    public ResponseEntity<String> getClientVersion() {
        try {
            return ResponseEntity.ok().body(blockchainService.getClientVersion());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // -- tag eth client info

    // tag block infos
    @GetMapping("/blocks")
    public String blockIndex() {
        return "blockchain/blockInfos";
    }

    @GetMapping(value = "/blocks/subscribe")
    @NoLogging
    @ResponseBody
    public DeferredResult<BlockchainDTO> subscribe() {
        if (!subscribeBlocks) {
            return null;
        }

        final DeferredResult<BlockchainDTO> deferredResult = new DeferredResult<>(blockTime + 3000L);

        blockNotificationListener.subscribe(deferredResult);

        deferredResult.onCompletion(() -> blockNotificationListener.unsubscribe(deferredResult));
        deferredResult.onError((throwable) -> blockNotificationListener.unsubscribe(deferredResult));
        // deferredResult.onTimeout(() -> blockNotificationListener.unsubscribe(deferredResult));
        deferredResult.onTimeout(() -> System.out.println("## timeout!"));

        return deferredResult;
    }

    @GetMapping(value = "/blocks/is-subscribe")
    @ResponseBody
    public ResponseEntity<Boolean> isSubscribe() {
        return ResponseEntity.ok(subscribeBlocks);
    }

    @GetMapping("/blocks/data")
    @ResponseBody
    @PreValidate
    public ResponseEntity<List<BlockWrapper>> getBlocks(PageListRequest pageRequest) {
        try {
            return ResponseEntity.ok().body(blockchainService.findAllBlocks(pageRequest));
        } catch (Exception e) {
            log.error("failed to get blocks", e);
            return ResponseEntity.badRequest().build();
        }
    }
    // -- tag block infos

    // tag tx infos
    // -- tag tx infos

    // tag account infos
    // -- tag account infos
}
