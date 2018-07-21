package org.explorer.web3j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.explorer.entity.EthNode;
import org.explorer.entity.enums.RpcType;
import org.explorer.util.OSUtil;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

/**
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
public class Web3jFactory {

    private static final Object LOCK = new Object();
    private Map<EthNode, Web3j> ethWeb3js = new HashMap<>();

    public Web3j getWeb3j(EthNode ethNode) {
        Objects.requireNonNull(ethNode, "ethNode must be not null");
        Web3j web3j = ethWeb3js.get(ethNode);

        if (web3j == null) {
            synchronized (LOCK) {
                if ((web3j = ethWeb3js.get(ethNode)) == null) {
                    RpcType rpcType = RpcType.getType(ethNode.getRpc().getType());
                    Web3jService web3jService = null;
                    String url = ethNode.getRpc().getUrl();
                    switch (rpcType) {
                        case JSON:
                            web3jService = new HttpService(url);
                            break;
                        case IPC:
                            web3jService = (OSUtil.isWindows()) ? new WindowsIpcService(url) : new UnixIpcService(url);
                            break;
                        case WEBSOCKET:
                            throw new RuntimeException("Not supported rpc type : " + ethNode.getRpc().getType());
                        default:
                            throw new RuntimeException("Not supported rpc type : " + ethNode.getRpc().getType());
                    }
                    web3j = Web3j.build(web3jService);
                }
            }
        }

        return web3j;
    }
}
