package org.explorer.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.explorer.configuration.properties.EthProperties;
import org.explorer.entity.EthNode;
import org.explorer.subscribe.BlockEventListener;
import org.explorer.subscribe.BlockNotificationListener;
import org.explorer.util.GsonUtil;
import org.explorer.web3j.DefaultWeb3jFactory;
import org.explorer.web3j.Web3jFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;

/**
 * Block chain configuration
 *
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
public class BlockchainConfiguration {

    @Autowired
    private TaskExecutor eventHandlerExecutor;
    private List<EthNode> ethNodes;

    @Autowired
    public BlockchainConfiguration(EthProperties ethProperties) {
        this.ethNodes = ethProperties.getNodes();
        System.out.println("// =========================================================");
        GsonUtil.printGsonPretty(ethNodes);
        System.out.println("============================================================ //");
    }

    @PostConstruct
    private void setUp() {
        initialize();
    }

    @Bean
    public Web3jFactory web3jFactory() {
        List<EthNode> ethNodes = ethNodes();

        if (CollectionUtils.isEmpty(ethNodes)) {
            log.warn("Eth Node is empty => Terminate");
            System.exit(-1);
        }

        return new DefaultWeb3jFactory();
    }

    @Bean
    public List<BlockEventListener> blockEventListeners() {
        return Arrays.asList(blockNotificationListener());
    }

    @Bean
    public BlockNotificationListener blockNotificationListener() {
        return new BlockNotificationListener();
    }

    @Bean
    public List<EthNode> ethNodes() {
        return ethNodes;
    }

    private void initialize() {
        List<EthNode> ethNodes = ethNodes();
        Web3jFactory web3jFactory = web3jFactory();
        List<BlockEventListener> blockEventListeners = blockEventListeners();

        for (EthNode ethNode : ethNodes) {
            Web3j web3j = web3jFactory.getWeb3j(ethNode);

            // client version
            String clientVersion = null;
            try {
                clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            } catch (IOException e) {
                clientVersion = "Connection error : " + e.getMessage();
            }
            ethNode.setClientVersion(clientVersion);

            // subscribe
            if (ethNode.getSubscribe().isBlock()) {
                web3j.blockObservable(true).subscribe((onNext -> {
                    log.info("## receive new block {}({})", onNext.getBlock().getNumber().toString(10), onNext.getBlock().getHash());
                    blockEventListeners.forEach(listener -> eventHandlerExecutor.execute(() -> listener.onBlock(ethNode, onNext)));
                }), (onError -> {
                    log.error("Failed to subscribe block..", onError.getCause());
                }));
            }
        }
    }
}