package com.jpmc.technicalStudy.trade.config;

import com.jpmc.technicalStudy.trade.exception.TradeException;
import com.jpmc.technicalStudy.trade.model.TradeEntity;
import com.jpmc.technicalStudy.trade.service.TradeService;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import org.slf4j.Logger;

/**
 * Created by vikas on 3/9/17.
 */
public class TradeMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeMain.class);

    private final static String INCOMING_REPORT_TYPE = "IncomingReport";

    private final static String OUTGOING_REPORT_TYPE = "OutgoingReport";

    private static TradeEntity foo;

    private static TradeEntity bar;

    private static TradeEntity baz;

    static {

        foo = new TradeEntity("foo", "B", 0.50F, "SGP", "01 Jan 2016", "02 Jan 2016", 200, 100.25F);

        bar = new TradeEntity("bar", "S", 0.22F, "AED", "05 Jan 2016", "07 Jan 2016", 450, 150.5F);

        baz = new TradeEntity("baz", "S", 0.32F, "GBP", "05 Jan 2016", "07 Jan 2016", 300, 250.5F);
    }

    public static void main(String args[]) {

        LOGGER.info("Start TradeMain::main()");

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(TradeConfig.class);

        LOGGER.info("After initialiazing context");

        TradeService tradeService = (TradeService)context.getBean("tradeService");

        try {

            tradeService.recordTrade(foo);
            tradeService.recordTrade(bar);
            tradeService.recordTrade(baz);

            tradeService.generateReport(INCOMING_REPORT_TYPE);

            tradeService.generateReport(OUTGOING_REPORT_TYPE);

        }catch(TradeException te){
            LOGGER.error("Error during validation " + te.getMessage());
            throw te;
        }catch(Exception e){
            LOGGER.error("Error during processing " + e.getMessage());
            throw new TradeException("");
        }

        LOGGER.info("End TradeMain::main()");
    }

}
