package org.explorer.service;

import java.math.BigInteger;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.parser.BlockChainParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
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


    public BlockChainDTO getLastBlock() {
        try {
            Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock();
            return BlockChainParser.parseBlockDto(block);
        } catch (Exception e) {
            return null;
        }
    }
}
