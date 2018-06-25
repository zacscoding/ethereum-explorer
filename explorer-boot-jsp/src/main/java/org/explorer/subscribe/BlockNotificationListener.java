package org.explorer.subscribe;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.explorer.dto.BlockchainDTO;
import org.explorer.parser.BlockchainParser;
import org.springframework.web.context.request.async.DeferredResult;
import org.web3j.protocol.core.methods.response.EthBlock;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public class BlockNotificationListener extends BlockEventListenrAdapter {

    private List<DeferredResult<BlockchainDTO>> subscribers;

    @PostConstruct
    private void setUp() {
        subscribers = new LinkedList<>();
    }

    @Override
    public void onBlock(EthBlock ethBlock) {
        BlockchainDTO dto = BlockchainParser.parseBlockDto(ethBlock.getBlock());
        subscribers.forEach(subscriber -> {
            if (subscriber != null && !subscriber.isSetOrExpired()) {
                subscriber.setResult(dto);
            }
        });
    }

    public void subscribe(DeferredResult<BlockchainDTO> result) {
        subscribers.add(result);
    }

    public void unsubscribe(DeferredResult<BlockchainDTO> result) {
        subscribers.remove(result);
    }
}
