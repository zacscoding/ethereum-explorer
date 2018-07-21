package org.explorer.subscribe;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.explorer.dto.BlockchainDTO;
import org.explorer.entity.EthNode;
import org.explorer.parser.BlockchainParser;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;
import org.web3j.protocol.core.methods.response.EthBlock;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public class BlockNotificationListener extends BlockEventListenrAdapter {

    private Map<EthNode, List<DeferredResult<BlockchainDTO>>> subscribersMap;

    @PostConstruct
    private void setUp() {
        subscribersMap = new ConcurrentHashMap<>();
    }

    @Override
    public void onBlock(EthNode ethNode, EthBlock ethBlock) {
        BlockchainDTO dto = BlockchainParser.parseBlockDto(ethBlock.getBlock());
        List<DeferredResult<BlockchainDTO>> subscribers = subscribersMap.get(ethNode);
        if (!CollectionUtils.isEmpty(subscribers)) {
            subscribers.forEach(subscriber -> {
                if (subscriber != null && !subscriber.isSetOrExpired()) {
                    subscriber.setResult(dto);
                }
            });
        }
    }

    public void subscribe(EthNode ethNode, DeferredResult<BlockchainDTO> result) {
        List<DeferredResult<BlockchainDTO>> subscribers = subscribersMap.get(ethNode);

        if (subscribers == null) {
            subscribers = new LinkedList<>();
            subscribersMap.put(ethNode, subscribers);
        }

        subscribers.add(result);
    }

    public void unsubscribe(EthNode ethNode, DeferredResult<BlockchainDTO> result) {
        List<DeferredResult<BlockchainDTO>> subscribers = subscribersMap.get(ethNode);
        if (subscribers != null) {
            subscribers.remove(result);
        }
    }
}