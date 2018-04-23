package org.explorer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author zacconding
 * @Date 2018-04-17
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class Web3jConfiguration {

    private static final String DEFAULT_URL = "http://localhost:8545";
    private String url = "http://192.168.5.16:9540/";

    @Bean
    public Web3j web3j() {
        if (!StringUtils.hasText(url)) {
            url = DEFAULT_URL;
        }

        return Web3j.build(new HttpService(url));
    }
}