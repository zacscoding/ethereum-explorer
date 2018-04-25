package org.explorer.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;
import org.explorer.parser.BlockChainParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * Produce BlockChainDTO (received from web3j observer)
 *
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class BlockProducer {

    private BlockingQueue<BlockChainDTO> blockQueue;
    private Web3j web3j;

    @Autowired
    public BlockProducer(BlockingQueue<BlockChainDTO> blockQueue, Web3j web3j) {
        this.blockQueue = blockQueue;
        this.web3j = web3j;
        initialize();
    }

    private void initialize() {
        log.info("## Initialize block producer..");
        log.info("#Start subscribe..");

        web3j.blockObservable(true).subscribe(
            (onNext -> {
                Block block = onNext.getBlock();
                // log.info("## Produce block.. : " + block.getNumber());
                blockQueue.add(BlockChainParser.parseBlockDto(block));
            }),
            (onError -> {
                log.error("Failed to subscribe block..", onError.getCause());
            }));

        log.info("#end subscribe..");
    }
}