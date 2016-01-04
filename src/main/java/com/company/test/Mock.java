package com.company.test;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        couponOfferDTO.setMaxInventory(4);
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

    public static CouponOfferDTO[] mocks1() {
        CouponOfferDTO couponOfferDTO = mock();
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setRedeemType(3);
        couponOfferDTO.setDiscount(discountDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("salePrice", new BigDecimal("8.8"));
        map.put("retailPrice", new BigDecimal("18.8"));
        couponOfferDTO.setBusinessAttribute(map);

        CouponOfferDTO couponOfferDTO2 = mock();
        DiscountDTO discountDTO2 = new DiscountDTO();
        discountDTO2.setRedeemType(3);
        couponOfferDTO2.setDiscount(discountDTO2);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("salePrice", new BigDecimal("5.66"));
        map2.put("retailPrice", new BigDecimal("38.8"));
        couponOfferDTO2.setBusinessAttribute(map2);

        CouponOfferDTO couponOfferDTO3 = mock();
        DiscountDTO discountDTO3 = new DiscountDTO();
        discountDTO3.setRedeemType(2);
        couponOfferDTO3.setDiscount(discountDTO3);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("salePrice", new BigDecimal("56.8"));
        map3.put("retailPrice", new BigDecimal("7.4"));
        couponOfferDTO3.setBusinessAttribute(map3);

        return new CouponOfferDTO[]{couponOfferDTO, couponOfferDTO2, couponOfferDTO3};
    }

    public static CouponOfferDTO[] mocks() {
        CouponOfferDTO couponOfferDTO = mock();
        couponOfferDTO.setMaxInventory(5);
        couponOfferDTO.getViewTimePeriod().setTimePeriod("test");
        couponOfferDTO.getViewTimePeriod().setTimePeriod("qita");

        CouponOfferDTO couponOfferDTO1 = new CouponOfferDTO();
        couponOfferDTO1.setMaxInventory(56);
        ViewTimePeriodDTO viewTimePeriodDTO1 = new ViewTimePeriodDTO();
        viewTimePeriodDTO1.setTimePeriod("wushi");
        couponOfferDTO1.setViewTimePeriod(viewTimePeriodDTO1);

        CouponOfferDTO couponOfferDTO2 = new CouponOfferDTO();
        couponOfferDTO2.setMaxInventory(44);
        ViewTimePeriodDTO viewTimePeriodDTO2 = new ViewTimePeriodDTO();
        viewTimePeriodDTO2.setTimePeriod("wushi");
        couponOfferDTO2.setViewTimePeriod(viewTimePeriodDTO2);

        CouponOfferDTO couponOfferDTO3 = new CouponOfferDTO();
        couponOfferDTO3.setMaxInventory(78);
        ViewTimePeriodDTO viewTimePeriodDTO3 = new ViewTimePeriodDTO();
        viewTimePeriodDTO3.setTimePeriod("wanshi");
        couponOfferDTO3.setViewTimePeriod(viewTimePeriodDTO3);

        CouponOfferDTO couponOfferDTO4 = new CouponOfferDTO();
        couponOfferDTO4.setMaxInventory(1234);
        ViewTimePeriodDTO viewTimePeriodDTO4 = new ViewTimePeriodDTO();
        viewTimePeriodDTO4.setTimePeriod("wushi");
        couponOfferDTO4.setViewTimePeriod(viewTimePeriodDTO4);

        return new CouponOfferDTO[]{couponOfferDTO, couponOfferDTO1, couponOfferDTO2, couponOfferDTO3, couponOfferDTO4};
    }
}
