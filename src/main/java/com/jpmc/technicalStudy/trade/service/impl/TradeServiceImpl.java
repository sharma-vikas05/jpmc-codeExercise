package com.jpmc.technicalStudy.trade.service.impl;

import com.jpmc.technicalStudy.trade.exception.TradeException;
import com.jpmc.technicalStudy.trade.model.TradeEntity;
import com.jpmc.technicalStudy.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by vikas on 3/8/17.
 */
@Service("tradeService")
public class TradeServiceImpl implements TradeService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    private Map<String, List<TradeEntity>> tradeEntityMap = new HashMap<String, List<TradeEntity>>();

    private List<TradeEntity> incomingList ;

    private List<TradeEntity> outgoingList;

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceImpl.class);

    private final static String INCOMING_REPORT_TYPE = "IncomingReport";

    private final static String OUTGOING_REPORT_TYPE = "OutgoingReport";

    private final static String BUY_FLAG = "B";

    private final static String SELL_FLAG = "S";

    public void recordTrade(TradeEntity tradeEntity) {

        LOGGER.info("Start TradeServiceImpl::recordTrade(), entityType: " + tradeEntity.getEntityName() );

        List<TradeEntity> tradeEntityList;

        validateTradeEntity(tradeEntity);

        List<TradeEntity> tradeEntityList = this.tradeEntityMap.getOrDefault(tradeEntity.getEntityName() , new ArrayList<TradeEntity>());

        tradeEntityList.add(tradeEntity);
        this.tradeEntityMap.put(tradeEntity.getEntityName(), tradeEntityList);

        LOGGER.info("End TradeServiceImpl::recordTrade()");

    }

    public List<TradeEntity> generateReport(String reportType) {

        LOGGER.info("Start TradeServiceImpl::generateReport() ");

        incomingList = new ArrayList<TradeEntity>();
        outgoingList = new ArrayList<TradeEntity>();

        validateReportType(reportType);

        this.tradeEntityMap.forEach((key,tradeEntities)->{

            tradeEntities.forEach((tradeEntity) -> {

                String settlementDate = checkWeekend(tradeEntity) ;

                tradeEntity.setSettlementDate(settlementDate);

                if(BUY_FLAG.equals(tradeEntity.getBuySellFlag()){
                    outgoingList.add(tradeEntity);
                }else if (SELL_FLAG.equals(tradeEntity.getBuySellFlag()){
                    incomingList.add(tradeEntity);
                }
            });

        });

        if(INCOMING_REPORT_TYPE.equals(reportType)){

            Collections.sort(incomingList, Collections.reverseOrder());

            LOGGER.info("********Printing Incoming List**********");
            printList(incomingList);
            LOGGER.info("**********END*************");

            LOGGER.info("End TradeServiceImpl::generateReport() ");

            return incomingList;

        }else {

            Collections.sort(outgoingList, Collections.reverseOrder());

            LOGGER.info("********Printing Outgoing List**********");
            printList(outgoingList);
            LOGGER.info("**********END*************");

            LOGGER.info("End TradeServiceImpl::generateReport() ");

            return outgoingList;
        }

    }

    @Override
    public void clearTradeEntities() {
        this.tradeEntityMap = new HashMap<String, List<TradeEntity>>();
    }

    private void validateReportType(String reportType) {

        if(StringUtils.isEmpty(reportType)){

            throw new TradeException(messageSource.getMessage("empty.reportType", new Object[]{}, Locale.getDefault()));

        } else if(!(INCOMING_REPORT_TYPE.equals(reportType) || OUTGOING_REPORT_TYPE.equals(reportType))){

            throw new TradeException(messageSource.getMessage("invalid.reportType", new Object[]{}, Locale.getDefault()));

        }
    }


    private void printList(List<TradeEntity> printList){

        LOGGER.info(String.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s" , "Entity_Name", "Buy/Sell", "AgreedFx",
                "Currency", "Instruction_Date","Settlement_Date", "Units", "Price/Unit", "Total_Price(USD)"));

        printList.forEach((tradeEntity)-> {

            LOGGER.info(String.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s", tradeEntity.getEntityName()
                    , tradeEntity.getBuySellFlag() , tradeEntity.getAgreedFx(), tradeEntity.getCurrencyCode(), tradeEntity.getInstructionDate()
                    , tradeEntity.getSettlementDate(), tradeEntity.getUnits(), tradeEntity.getPrice(), tradeEntity.getTotalPrice()));

        });

    }

    private String checkWeekend(TradeEntity tradeEntity){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");;

        Set<DayOfWeek> weekend = EnumSet.of( DayOfWeek.SATURDAY , DayOfWeek.SUNDAY );
        Set<DayOfWeek> middleEastWeekend = EnumSet.of( DayOfWeek.FRIDAY, DayOfWeek.SATURDAY );
        Set<String> middleEastCountryCode = new HashSet<String>();
        middleEastCountryCode.add("AED");
        middleEastCountryCode.add("SAR");

        LocalDate settlementDate = LocalDate.parse(tradeEntity.getSettlementDate(), formatter);
        DayOfWeek dow = settlementDate.getDayOfWeek();

        if(middleEastCountryCode.contains(tradeEntity.getCurrencyCode())
                && middleEastWeekend.contains(dow)){

            if(dow == DayOfWeek.FRIDAY){

                settlementDate = settlementDate.plusDays(2);

            }else if(dow == DayOfWeek.SATURDAY){

                settlementDate = settlementDate.plusDays(1);

            }
        } else if(weekend.contains(dow)){

            if(dow == DayOfWeek.SATURDAY){

                settlementDate = settlementDate.plusDays(2);

            }else if(dow == DayOfWeek.SUNDAY){

                settlementDate = settlementDate.plusDays(1);

            }
        }

        return formatter.format(settlementDate);
    }

    private void validateTradeEntity(TradeEntity tradeEntity){

        if(null != tradeEntity){

            if(StringUtils.isEmpty(tradeEntity.getEntityName())
                    || StringUtils.isEmpty(tradeEntity.getBuySellFlag())
                    || StringUtils.isEmpty(tradeEntity.getAgreedFx())
                    || StringUtils.isEmpty(tradeEntity.getCurrencyCode())
                    || StringUtils.isEmpty(tradeEntity.getInstructionDate())
                    || StringUtils.isEmpty(tradeEntity.getSettlementDate())
                    || StringUtils.isEmpty(tradeEntity.getUnits())
                    || StringUtils.isEmpty(tradeEntity.getPrice())
                    ){
                throw new TradeException(messageSource.getMessage("empty.message", new Object[]{}, Locale.getDefault()));
            }

        }else {
            throw new TradeException(messageSource.getMessage("empty.message", new Object[]{}, Locale.getDefault()));
        }

    }

}
