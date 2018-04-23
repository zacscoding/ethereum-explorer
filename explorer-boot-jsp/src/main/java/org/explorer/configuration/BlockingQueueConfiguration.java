package org.explorer.configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class BlockingQueueConfiguration {

    @Bean
    public BlockingQueue<BlockChainDTO> blockQueue() {
        return new LinkedBlockingQueue();
    }
}
