package org.explorer.entity;

import com.google.gson.Gson;
import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import org.explorer.parser.BlockchainParser;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
public class TransactionWrapper {

    private String hash;                    // 32 Bytes - hash of the transaction
    private BigInteger nonce;               // the number of transactions made by the sender prior to this one
    private String blockHash;               // 32 Bytes - hash of the block where this transaction was in
    private BigInteger blockNumber;         // block number where this transaction was in
    private BigInteger transactionIndex;    // integer of the transactions index position in the block
    private String from;                    // 20 Bytes - address of the sender (Berith의 경우 파싱 해서 길이 달라짐)
    private String to;                      // 20 Bytes - address of the receiver (Berith의 경우 파싱 해서 길이 달라짐)
    private BigInteger value;               // value transferred in Wei
    private BigInteger gasPrice;                // gas price provided by the sender in GWei
    private BigInteger gas;                 // gas provided by the sender
    private String txPrice;                 // transaction fee : (gas * gasPrice) / 1000000000000000000, where gas unit is WEI
    private String input;                   // the data sent along with the transaction
    private long timestamp;                 // tx timestamp(==block timestamp)
    private BigInteger gasLimit;            // the maximum gas allowed in this block.
    private String confirms;

    /*  added from transaction receipt   */
    private BigInteger cumulativeGasUsed;   // The total amount of gas used when this transaction was executed in the block.
    private String contractAddress;         // 20 Bytes - The contract address created, if the transaction was a contract creation, otherwise null.
    private String status;                  // '0x0' indicates transaction failure , '0x1' indicates transaction succeeded.
    private boolean isPending;

    public TransactionWrapper() {
    }

    public TransactionWrapper(Transaction tx) {
        BlockchainParser.parseTransaction(this, tx);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}