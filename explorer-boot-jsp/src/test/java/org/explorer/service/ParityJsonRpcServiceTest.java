package org.explorer.service;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ParityJsonRpcServiceTest {

    @Autowired
    private ParityJsonRpcService parityJsonRpcService;

    @Test
    public void initialize() {
        /*//assertTrue(parityJsonRpcService.getJsonRpcSpecs().size() == 5L);

        List<String> methods = parityJsonRpcService.getMethods("eth_pubsub");
        assertTrue(methods.size() == 2);
        assertTrue(methods.indexOf("eth_subscribe") >= 0);
        assertTrue(methods.indexOf("eth_unsubscribe") >= 0);*/
    }
}
