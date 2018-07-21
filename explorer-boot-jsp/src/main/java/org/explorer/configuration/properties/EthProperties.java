package org.explorer.configuration.properties;

import java.util.List;
import org.explorer.entity.EthNode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-07-03
 * @GitHub : https://github.com/zacscoding
 */
@Component
@ConfigurationProperties(prefix = "eth")
@Scope("prototype")
public class EthProperties {

    List<EthNode> nodes;

    public List<EthNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<EthNode> nodes) {
        this.nodes = nodes;
    }
}