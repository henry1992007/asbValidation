package com.company.test;

import java.io.Serializable;

public class ViewTimePeriodDTO implements Serializable, Cloneable {
    private String timePeriod; //早市 午时  晚市 等
    private String beginTime;//开始时间的小时
    private String endTime; //结束时间的小时
    private Integer beginDay; // 周几
    private Integer endDay; // 周几

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(Integer beginDay) {
        this.beginDay = beginDay;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return "ViewTimePeriodDTO{" +
                "timePeriod='" + timePeriod + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", beginDay=" + beginDay +
                ", endDay=" + endDay +
                '}';
    }

    @Override
    public ViewTimePeriodDTO clone() {
        try {
            return (ViewTimePeriodDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
