package com.company.test;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by henry on 15/12/10.
 */
public class ExceptTimePeriodDTO implements Serializable {
    private Date dayValue;

    public ExceptTimePeriodDTO(Date dayValue) {
        this.dayValue = dayValue;
    }

    public Date getDayValue() {
        return dayValue;
    }

    public void setDayValue(Date dayValue) {
        this.dayValue = dayValue;
    }
}

