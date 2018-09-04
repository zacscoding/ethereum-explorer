package org.explorer.web3j;

import org.explorer.entity.EthNode;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
public interface Web3jFactory {

    Web3j getWeb3j(EthNode ethNode);

    Web3jService getService(EthNode ethNode);
}