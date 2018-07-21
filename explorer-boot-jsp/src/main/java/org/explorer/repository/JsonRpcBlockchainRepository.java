package org.explorer.repository;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.explorer.dto.BlockchainDTO;
import org.explorer.dto.PageListRequest;
import org.explorer.entity.AccountWrapper;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.EthNode;
import org.explorer.entity.TransactionWrapper;
import org.explorer.parser.BlockchainParser;
import org.explorer.web3j.Web3jFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * @author zacconding
 * @Date 2018-06-18
 * @GitHub : https://github.com/zacscoding
 */
@Repository
public class JsonRpcBlockchainRepository implements BlockchainRepository {

    @Autowired
    private Web3jFactory web3jFactory;

    @Override
    public String getClientVersion(EthNode ethNode) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);
        return web3j.web3ClientVersion().send().getWeb3ClientVersion();
    }

    // tag block
    @Override
    public BigInteger findBestBlockNumber(EthNode ethNode) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    @Override
    public List<BlockWrapper> findAllBlocks(EthNode ethNode, BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);
        int diff = (startBlockNum.compareTo(lastBlockNum) > 0) ? -1 : 1;

        int size = Math.abs(lastBlockNum.subtract(startBlockNum).intValue()) + 1;
        if (size == 0) {
            size = 1;
        }

        List<BlockWrapper> blocks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            BigInteger number = lastBlockNum.subtract(BigInteger.valueOf(i * diff));
            Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), false).send().getBlock();
            blocks.add(new BlockWrapper(block));
        }

        return blocks;
    }

    @Override
    public BlockchainDTO findOneBlockByNumber(EthNode ethNode, BigInteger blockNumber, boolean includeTxns) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);

        Block block = null;

        if (blockNumber != null) {
            block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), includeTxns).send().getBlock();
        }

        return block != null ? BlockchainParser.parseBlockDto(block) : null;
    }

    @Override
    public BlockchainDTO findOneBlockByHash(EthNode ethNode, String blockHash, boolean includeTxns) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);
        Block block = null;

        if (StringUtils.hasText(blockHash)) {
            block = web3j.ethGetBlockByHash(blockHash, includeTxns).send().getBlock();
        }

        return block != null ? BlockchainParser.parseBlockDto(block) : null;
    }

    // -- tag block

    // tag tx

    @Override
    public TransactionWrapper findOneTxByHash(EthNode ethNode, String hash) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);
        TransactionWrapper wrapper = null;

        Optional<Transaction> optional = web3j.ethGetTransactionByHash(hash).send().getTransaction();

        if (optional.isPresent()) {
            wrapper = BlockchainParser.parseTransaction(optional.get());

            if (!wrapper.isPending()) {
                TransactionReceipt tr = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
                BlockchainParser.parseTransactionReceipt(wrapper, tr);
            }
        }

        return wrapper;
    }

    @Override
    public List<String> findAllAccounts(EthNode ethNode) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);

        return web3j.ethAccounts().send().getAccounts();
    }

    @Override
    public BigInteger getBalance(EthNode ethNode, String address, DefaultBlockParameter defaultBlockParameter) throws Exception {
        Web3j web3j = web3jFactory.getWeb3j(ethNode);

        return web3j.ethGetBalance(address, defaultBlockParameter).send().getBalance();
    }

    // -- tag tx
}