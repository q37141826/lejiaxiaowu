package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/10
 */
public class QualificationInfo {

    @SerializedName("ud_industry")
    private String profession;

    @SerializedName("ud_company_name")
    private String company;

    @SerializedName("ud_address")
    private String companyAddress;

    @SerializedName("ud_urgent_name")
    private String emergencyContactNmae;

    @SerializedName("ud_urgent_tel")
    private String emergencyContactPhone;

    @SerializedName("ud_relation")
    private String relationship;

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmergencyContactNmae() {
        return emergencyContactNmae;
    }

    public void setEmergencyContactNmae(String emergencyContactNmae) {
        this.emergencyContactNmae = emergencyContactNmae;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

}
