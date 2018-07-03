package org.explorer.configuration;

import org.explorer.configuration.properties.EthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-07-03
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@EnableConfigurationProperties(EthProperties.class)
public class AppConfiguration {
}
