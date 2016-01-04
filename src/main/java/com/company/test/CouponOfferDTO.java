package com.company.test;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CouponOfferDTO implements Serializable, Cloneable {
    private String title; //优惠标题
    private Integer maxInventory;//最大库存
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

    @Override
    public CouponOfferDTO clone() {
        try {
            return (CouponOfferDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
