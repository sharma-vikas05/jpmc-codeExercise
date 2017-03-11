package com.jpmc.technicalStudy.trade.model;

/**
 * Created by vikas on 3/8/17.
 */
public class TradeEntity implements Comparable{

    private String entityName;

    private String buySellFlag;

    private Float agreedFx;

    private String currencyCode;

    private String instructionDate;

    private String settlementDate;

    private Integer units;

    private Float price;

    private Float totalPrice;

    public TradeEntity(String entityName, String buySellFlag, Float agreedFx, String currencyCode, String instructionDate, String settlementDate, Integer units, Float price) {
        this.entityName = entityName;
        this.buySellFlag = buySellFlag;
        this.agreedFx = agreedFx;
        this.currencyCode = currencyCode;
        this.instructionDate = instructionDate;
        this.settlementDate = settlementDate;
        this.units = units;
        this.price = price;
    }


    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Float getAgreedFx() {
        return agreedFx;
    }

    public void setAgreedFx(Float agreedFx) {
        this.agreedFx = agreedFx;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTotalPrice(){
        this.totalPrice = this.units * this.price * this.agreedFx;
        return this.totalPrice;
    }

    public int compareTo(Object o) {
        TradeEntity tradeEntity = (TradeEntity)o;
        return this.getTotalPrice().compareTo(tradeEntity.getTotalPrice());
    }

    public String getInstructionDate() {
        return instructionDate;
    }

    public void setInstructionDate(String instructionDate) {
        this.instructionDate = instructionDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getBuySellFlag() {
        return buySellFlag;
    }

    public void setBuySellFlag(String buySellFlag) {
        this.buySellFlag = buySellFlag;
    }
}
