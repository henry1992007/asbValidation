package com.company.test;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/12/11.
 */
public class Mock {
    public static CouponOfferDTO mock() {
        CouponOfferDTO couponOfferDTO = new CouponOfferDTO();
        couponOfferDTO.setTitle("你个大西瓜");
        couponOfferDTO.setMaxInventory(890);
        couponOfferDTO.setBeginDate(new Date());
        couponOfferDTO.setEndDate(new Date());

        ViewTimePeriodDTO viewTimePeriodDTO = new ViewTimePeriodDTO();
        viewTimePeriodDTO.setBeginDay(5);
        viewTimePeriodDTO.setEndDay(6);
        viewTimePeriodDTO.setBeginTime("08:00");
        viewTimePeriodDTO.setEndTime("12:00");
        viewTimePeriodDTO.setTimePeriod("???");

        couponOfferDTO.setViewTimePeriod(viewTimePeriodDTO);
        couponOfferDTO.setShopIds(Lists.newArrayList(23, 545465, 2342, 122));
        couponOfferDTO.setEffectiveInHolidays(true);
        couponOfferDTO.setDetailDescs(Lists.newArrayList("435", "sdfsa", "zx"));
        couponOfferDTO.setExceptTimePeriods(Lists.newArrayList(new ExceptTimePeriodDTO(new Date()), new ExceptTimePeriodDTO(new Date())));

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setCut(new BigDecimal(6.5));
        discountDTO.setFull(new BigDecimal(9.00));
        discountDTO.setRedeemType(5);
        couponOfferDTO.setDiscount(discountDTO);

        couponOfferDTO.setTicketIssueThreshold(new BigDecimal(-808.80));

        Map<String, Object> map = new HashMap<>();
        map.put("salePrice", new BigDecimal(89));
        map.put("retailPrice", new BigDecimal(77));
        map.put("quotaType", 2);
        couponOfferDTO.setBusinessAttribute(map);
        return couponOfferDTO;
    }
}
