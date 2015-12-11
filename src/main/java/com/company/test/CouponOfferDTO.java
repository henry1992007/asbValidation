package com.company.test;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CouponOfferDTO implements Serializable{
    private String title; //优惠标题
    private int maxInventory;//最大库存
    private Date beginDate;//有效期开始时间
    private Date endDate;//有效期结束时间
    private ViewTimePeriodDTO viewTimePeriod;//时间段
    private List<Integer> shopIds;//优惠门店
    private boolean effectiveInHolidays;//节假日是否可用
    private List<String> detailDescs;//说明
    private List<ExceptTimePeriodDTO> exceptTimePeriods;//哪些日期除外
    private DiscountDTO discount;//优惠信息
    private BigDecimal ticketIssueThreshold;// 发券的金额 比如满200发券
    private Map<String, Object> businessAttribute;

    public ViewTimePeriodDTO getViewTimePeriod(){
        return viewTimePeriod;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(int maxInventory) {
        this.maxInventory = maxInventory;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public void setViewTimePeriod(ViewTimePeriodDTO viewTimePeriodDTO) {
        this.viewTimePeriod = viewTimePeriodDTO;
    }

    public List<Integer> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Integer> shopIds) {
        this.shopIds = shopIds;
    }

    public boolean isEffectiveInHolidays() {
        return effectiveInHolidays;
    }

    public void setEffectiveInHolidays(boolean effectiveInHolidays) {
        this.effectiveInHolidays = effectiveInHolidays;
    }

    public List<String> getDetailDescs() {
        return detailDescs;
    }

    public void setDetailDescs(List<String> detailDescs) {
        this.detailDescs = detailDescs;
    }

    public List<ExceptTimePeriodDTO> getExceptTimePeriods() {
        return exceptTimePeriods;
    }

    public void setExceptTimePeriods(List<ExceptTimePeriodDTO> exceptTimePeriods) {
        this.exceptTimePeriods = exceptTimePeriods;
    }

    public DiscountDTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDTO discount) {
        this.discount = discount;
    }

    public BigDecimal getTicketIssueThreshold() {
        return ticketIssueThreshold;
    }

    public void setTicketIssueThreshold(BigDecimal ticketIssueThreshold) {
        this.ticketIssueThreshold = ticketIssueThreshold;
    }

    public Map<String, Object> getBusinessAttribute() {
        return businessAttribute;
    }

    public void setBusinessAttribute(Map<String, Object> businessAttribute) {
        this.businessAttribute = businessAttribute;
    }

}
