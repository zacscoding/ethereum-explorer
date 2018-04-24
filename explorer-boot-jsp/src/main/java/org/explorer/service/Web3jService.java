package org.explorer.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.parser.BlockChainParser;
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

    public List<BlockWrapper> findAllBlock(BigInteger startBlockNum, BigInteger lastBlockNum) throws Exception {
        if (startBlockNum.compareTo(lastBlockNum) > 0) {
            throw new RuntimeException("Start block number must be smaller than last block number");
        }

        List<BlockWrapper> blocks = new ArrayList<>(lastBlockNum.subtract(startBlockNum).intValue());

        web3j.replayBlocksObservable(
            DefaultBlockParameter.valueOf(startBlockNum),
            DefaultBlockParameter.valueOf(lastBlockNum),
            false).subscribe(ethBlock -> blocks.add(new BlockWrapper(ethBlock.getBlock())));

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
