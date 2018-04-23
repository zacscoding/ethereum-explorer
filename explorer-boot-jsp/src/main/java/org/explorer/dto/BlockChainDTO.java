package org.explorer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.explorer.entity.BlockWrapper;
import org.explorer.entity.TransactionWrapper;

/**
 * @author zacconding
 * @Date 2018-04-23
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainDTO {

    private BlockWrapper block;
    private List<TransactionWrapper> txns;
}
