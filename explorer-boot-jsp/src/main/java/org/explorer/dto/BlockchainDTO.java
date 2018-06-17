package org.explorer.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
public class BlockchainDTO {

    private BlockWrapper block;
    private List<TransactionWrapper> txns;
}
