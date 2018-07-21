package org.explorer.repository;

import java.math.BigInteger;
import java.util.List;
import org.explorer.dto.BlockchainDTO;
import org.explorer.dto.PageListRequest;
import org.explorer.entity.AccountWrapper;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.EthNode;
import org.explorer.entity.TransactionWrapper;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

/**
 * @author zacconding
 * @Date 2018-06-18
 * @GitHub : https://github.com/zacscoding
 */
public interface BlockchainRepository {

    // tag block
    String getClientVersion(EthNode ethNode) throws Exception;

    BigInteger findBestBlockNumber(EthNode ethNode) throws Exception;

    /**
     * 1) startBlockNum <= x <= lastBlockNum
     * 2) startBlockNum >= x >= lastBlockNum
     * => will be added in list from startBlockNum to lastBlockNum
     */
    List<BlockWrapper> findAllBlocks(EthNode ethNode, BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception;

    BlockchainDTO findOneBlockByNumber(EthNode ethNode, BigInteger blockNumber, boolean includeTxns) throws Exception;

    BlockchainDTO findOneBlockByHash(EthNode ethNode, String blockHash, boolean includeTxns) throws Exception;

    // -- tag block

    // tag tx
    TransactionWrapper findOneTxByHash(EthNode ethNode, String hash) throws Exception;
    // -- tag tx

    // tag account
    List<String> findAllAccounts(EthNode ethNode) throws Exception;

    BigInteger getBalance(EthNode ethNode, String address, DefaultBlockParameter defaultBlockParameter) throws Exception;
    // -- tag account
}