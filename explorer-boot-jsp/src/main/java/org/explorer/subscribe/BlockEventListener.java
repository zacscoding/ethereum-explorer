package org.explorer.subscribe;

import org.explorer.entity.EthNode;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public interface BlockEventListener {

    void onBlock(EthNode ethNode, EthBlock ethBlock);

    void onPendingTransaction(EthNode ethNode, Transaction tx);
}
