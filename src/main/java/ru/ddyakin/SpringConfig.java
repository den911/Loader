package ru.ddyakin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Configuration
@ComponentScan("ru.ddyakin")
//@PropertySource(value = "classpath:${config_path}")
//@EnableCaching
public class SpringConfig implements EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(SpringConfig.class);

    private Environment env;

    @Bean
    TarantoolClientConfig tarantoolConfig() {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = env.getProperty("tarantool.user");
        config.password = env.getProperty("tarantool.password");
        return config;
    }

    @Bean
    SocketChannelProvider channelProvider() {
        return (retryNumber, lastError) -> {
            if (lastError != null) log.error("err ", lastError);
            try {
                return SocketChannel.open(new InetSocketAddress(env.getProperty("tarantool.host"),
                        Integer.valueOf(env.getProperty("tarantool.port"))));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Bean
    TarantoolClient tarantoolClient() {
        return new TarantoolClientImpl(channelProvider(), tarantoolConfig());
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

}
