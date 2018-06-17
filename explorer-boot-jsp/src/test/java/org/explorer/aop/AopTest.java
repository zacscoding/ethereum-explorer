package org.explorer.aop;

import static org.junit.Assert.assertTrue;

import org.explorer.entity.PageListRequest;
import org.explorer.web.BlockchainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AopTest {

    @Autowired
    BlockchainController blockchainController;

    @Test
    public void checkAop() {
        PageListRequest pageRequest = new PageListRequest(0, 10);
        blockchainController.getBlocks(pageRequest);
        assertTrue(pageRequest.getStart() == 1);
    }
}
