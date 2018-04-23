package org.explorer.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.explorer.dto.BlockChainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * @author zacconding
 * @Date 2018-04-18
 * @GitHub : https://github.com/zacscoding
 */
public class BlockChainParser {

    // tag : parse block chain

    public static BlockChainDTO parseBlockDto(Block block) {
        BlockChainDTO dto = new BlockChainDTO();

        // parse block
        BlockWrapper blockWrapper = new BlockWrapper(block);
        dto.setBlock(blockWrapper);

        if (block.getTransactions() != null) {
            List<TransactionWrapper> txns = new ArrayList<>(block.getTransactions().size());
            block.getTransactions().forEach(tx -> {
                txns.add(new TransactionWrapper((Transaction) tx.get()));
            });
            dto.setTxns(txns);
        }

        return dto;
    }

    // -- end tag : parse block chain

    // tag : parse block
    public static BlockWrapper parseBlock(Block block) {
        if (block == null) {
            return null;
        }

        return new BlockWrapper(block);
    }

    public static void parseBlock(BlockWrapper wrapper, Block block) {
        if (wrapper == null || block == null) {
            return;
        }

        String hash = block.getHash();
        wrapper.setHash(hash == null ? "pending" : hash);

        String miner = block.getMiner();
        wrapper.setMiner(miner == null ? "pending" : miner);

        wrapper.setGasLimit(block.getGasLimit());
        wrapper.setGasUsed(block.getGasUsed());
        wrapper.setNonce(block.getNonce());
        wrapper.setDifficulty(block.getDifficulty());
        wrapper.setGasLimit(block.getGasLimit());
        wrapper.setNumber(block.getNumber());
        wrapper.setParentHash(block.getParentHash());
        wrapper.setTimestamp(block.getTimestamp().longValue());
        wrapper.setExtraData(block.getExtraData());
        wrapper.setSize(block.getSize());
        wrapper.setSha3Uncles(block.getSha3Uncles());

        // if u want to wrap more information, then add here
    }

    // -- end tag : parse block

    // tag : parse transaction
    public static TransactionWrapper parseTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionWrapper(transaction);
    }

    public static void parseTransaction(TransactionWrapper wrapper, Transaction tx) {
        if (wrapper == null || tx == null) {
            return;
        }

        // check pending
        String blockHash = tx.getBlockHash();
        if (blockHash == null) {
            wrapper.setPending(true);
        } else {
            wrapper.setBlockHash(tx.getBlockHash());
            wrapper.setBlockNumber(tx.getBlockNumber());
        }

        // calculate tx price
        BigInteger gas = tx.getGas();
        BigInteger gasPrice = tx.getGasPrice();
        String txPrice = Convert.fromWei(gas.multiply(gasPrice).toString(), Unit.ETHER).toPlainString();
        wrapper.setGas(gas);
        wrapper.setGasPrice(gasPrice);
        wrapper.setTxPrice(txPrice);

        wrapper.setFrom(tx.getFrom());
        wrapper.setTo(tx.getTo());
        wrapper.setInput(tx.getInput());
        wrapper.setNonce(tx.getNonce());
        wrapper.setTransactionIndex(tx.getTransactionIndex());
        wrapper.setValue(tx.getValue());

        // if u want to wrap more information, then add here
    }

    // -- endtag : parse transaction
}