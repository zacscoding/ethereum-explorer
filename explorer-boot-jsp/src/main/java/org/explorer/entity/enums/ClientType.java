package org.explorer.entity.enums;

/**
 * @author zacconding
 * @Date 2018-06-15
 * @GitHub : https://github.com/zacscoding
 */
public enum ClientType {
    GETH("geth"), ETHJ("ethereumj"), PARITY("parity");

    private final String type;

    ClientType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static ClientType getClientType(String type) {
        switch (type) {
            case "parity":
                return PARITY;
            default:
                return null;
        }
    }
}
