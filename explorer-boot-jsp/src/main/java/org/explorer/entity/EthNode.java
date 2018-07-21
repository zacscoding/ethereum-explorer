package org.explorer.entity;

import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-07-21
 * @GitHub : https://github.com/zacscoding
 */
public class EthNode {

    /**
     * Will be diff
     */
    private String nodeName;
    private String clientVersion;
    private Rpc rpc;
    private Block block;
    private EthSubscribe subscribe;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Rpc getRpc() {
        return rpc;
    }

    public void setRpc(Rpc rpc) {
        this.rpc = rpc;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public EthSubscribe getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(EthSubscribe subscribe) {
        this.subscribe = subscribe;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public static class Rpc {

        private String type;
        private String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Block {

        private long time;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public static class EthSubscribe {

        private boolean block;

        public boolean isBlock() {
            return block;
        }

        public void setBlock(boolean block) {
            this.block = block;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EthNode)) {
            return false;
        }
        EthNode ethNode = (EthNode) o;
        return Objects.equals(getNodeName(), ethNode.getNodeName());
    }

    @Override
    public int hashCode() {
        return nodeName.hashCode();
    }
}
