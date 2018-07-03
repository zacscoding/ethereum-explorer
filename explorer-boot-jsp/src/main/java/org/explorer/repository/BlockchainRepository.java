package org.explorer.repository;

import java.math.BigInteger;
import java.util.List;
import org.explorer.dto.BlockchainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

/**
 * @author zacconding
 * @Date 2018-06-18
 * @GitHub : https://github.com/zacscoding
 */
public interface BlockchainRepository {

    // tag block
    String getClientVersion() throws Exception;

    BigInteger findBestBlockNumber() throws Exception;

    /**
     * 1) startBlockNum <= x <= lastBlockNum
     * 2) startBlockNum >= x >= lastBlockNum
     * => will be added in list from startBlockNum to lastBlockNum
     */
    List<BlockWrapper> findAllBlocks(BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception;

    BlockchainDTO findOneBlockByNumber(BigInteger blockNumber, boolean includeTxns) throws Exception;

    BlockchainDTO findOneBlockByHash(String blockHash, boolean includeTxns) throws Exception;

    // -- tag block

    // tag tx
    TransactionWrapper findOneTxByHash(String hash) throws Exception;
    // -- tag tx
}