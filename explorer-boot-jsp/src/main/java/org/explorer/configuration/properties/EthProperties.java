package org.explorer.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-07-03
 * @GitHub : https://github.com/zacscoding
 */
@Component
@ConfigurationProperties(prefix = "eth")
public class EthProperties {

    private static EthProperties ethProperties;

    private String test;
    private Json json;
    private Block block;
    private EthSubscribe subscribe;

    public EthProperties() {
        if (ethProperties == null) {
            ethProperties = this;
        }
    }

    public static EthProperties getEthProperties() {
        return ethProperties;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
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

    public static class Json {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Block {

        private long time;
        private long reward;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getReward() {
            return reward;
        }

        public void setReward(long reward) {
            this.reward = reward;
        }
    }

    public static class EthSubscribe {

        private boolean newBlock;

        public boolean isNewBlock() {
            return newBlock;
        }

        public void setNewBlock(boolean newBlock) {
            this.newBlock = newBlock;
        }
    }
}
