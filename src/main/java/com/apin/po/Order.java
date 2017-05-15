package com.apin.po;

import com.apin.utils.DateJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Order {

    private Integer id;
    private Long orderNo;
    private Long journeyId;
    private Integer userId;
    private Integer merchantId;
    private BigDecimal unitPrice;
    private Integer passengerNum;
    private BigDecimal insurancePrice;
    private Integer insuranceNum;
    private BigDecimal totalPrice;
    private BigDecimal actualPayCash;
    private Integer payModel;
    private String currency;
    private Integer status;
    private Integer statusByApp;
    private String payVoucher;
    private String account;
    private String payer;
    private Date payTime;
    private Integer actualPassengerNum;
    private BigDecimal restPrice;
    private Integer priceId;
    private Integer restPayModel;
    private String restCurrency;
    private Integer restStatusByApp;
    private String restPayVoucher;
    private String restAccount;
    private String restPayer;
    private Date restPayTime;
    private Date createTime;
    private Date updateTime;
    private Long routeNo;
    private Integer flag;
    private String travelAgencyId;
    private String supplierId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(Long journeyId) {
        this.journeyId = journeyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(Integer passengerNum) {
        this.passengerNum = passengerNum;
    }

    public BigDecimal getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(BigDecimal insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public Integer getInsuranceNumber() {
        return insuranceNum;
    }

    public void setInsuranceNumber(Integer insuranceNumber) {
        this.insuranceNum = insuranceNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getActualPayCash() {
        return actualPayCash;
    }

    public void setActualPayCash(BigDecimal actualPayCash) {
        this.actualPayCash = actualPayCash;
    }

    public Integer getPayModel() {
        return payModel;
    }

    public void setPayModel(Integer payModel) {
        this.payModel = payModel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusByApp() {
        return statusByApp;
    }

    public void setStatusByApp(Integer statusByApp) {
        this.statusByApp = statusByApp;
    }

    public String getPayVoucher() {
        return payVoucher;
    }

    public void setPayVoucher(String payVoucher) {
        this.payVoucher = payVoucher;
    }

    @JsonSerialize(using= DateJsonSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using= DateJsonSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @JsonSerialize(using= DateJsonSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Integer getActualPassengerNum() {
        return actualPassengerNum;
    }

    public void setActualPassengerNum(Integer actualPassengerNum) {
        this.actualPassengerNum = actualPassengerNum;
    }

    public BigDecimal getRestPrice() {
        return restPrice;
    }

    public void setRestPrice(BigDecimal restPrice) {
        this.restPrice = restPrice;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public Integer getRestPayModel() {
        return restPayModel;
    }

    public void setRestPayModel(Integer restPayModel) {
        this.restPayModel = restPayModel;
    }

    public String getRestCurrency() {
        return restCurrency;
    }

    public void setRestCurrency(String restCurrency) {
        this.restCurrency = restCurrency;
    }

    public Integer getRestStatusByApp() {
        return restStatusByApp;
    }

    public void setRestStatusByApp(Integer restStatusByApp) {
        this.restStatusByApp = restStatusByApp;
    }

    public String getRestPayVoucher() {
        return restPayVoucher;
    }

    public void setRestPayVoucher(String restPayVoucher) {
        this.restPayVoucher = restPayVoucher;
    }

    public String getRestAccount() {
        return restAccount;
    }

    public void setRestAccount(String restAccount) {
        this.restAccount = restAccount;
    }

    public String getRestPayer() {
        return restPayer;
    }

    public void setRestPayer(String restPayer) {
        this.restPayer = restPayer;
    }

    public Date getRestPayTime() {
        return restPayTime;
    }

    public void setRestPayTime(Date restPayTime) {
        this.restPayTime = restPayTime;
    }

    public Long getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(Long routeNo) {
        this.routeNo = routeNo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getTravelAgencyId() {
        return travelAgencyId;
    }

    public void setTravelAgencyId(String travelAgencyId) {
        this.travelAgencyId = travelAgencyId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
