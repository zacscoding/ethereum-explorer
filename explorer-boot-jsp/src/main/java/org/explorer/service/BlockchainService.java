package org.explorer.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.dto.BlockchainDTO;
import org.explorer.entity.AccountWrapper;
import org.explorer.entity.BlockWrapper;
import org.explorer.dto.PageListRequest;
import org.explorer.entity.EthNode;
import org.explorer.entity.TransactionWrapper;
import org.explorer.repository.BlockchainRepository;
import org.explorer.util.BIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class BlockchainService {

    @Autowired
    private BlockchainRepository blockchainRepository;

    // tag ethereum client info
    public String getClientVersion(EthNode ethNode) throws Exception {
        return blockchainRepository.getClientVersion(ethNode);
    }

    public boolean isBlockSubscribe(EthNode ethNode) {
        return ethNode.getSubscribe().isBlock();
    }

    public long findBlockTime(EthNode ethNode) {
        return ethNode.getBlock().getTime();
    }

    // -- tag ethereum client info

    // tag block
    public BigInteger findBestBlockNumber(EthNode ethNode) throws Exception {
        return blockchainRepository.findBestBlockNumber(ethNode);
    }

    public List<BlockWrapper> findAllBlocks(EthNode ethNode, PageListRequest pageRequest) throws Exception {
        BigInteger bestBlockNumber = findBestBlockNumber(ethNode);
        if (bestBlockNumber == null) {
            return Collections.emptyList();
        }

        BigInteger lastBlockNumber = bestBlockNumber.subtract(BigInteger.valueOf(((pageRequest.getStart() - 1) * pageRequest.getLength())));
        if (BIUtil.isLessThan(lastBlockNumber, BigInteger.ZERO)) {
            return Collections.emptyList();
        }

        BigInteger startBlockNumber = lastBlockNumber.subtract(BigInteger.valueOf(pageRequest.getLength() - 1));
        if (BIUtil.isLessThan(startBlockNumber, BigInteger.ZERO)) {
            startBlockNumber = BigInteger.ZERO;
        }

        log.debug("## best block number : {}, start : {} - last : {}", bestBlockNumber, startBlockNumber, lastBlockNumber);
        return blockchainRepository.findAllBlocks(ethNode, startBlockNumber, lastBlockNumber);
    }

    /**
     * @param query block number decimal > hex string || block hash
     */
    public BlockchainDTO findOneBlock(EthNode ethNode, String query) throws Exception {
        try {
            return blockchainRepository.findOneBlockByNumber(ethNode, new BigInteger(query), true);
        } catch (NumberFormatException e) {
            try {
                return blockchainRepository.findOneBlockByNumber(ethNode, new BigInteger(query, 16), true);
            } catch (NumberFormatException e2) {
                return blockchainRepository.findOneBlockByHash(ethNode, query, true);
            }
        }
    }
    // --tag block

    // tag tx
    public TransactionWrapper findOneTxByHash(EthNode ethNode, String query) throws Exception {
        TransactionWrapper tx = blockchainRepository.findOneTxByHash(ethNode, query);

        if (tx != null && !tx.isPending()) {
            BigInteger bestBlockNumber = blockchainRepository.findBestBlockNumber(ethNode);
            tx.setConfirms(bestBlockNumber.subtract(tx.getBlockNumber()).toString());
            BlockchainDTO blockchainDTO = blockchainRepository.findOneBlockByNumber(ethNode, tx.getBlockNumber(), false);
            tx.setTimestamp(blockchainDTO.getBlock().getTimestamp());
        }

        return tx;
    }
    // --tag tx

    // tag accounts
    public List<AccountWrapper> findAllAccounts(EthNode ethNode, PageListRequest pageRequest) throws Exception {
        List<String> addrs = blockchainRepository.findAllAccounts(ethNode);
        System.out.println("## accounts size : " + addrs.size());

        int startIdx = (pageRequest.getStart() - 1) * pageRequest.getLength();
        System.out.println("## start idx : " + startIdx);

        if (CollectionUtils.isEmpty(addrs) || startIdx > addrs.size() - 1) {
            return Collections.emptyList();
        }

        int lastIdx = Math.min((startIdx + pageRequest.getLength() - 1), addrs.size() - 1);
        System.out.println("## calc last offset : " + (startIdx + pageRequest.getLength() - 1));
        System.out.println("## lastIdx idx : " + lastIdx);
        System.out.println("## addr size : " + addrs.size());
        if (startIdx == lastIdx) {
            addrs = Arrays.asList(addrs.get(startIdx));
        } else {
            addrs = addrs.subList(startIdx, lastIdx);
        }

        List<AccountWrapper> accounts = new ArrayList<>(addrs.size());
        for (String addr : addrs) {
            AccountWrapper accountWrapper = new AccountWrapper();
            accounts.add(accountWrapper);

            accountWrapper.setAddress(addr);
            try {
                BigInteger balance = blockchainRepository.getBalance(ethNode, addr, DefaultBlockParameterName.LATEST);
                accountWrapper.setBalance(Convert.fromWei(balance.toString(), Unit.ETHER).toPlainString());
            } catch (IOException e) {
                accountWrapper.setBalance("ERROR");
            }
        }

        return accounts;
    }

    // --tag accounts
}
