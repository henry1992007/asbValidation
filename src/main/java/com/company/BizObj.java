package com.company;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/11/4.
 */
public class BizObj {

    private BigDecimal test = BigDecimal.valueOf(6.1);
    private float field2 = 6.2f;
    private boolean flag = false;
    private String string = "aaa";
    private BizObj2 inner = new BizObj2();
    private Map<String, Double> map = new HashMap<String, Double>();

    public BizObj() {
        map.put("key1", 23.0);
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getInt() {
        return 5;
    }

    public BigDecimal getTest() {
        return test;
    }

    public void setTest(BigDecimal test) {
        this.test = test;
    }

    public float getField2() {
        return field2;
    }

    public void setField2(float field2) {
        this.field2 = field2;
    }

    public BizObj2 getInner() {
        return inner;
    }

    public void setInner(BizObj2 inner) {
        this.inner = inner;
    }
}
