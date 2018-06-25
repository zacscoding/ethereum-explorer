package org.explorer.repository;

import java.math.BigInteger;
import java.util.List;
import org.explorer.entity.BlockWrapper;

/**
 * @author zacconding
 * @Date 2018-06-18
 * @GitHub : https://github.com/zacscoding
 */
public interface BlockchainRepository {

    String getClientVersion() throws Exception;

    BigInteger findBestBlockNumber() throws Exception;

    /**
     * 1) startBlockNum <= x <= lastBlockNum
     * 2) startBlockNum >= x >= lastBlockNum
     * => will be added in list from startBlockNum to lastBlockNum
     */
     List<BlockWrapper> findAllBlocks(BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception;
}
