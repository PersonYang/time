package com.apin.po;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ApinClient {

    private Integer id;
    private String phone;
    private String nickName;
    private String headPic;
    private String invitationCode;
    private Integer isMerchant;
    private Date createTime;
    private String developerId;
    private String maintenancerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Integer getIsMerchant() {
        return isMerchant;
    }

    public void setIsMerchant(Integer isMerchant) {
        this.isMerchant = isMerchant;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getMaintenancerId() {
        return maintenancerId;
    }

    public void setMaintenancerId(String maintenancerId) {
        this.maintenancerId = maintenancerId;
    }
}
