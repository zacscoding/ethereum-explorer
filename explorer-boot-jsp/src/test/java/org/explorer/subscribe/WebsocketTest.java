package org.explorer.subscribe;

import java.net.URI;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
public class WebsocketTest {

    @Test
    public void test() throws Exception {
        String url = "http://192.168.5.77:8450/";
        WebSocketClient client = new WebSocketClient(new URI(url));
        WebSocketService service = new WebSocketService(client, false);
        service.connect();
        Web3j web3j = Web3j.build(service);

        System.out.println(web3j.web3ClientVersion().send().getWeb3ClientVersion());
    }
}
