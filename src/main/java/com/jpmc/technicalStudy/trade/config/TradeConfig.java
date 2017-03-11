package com.jpmc.technicalStudy.trade.config;

import com.jpmc.technicalStudy.trade.service.TradeService;
import com.jpmc.technicalStudy.trade.service.impl.TradeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Created by vikas on 3/8/17.
 */

@Configuration
public class TradeConfig {

    @Bean(name="tradeService")
    public TradeService tradeService() {
        return new TradeServiceImpl();
    }

    @Bean(name="messageSource")
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
