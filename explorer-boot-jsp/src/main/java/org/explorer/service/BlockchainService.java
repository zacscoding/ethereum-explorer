package org.explorer.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.PageListRequest;
import org.explorer.util.BIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class BlockchainService {

    @Autowired
    private Web3j web3j;

    // tag block
    public BigInteger findBestBlockNumber() throws Exception {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    public List<BlockWrapper> findAllBlocks(PageListRequest pageRequest) throws Exception {
        BigInteger bestBlockNumber = findBestBlockNumber();
        if (bestBlockNumber == null) {
            return Collections.emptyList();
        }

        BigInteger lastBlockNumber = bestBlockNumber.subtract(BigInteger.valueOf(((pageRequest.getStart() - 1) * pageRequest.getLength())));
        if (BIUtil.isLessThan(lastBlockNumber, BigInteger.ZERO)) {
            return Collections.emptyList();
        }

        BigInteger startBlockNumber = lastBlockNumber.subtract(BigInteger.valueOf(pageRequest.getLength() - 1));
        if (BIUtil.isLessThan(startBlockNumber, BigInteger.ZERO)) {
            startBlockNumber = BigInteger.ZERO;
        }

        log.debug("## best block number : {}, start : {} - last : {}", bestBlockNumber, startBlockNumber, lastBlockNumber);
        return findAllBlock(startBlockNumber, lastBlockNumber);
    }

    /**
     * 1) startBlockNum <= x <= lastBlockNum
     * 2) startBlockNum >= x >= lastBlockNum
     * => will be added in list from startBlockNum to lastBlockNum
     */
    public List<BlockWrapper> findAllBlock(BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception {
        log.info("# request find all blocks startBlockNum : {}, lastBlockNum : {}", startBlockNum, lastBlockNum);
        int diff = (startBlockNum.compareTo(lastBlockNum) > 0) ? -1 : 1;

        int size = Math.abs(lastBlockNum.subtract(startBlockNum).intValue()) + 1;
        if (size == 0) {
            size = 1;
        }

        List<BlockWrapper> blocks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            BigInteger number = lastBlockNum.subtract(BigInteger.valueOf(i * diff));
            log.info("" + number.toString(10));
            Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), false).send().getBlock();
            blocks.add(new BlockWrapper(block));
        }

        return blocks;
    }
    // --tag block
}
