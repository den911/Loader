package ru.ddyakin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class App extends SpringBootServletInitializer {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(App.class);
        springApplication.run(App.class);
        log.info("Application is runned");
    }
}
