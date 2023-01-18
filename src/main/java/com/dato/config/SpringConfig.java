package com.dato.config;

import com.dato.processor.MethodValidationInitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MethodValidationInitializingBean getProcessor(){
        return new MethodValidationInitializingBean();
    }
}
