package ru.ddyakin;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by ddyakin on 14.05.17.
 */

@Configuration
@ComponentScan("ru.ddyakin")
//@PropertySource(value = "classpath:${config_path}")
//@EnableCaching
public class SpringConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
