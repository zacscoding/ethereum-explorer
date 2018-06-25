package org.explorer.configuration;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.explorer.subscribe.BlockEventListener;
import org.explorer.subscribe.BlockNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
public class BlockchainConfiguration {

    @Value("${eth.json.url}")
    private String jsonRpcUrl;
    private String clientVersion;

    @Autowired
    private TaskExecutor eventHandlerExecutor;


    @PostConstruct
    private void initialize() {
        blockObserve(web3j(), blockEventListeners());
    }

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(jsonRpcUrl));
    }

    @Bean
    public List<BlockEventListener> blockEventListeners() {
        return Arrays.asList(
            blockNotificationListener()
        );
    }

    @Bean
    public BlockNotificationListener blockNotificationListener() {
        return new BlockNotificationListener();
    }

    public String getJsonRpcUrl() {
        return jsonRpcUrl;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    private void blockObserve(Web3j web3j, List<BlockEventListener> blockEventListeners) {
        log.info("## Start to subscribe block event. listener : {}", blockEventListeners.size());

        if (!CollectionUtils.isEmpty(blockEventListeners)) {
            web3j.blockObservable(true).subscribe(
                (onNext -> {
                    log.info("## receive new block {}({})", onNext.getBlock().getNumber().toString(10), onNext.getBlock().getHash());
                    blockEventListeners.forEach(listener -> eventHandlerExecutor.execute(() -> listener.onBlock(onNext)));
                }),
                (onError -> {
                    log.error("Failed to subscribe block..", onError.getCause());
                })
            );
        }
    }
}
