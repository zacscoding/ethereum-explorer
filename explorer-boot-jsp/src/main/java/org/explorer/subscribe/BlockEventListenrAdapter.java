package org.explorer.subscribe;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public class BlockEventListenrAdapter implements BlockEventListener {

    @Override
    public void onBlock(EthBlock ethBlock) {
    }

    @Override
    public void onPendingTransaction(Transaction tx) {
    }
}