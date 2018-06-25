package org.explorer.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.PageListRequest;
import org.explorer.repository.BlockchainRepository;
import org.explorer.util.BIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private String clientVersion;

    // tag ethereum client info
    public String getClientVersion() throws Exception {
        if(clientVersion == null) {
            clientVersion = blockchainRepository.getClientVersion();
        }

        return clientVersion;
    }

    // -- tag ethereum client info

    // tag block
    public BigInteger findBestBlockNumber() throws Exception {
        return blockchainRepository.findBestBlockNumber();
    }

    public List<BlockWrapper> findAllBlocks(PageListRequest pageRequest) throws Exception {
        BigInteger bestBlockNumber = findBestBlockNumber();
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
        return blockchainRepository.findAllBlocks(startBlockNumber, lastBlockNumber);
    }
    // --tag block

}
