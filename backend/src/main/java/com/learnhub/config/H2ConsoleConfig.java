package com.learnhub.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("h2")
public class H2ConsoleConfig {
    @Bean
    ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        return new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
    }
}
