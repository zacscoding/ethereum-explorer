package org.explorer.observer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Consume BlockChainDTO (produced by BlockProducer)
 *
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class BlockConsumer {

    private Thread thread;
    private boolean alive = true;
    private BlockingQueue<BlockChainDTO> blockQueue;
    private List<DeferredResult<BlockChainDTO>> subscribers;

    @Autowired
    public BlockConsumer(BlockingQueue<BlockChainDTO> blockQueue) {
        this.blockQueue = blockQueue;
        subscribers = new LinkedList<>();
        initialize();
    }

    public void subscribe(DeferredResult<BlockChainDTO> result) {
        subscribers.add(result);
    }

    public void unsubscribe(DeferredResult<BlockChainDTO> result) {
        subscribers.remove(result);
    }

    private void initialize() {
        log.info("## Initialize block consumer..");

        thread = new Thread(() -> {
            while (alive) {
                try {
                    BlockChainDTO dto = blockQueue.take();
                    log.info("## Take block.. number : {}, #tx : {} ", dto.getBlock().getNumber(), dto);
                    log.info("## Subscribers : " + subscribers.size());
                    subscribers.forEach(subscriber -> subscriber.setResult(dto));
                } catch (Exception e) {
                    alive = false;
                }
            }
        });

        thread.setDaemon(true);
        thread.setName("[Block Consumer Thread]");
        thread.start();
    }
}