package org.explorer.entity;

import java.math.BigInteger;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.explorer.parser.BlockChainParser;
import org.explorer.util.GsonUtil;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

/**
 * @author zacconding
 * @Date 2018-04-18
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
public class BlockWrapper {

    private String hash;                // 32 bytes hash of the block
    private BigInteger number;          // block number
    private String parentHash;          // 32 Bytes - hash of the parent block
    private BigInteger nonce;           // 8 Bytes - hash of the generated proof-of-work
    private String sha3Uncles;          // 32 Bytes - SHA3 of the uncles data in the block.
    private String miner;               // 20 Bytes - the address of the beneficiary to whom the mining rewards were given.
    private BigInteger difficulty;      // integer of the difficulty for this block.
    private String extraData;           // the "extra data" field of this block.
    private BigInteger size;            // integer the size of this block in bytes.
    private BigInteger gasLimit;        // the maximum gas allowed in this block.
    private BigInteger gasUsed;         // the total used gas by all transactions in this block.
    private long timestamp;             // the unix timestamp for when the block was collated
    private List<String> transactions;  // transactions hash list
    private List<String> uncles;        // uncles`s hash list

    public BlockWrapper() {
    }

    public BlockWrapper(Block block) {
        BlockChainParser.parseBlock(this, block);
    }

    @Override
    public String toString() {
        return GsonUtil.toString(this);
    }
}