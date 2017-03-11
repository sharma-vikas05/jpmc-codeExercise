package com.jpmc.technicalStudy.trade.api;

import com.jpmc.technicalStudy.trade.config.TradeConfig;

import com.jpmc.technicalStudy.trade.exception.TradeException;
import com.jpmc.technicalStudy.trade.model.TradeEntity;
import com.jpmc.technicalStudy.trade.service.TradeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by vikas on 3/8/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TradeConfig.class})
public class TradeServiceTest {

    @Autowired
    @Qualifier("tradeService")
    private TradeService tradeService;

    @Autowired
    @Qualifier("messageSource")
    private ResourceBundleMessageSource messageSource;

    private TradeEntity bar;
    private TradeEntity foo;
    private TradeEntity baz;

    private final String EMPTY_MESSAGE = "Required field(s) are not present.";
    private final String INCOMING_REPORT_TYPE = "IncomingReport";
    private final String OUTGOING_REPORT_TYPE = "OutgoingReport";

    @Before
    public void setup(){

        foo =  new TradeEntity("foo", "B", 0.50F, "SGP", "01 Jan 2016", "02 Jan 2016", 200, 100.25F);
        bar = new TradeEntity("bar", "S", 0.22F, "AED", "05 Jan 2016", "08 Jan 2016", 450, 150.5F);
        baz = new TradeEntity("baz", "S", 0.32F, "GBP", "05 Jan 2016", "07 Jan 2016", 300, 250.5F);

        tradeService.clearTradeEntities();
    }

    @Test(expected = TradeException.class)
    public void testrecordTradeException() {
        //test to verify Error message if blank Entity name is being passed.

        TradeEntity blankEntityName = new TradeEntity("", "B", 0.50F, "SGP", "01-Jan-2016", "02-Jan-2016", 200, 100.25F);
        try {

            tradeService.recordTrade(blankEntityName);

        } catch(TradeException te){

            Assert.assertEquals(te.getMessage(), messageSource.getMessage("empty.message", new Object[]{}, Locale.getDefault()));
            throw te;
        }
    }

    @Test(expected = TradeException.class)
    public void testReportType() {
        //test to verify reportType value

        String reportType = "";
        try {
            tradeService.generateReport(reportType);
        }catch (TradeException te){
            Assert.assertEquals(te.getMessage(), messageSource.getMessage("empty.reportType", new Object[]{}, Locale.getDefault()));
        }
        reportType = "invalid";
        try {
            tradeService.generateReport(reportType);
        }catch (TradeException te){
            Assert.assertEquals(te.getMessage(), messageSource.getMessage("invalid.reportType", new Object[]{}, Locale.getDefault()));
            throw te;
        }
    }

    @Test
    public void testWeekendForSettlementDate(){
        //test to verify Settlement date moving forward for weekend i.e. Saturday and Sunday

        tradeService.recordTrade(foo);
        List<TradeEntity> outgoingList = tradeService.generateReport(OUTGOING_REPORT_TYPE);

        Assert.assertNotNull(outgoingList);
        Assert.assertEquals(1, outgoingList.size());
        Assert.assertEquals("4 Jan 2016", outgoingList.get(0).getSettlementDate());

        //Setting date to Sunday, it should be moved ahead by ONE day
        foo.setSettlementDate("03 Jan 2016");

        outgoingList = tradeService.generateReport(OUTGOING_REPORT_TYPE);

        Assert.assertNotNull(outgoingList);
        Assert.assertEquals(1, outgoingList.size());
        Assert.assertEquals("4 Jan 2016", outgoingList.get(0).getSettlementDate());

    }

    @Test
    public void testMiddleEastWeekendForSettlementDate(){
        //test to verify Settlement date moving forward for MiddleEast weekend i.e. Friday and Saturday
        tradeService.recordTrade(bar);
        List<TradeEntity> outgoingList = tradeService.generateReport(INCOMING_REPORT_TYPE);

        Assert.assertNotNull(outgoingList);
        Assert.assertEquals(1, outgoingList.size());
        Assert.assertEquals("10 Jan 2016", outgoingList.get(0).getSettlementDate());

        //Setting date as Saturday, it should be moved ahead by ONE day
        bar.setSettlementDate("09 Jan 2016");

        outgoingList = tradeService.generateReport(INCOMING_REPORT_TYPE);

        Assert.assertNotNull(outgoingList);
        Assert.assertEquals(1, outgoingList.size());
        Assert.assertEquals("10 Jan 2016", outgoingList.get(0).getSettlementDate());
    }

    @Test
    public void testWeekdayForSettlementDate(){
        //test to verify Settlement remains same if it falls on weekday.

        tradeService.recordTrade(baz);
        List<TradeEntity> incomingList = tradeService.generateReport(INCOMING_REPORT_TYPE);

        Assert.assertNotNull(incomingList);
        Assert.assertEquals(1, incomingList.size());
        Assert.assertEquals("7 Jan 2016", incomingList.get(0).getSettlementDate());
    }

    @Test
    public void testIncomingReportType(){
        //test to verify Incoming Report data and order of entities

        tradeService.recordTrade(baz);
        tradeService.recordTrade(bar);
        List<TradeEntity> incomingList = tradeService.generateReport(INCOMING_REPORT_TYPE);

        Assert.assertNotNull(incomingList);
        Assert.assertEquals(2, incomingList.size());
        Assert.assertEquals("baz", incomingList.get(0).getEntityName());
        Assert.assertEquals(24048.0F, incomingList.get(0).getTotalPrice(),0.0);

    }

    @Test
    public void testOutgoingReportType(){
        //test to verify Outgoing Report data and order of entities
        tradeService.recordTrade(foo);
        tradeService.recordTrade(bar);
        List<TradeEntity> outgoingList = tradeService.generateReport(OUTGOING_REPORT_TYPE);

        Assert.assertNotNull(outgoingList);
        Assert.assertEquals(1, outgoingList.size());
        Assert.assertEquals("foo", outgoingList.get(0).getEntityName());
        Assert.assertEquals(10025.0F, outgoingList.get(0).getTotalPrice(),0.0);

    }
}
