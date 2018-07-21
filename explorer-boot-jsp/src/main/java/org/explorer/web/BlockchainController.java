package org.explorer.web;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.aop.annotation.NoLogging;
import org.explorer.aop.annotation.PreValidate;
import org.explorer.dto.BlockchainDTO;
import org.explorer.dto.PageListRequest;
import org.explorer.entity.AccountWrapper;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.EthNode;
import org.explorer.entity.TransactionWrapper;
import org.explorer.service.BlockchainService;
import org.explorer.subscribe.BlockNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private BlockNotificationListener blockNotificationListener;
    @Autowired
    private List<EthNode> ethNodes;

    // tag eth client info

    /**
     * Get client version
     */
    @GetMapping(value = "/{nodeName}/client-version")
    public ResponseEntity<String> getClientVersion(@PathVariable("nodeName") String nodeName) {
        try {
            EthNode ethNode = getEthNode(nodeName);
            return ResponseEntity.ok().body(blockchainService.getClientVersion(ethNode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get EthNodes
     */
    @GetMapping(value = "/nodes")
    public ResponseEntity<List<EthNode>> getNodes() {
        try {
            return ResponseEntity.ok().body(ethNodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // -- tag eth client info

    // tag block infos

    /**
     * Subscribe new block event
     */
    @GetMapping(value = "/{nodeName}/blocks/subscribe")
    @NoLogging
    public DeferredResult<BlockchainDTO> subscribe(@PathVariable("nodeName") String nodeName) {
        EthNode ethNode = getEthNode(nodeName);
        if (!blockchainService.isBlockSubscribe(ethNode)) {
            return null;
        }

        final DeferredResult<BlockchainDTO> deferredResult = new DeferredResult<>();

        blockNotificationListener.subscribe(ethNode, deferredResult);

        deferredResult.onCompletion(() -> blockNotificationListener.unsubscribe(ethNode, deferredResult));
        deferredResult.onError((throwable) -> blockNotificationListener.unsubscribe(ethNode, deferredResult));
        deferredResult.onTimeout(() -> {
            blockNotificationListener.unsubscribe(ethNode, deferredResult);
            deferredResult.setResult(null);
        });

        return deferredResult;
    }

    /**
     * get new block subscribe or not
     */
    @GetMapping(value = "/{nodeName}/blocks/is-subscribe")
    public ResponseEntity<Boolean> isSubscribe(@PathVariable("nodeName") String nodeName) {
        EthNode ethNode = getEthNode(nodeName);
        return ResponseEntity.ok(blockchainService.isBlockSubscribe(ethNode));
    }

    /**
     * get block time
     */
    @GetMapping(value = "/{nodeName}/blocks/time")
    public ResponseEntity<Long> getBlockTime(@PathVariable("nodeName") String nodeName) {
        EthNode ethNode = getEthNode(nodeName);
        return ResponseEntity.ok(blockchainService.findBlockTime(ethNode));
    }


    /**
     * get block list
     */
    @GetMapping("/{nodeName}/blocks/data")
    @PreValidate
    public ResponseEntity<List<BlockWrapper>> getBlocks(@PathVariable("nodeName") String nodeName, PageListRequest pageRequest) {
        try {
            EthNode ethNode = getEthNode(nodeName);
            return ResponseEntity.ok().body(blockchainService.findAllBlocks(ethNode, pageRequest));
        } catch (Exception e) {
            log.error("failed to get blocks", e);
            return ResponseEntity.badRequest().build();
        }
    }
    // -- tag block infos

    /**
     * search block
     */
    @PostMapping("/{nodeName}/block/{query}")
    public ResponseEntity<BlockchainDTO> getBlock(@PathVariable("nodeName") String nodeName, @PathVariable("query") String query) {
        try {
            EthNode ethNode = getEthNode(nodeName);
            return ResponseEntity.ok().body(blockchainService.findOneBlock(ethNode, query));
        } catch (Exception e) {
            log.error("failed to get block : " + query, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // tag tx infos

    /**
     * search tx
     */
    @PostMapping("/{nodeName}/tx/{query}")
    public ResponseEntity<TransactionWrapper> getTx(@PathVariable("nodeName") String nodeName, @PathVariable("query") String query) {
        try {
            EthNode ethNode = getEthNode(nodeName);
            return ResponseEntity.ok().body(blockchainService.findOneTxByHash(ethNode, query));
        } catch (Exception e) {
            log.error("failed to get block : " + query, e);
            return ResponseEntity.badRequest().build();
        }
    }
    // -- tag tx infos

    // tag account infos
    @GetMapping("/{nodeName}/accounts/data")
    @PreValidate
    public ResponseEntity<List<AccountWrapper>> getAccounts(@PathVariable("nodeName") String nodeName, PageListRequest pageRequest) {
        try {
            EthNode ethNode = getEthNode(nodeName);
            return ResponseEntity.ok().body(blockchainService.findAllAccounts(ethNode, pageRequest));
        } catch (Exception e) {
            log.error("failed to get blocks", e);
            return ResponseEntity.badRequest().build();
        }
    }
    // -- tag account infos

    private EthNode getEthNode(String nodeName) {
        for (EthNode ethNode : ethNodes) {
            if (ethNode.getNodeName().equalsIgnoreCase(nodeName)) {
                return ethNode;
            }
        }

        throw new RuntimeException("Not exist node : " + nodeName);
    }
}
