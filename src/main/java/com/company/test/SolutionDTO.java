package com.company.test;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/12/10.
 */
public class SolutionDTO implements Serializable {
    private int bizTypeId;
    private int solutionId;
    private Integer customerId;
    private Integer contractId;
    private Integer processId;
    private Integer approvalStatus;
    private Integer publishStatus;
    private Date effectiveBeginDate;
    private Date effectiveEndDate;
    private Integer settleLevel; // 结算方式
    private Integer payType; // 收费方式
    private Integer cityId; // 销售所在城市
    private Integer ownerId; // 责任人
    private Integer createBy;
    private List<Integer> shopIds;
    private Map<String, String> bizValues;


    public int getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(int bizTypeId) {
        this.bizTypeId = bizTypeId;
    }

    /**
     * @return the solutionId
     */
    public int getSolutionId() {
        return solutionId;
    }

    /**
     * @param solutionId the solutionId to set
     */
    public void setSolutionId(int solutionId) {
        this.solutionId = solutionId;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the processId
     */
    public Integer getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    /**
     * @return the approvalStatus
     */
    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * @param approvalStatus the approvalStatus to set
     */
    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * @return the publishStatus
     */
    public Integer getPublishStatus() {
        return publishStatus;
    }

    /**
     * @param publishStatus the publishStatus to set
     */
    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    /**
     * @return the effectiveBeginDate
     */
    public Date getEffectiveBeginDate() {
        return effectiveBeginDate;
    }

    /**
     * @param effectiveBeginDate the effectiveBeginDate to set
     */
    public void setEffectiveBeginDate(Date effectiveBeginDate) {
        this.effectiveBeginDate = effectiveBeginDate;
    }

    /**
     * @return the effectiveEndDate
     */
    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    /**
     * @param effectiveEndDate the effectiveEndDate to set
     */
    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    /**
     * @return the payType
     */
    public Integer getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * @return the ownerId
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return the shopIds
     */
    public List<Integer> getShopIds() {
        return shopIds;
    }

    /**
     * @param shopIds the shopIds to set
     */
    public void setShopIds(List<Integer> shopIds) {
        this.shopIds = shopIds;
    }

    /**
     * @return the bizValues
     */
    public Map<String, String> getBizValues() {
        return bizValues;
    }

    /**
     * @param bizValues the bizValues to set
     */
    public void setBizValues(Map<String, String> bizValues) {
        this.bizValues = bizValues;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getSettleLevel() {
        return settleLevel;
    }

    public void setSettleLevel(Integer settleLevel) {
        this.settleLevel = settleLevel;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }
}

