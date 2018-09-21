package org.explorer.web3j;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.explorer.entity.EthNode;
import org.explorer.entity.enums.RpcType;
import org.explorer.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultWeb3jFactory implements Web3jFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWeb3jFactory.class);
    private static final Object LOCK = new Object();
    private Map<EthNode, Web3jService> serviceMap;
    private Map<EthNode, Web3j> web3jMap;

    @PostConstruct
    private void setUp() {
        this.serviceMap = new HashMap<>();
        this.web3jMap = new HashMap<>();
    }

    @Override
    public Web3j getWeb3j(EthNode ethNode) {
        Objects.requireNonNull(ethNode, "EthNode must be not null");
        Web3j web3j = null;

        if ((web3j = web3jMap.get(ethNode)) == null) {
            synchronized (LOCK) {
                if ((web3j = web3jMap.get(ethNode)) == null) {
                    Web3jService service = getService(ethNode);
                    web3j = Web3j.build(service);
                    web3jMap.put(ethNode, web3j);
                }
            }
        }

        return web3j;
    }

    @Override
    public Web3jService getService(EthNode ethNode) {
        Objects.requireNonNull(ethNode, "EthNode must be not null");
        Web3jService service = null;

        if ((service = serviceMap.get(ethNode)) == null) {
            synchronized (LOCK) {
                if ((service = serviceMap.get(ethNode)) == null) {
                    RpcType rpcType = RpcType.getType(ethNode.getRpc().getType());
                    String url = ethNode.getRpc().getUrl();
                    switch (rpcType) {
                        case JSON:
                            service = new HttpService(url);
                            break;
                        case IPC:
                            service = (OSUtil.isWindows()) ? new WindowsIpcService(url) : new UnixIpcService(url);
                            break;
                        case WEBSOCKET:
                            try {
                                WebSocketClient client = new WebSocketClient(new URI(url));
                                WebSocketService webSocketService = new WebSocketService(client, false);
                                webSocketService.connect();
                                service = webSocketService;
                                break;
                            } catch (URISyntaxException e) {
                                logger.error("failed to connect node : " + ethNode);
                                throw new RuntimeException(e);
                            } catch (ConnectException e) {
                                e.printStackTrace();
                            }
                        default:
                            throw new RuntimeException("Not supported rpc type : " + ethNode.getRpc().getType());
                    }

                    serviceMap.put(ethNode, service);
                }
            }
        }

        return service;
    }
}
