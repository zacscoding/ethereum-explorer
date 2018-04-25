package org.explorer.service;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;
import org.explorer.parser.BlockChainParser;
import org.explorer.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class Web3jService {

    @Autowired
    private Web3j web3j;

    // tag :: block
    public BlockChainDTO findLastBlock() throws Exception {
        Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock();
        return BlockChainParser.parseBlockDto(block);
    }

    public BigInteger findLastBlockNum() throws Exception {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    public BlockChainDTO findBlockByKeyword(String keyword) throws Exception {
        Block block = null;
        try {
            BigInteger bigInteger = new BigInteger(keyword);
            block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(bigInteger), true).send().getBlock();
        } catch (NumberFormatException e) {
            block = web3j.ethGetBlockByHash(keyword, true).send().getBlock();
        }

        if (block == null) {
            return null;
        }

        BlockChainDTO dto = BlockChainParser.parseBlockDto(block);

        if (CollectionUtil.isNotEmpty(dto.getTxns())) {
            List<TransactionWrapper> txns = dto.getTxns();
            for (TransactionWrapper tx : txns) {
                log.info("## request " + tx.getTransactionIndex());
                web3j.ethGetTransactionReceipt(tx.getHash()).send().getTransactionReceipt().ifPresent(tr -> {
                    log.info("# Receive : " + tx.getTransactionIndex());
                    BlockChainParser.parseTransactionReceipt(tx, tr);
                });
            }
        }

        return dto;
    }

    /**
     * 1) startBlockNum <= x <= lastBlockNum
     * 2) startBlockNum >= x >= lastBlockNum
     * => will be added in list from startBlockNum to lastBlockNum
     */
    public List<BlockWrapper> findAllBlock(BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception {
        log.info("# request find all blocks startBlockNum : {}, lastBlockNum : {}", startBlockNum, lastBlockNum);
        int diff = (startBlockNum.compareTo(lastBlockNum) > 0) ? -1 : 1;

        int size = Math.abs(lastBlockNum.subtract(startBlockNum).intValue());
        List<BlockWrapper> blocks = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            BigInteger number = startBlockNum.add(BigInteger.valueOf(i * diff));
            log.info("# get block : " + number);
            Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), false).send().getBlock();
            blocks.add(new BlockWrapper(block));
        }

        return blocks;
    }

    public List<BlockWrapper> findAllBlockToLastest(BigInteger startBlockNum) throws Exception {
        BigInteger lastBlockNum = findLastBlockNum();
        return findAllBlock(startBlockNum, lastBlockNum);
    }

    // -- end tag :: block

    // tag :: transaction

    // -- end tag :: transaction

    // tag :: account

    // -- end tag :: account


}
