package com.company.test;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by henry on 15/12/10.
 */
public class DiscountDTO implements Serializable {
    private int redeemType ;//优惠类型；1表示折扣；2表示赠送；3表示满减；4表示送惠；6表示定额
    private String redeemDescription;
    private BigDecimal full;//满金额
    private BigDecimal cut;//减金额

    public int getRedeemType() {
        return redeemType;
    }

    public void setRedeemType(int redeemType) {
        this.redeemType = redeemType;
    }

    public String getRedeemDescription() {
        return redeemDescription;
    }

    public void setRedeemDescription(String redeemDescription) {
        this.redeemDescription = redeemDescription;
    }

    public BigDecimal getFull() {
        return full;
    }

    public void setFull(BigDecimal full) {
        this.full = full;
    }

    public BigDecimal getCut() {
        return cut;
    }

    public void setCut(BigDecimal cut) {
        this.cut = cut;
    }

}
