package org.explorer.entity.enums;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * Ethereum client rpc type
 *
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
public enum RpcType {
    UNKNOWN, JSON, IPC, WEBSOCKET;

    private static final Set<RpcType> TYPES = EnumSet.allOf(RpcType.class);

    public static RpcType getType(String name) {
        if (StringUtils.hasText(name)) {
            for (RpcType rpcType : TYPES) {
                if (rpcType.name().equalsIgnoreCase(name)) {
                    return rpcType;
                }
            }
        }

        return UNKNOWN;
    }
}