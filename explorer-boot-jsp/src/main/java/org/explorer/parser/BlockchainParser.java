package org.explorer.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.explorer.dto.BlockchainDTO;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public class BlockchainParser {
    // tag : parse block chain

    public static BlockchainDTO parseBlockDto(Block block) {
        BlockchainDTO dto = new BlockchainDTO();

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
        String nonceRaw = block.getNonceRaw();
        if (StringUtils.hasText(nonceRaw)) {
            wrapper.setNonce(block.getNonce());
        }
        wrapper.setDifficulty(block.getDifficulty().toString(10));
        wrapper.setGasLimit(block.getGasLimit());
        wrapper.setNumber(block.getNumber());
        wrapper.setParentHash(block.getParentHash());
        wrapper.setTimestamp(block.getTimestamp().longValue());
        wrapper.setExtraData(block.getExtraData());
        wrapper.setSize(block.getSize());
        wrapper.setSha3Uncles(block.getSha3Uncles());
        wrapper.setSealFields(block.getSealFields());
        wrapper.setTxCount(block.getTransactions().size());

        if (CollectionUtils.isEmpty(block.getTransactions())) {
            wrapper.setTransactions(Collections.emptyList());
        } else {
            List<String> txns = null;
            if (block.getTransactions().get(0).get() instanceof String) {
                txns = block.getTransactions().stream().map(tr -> (String) tr.get()).collect(Collectors.toList());
            } else {
                txns = block.getTransactions().stream().map(tr -> ((Transaction) tr.get()).getHash()).collect(Collectors.toList());
            }
            wrapper.setTransactions(txns);
        }
        wrapper.setTxCount(wrapper.getTransactions().size());

        // if u want to wrap more information, then add here
    }

    public static void parseBlock(Map<String, Object> blockMap, BlockWrapper wrapper) {
        String parentHashVal = (String)blockMap.get("parentHash");
        String unclesHashVal = (String)blockMap.get("sha3Uncles");
        String coinbaseVal = (String)blockMap.get("miner");
        String stateRootVal = (String) blockMap.get("stateRoot");
        String txTrieRootVal = (String) blockMap.get("transactionsRoot");
        String receiptRootVal = (String) blockMap.get("receiptsRoot");

        String logsBloomVal = (String) blockMap.get("logsBloom");
        String difficultyVal = (String) blockMap.get("difficulty");
        String gasLimitVal = (String) blockMap.get("gasLimit");
        String gasUsedHexVal = (String) blockMap.get("gasUsed");
        System.out.println(gasUsedHexVal);
        String timestampHexVal = (String) blockMap.get("timestamp");
        String extraDataVal = (String)blockMap.get("extraData");
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
        wrapper.setHash(tx.getHash());
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

    public static void parseTransactionReceipt(TransactionWrapper tx, TransactionReceipt tr) {
        if (tx == null || tr == null) {
            return;
        }

        tx.setStatus(tr.getStatus());
        tx.setCumulativeGasUsed(tr.getCumulativeGasUsed());
        tx.setContractAddress(tr.getContractAddress());
    }
    // -- endtag : parse transaction
}
