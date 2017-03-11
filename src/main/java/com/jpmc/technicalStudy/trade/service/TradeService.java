package com.jpmc.technicalStudy.trade.service;

import com.jpmc.technicalStudy.trade.model.TradeEntity;

import java.util.List;


/**
 * Created by vikas on 3/8/17.
 */
public interface TradeService {

    public void recordTrade(TradeEntity tradeEntity);

    public List<TradeEntity> generateReport(String reportType);

    public void clearTradeEntities();
}
