package com.kpi.io45.bondarchuk.config;

import com.kpi.io45.bondarchuk.util.DateFormatterHelper;
import com.kpi.io45.bondarchuk.util.PrioritySorterHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class AppConfig {

    @Bean
    @Scope("singleton")
    public DateFormatterHelper dateFormatterHelper() {
        return new DateFormatterHelper();
    }

    @Bean
    @Scope("prototype")
    public PrioritySorterHelper prioritySorterHelper() {
        return new PrioritySorterHelper();
    }
}