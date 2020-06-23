package com.freelancer.catpcha.solver.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Timer;

@Configuration
public class TimerConfig {

    @Bean
    @Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
    public Timer getTimer() {
        return new Timer(true);
    }

}
