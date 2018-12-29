package com.qixiu.lejia.beans.resp;

import com.google.gson.annotations.SerializedName;
import com.qixiu.lejia.beans.UserProfile;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     我的响应数据
 * </pre>
 */
public class MeResp {

    @SerializedName("sign")
    private String signedCount;

    @SerializedName("reserve")
    private String appointmentCount;

    @SerializedName("paymentstatus")
    private RentPaymentStatus rentPaymentStatus;

    @SerializedName("hydropowerstatus")
    private HydroelectricPaymentStatus hydroelectricPaymentStatus;


    @SerializedName("list")
    private UserProfile profile;


    public static class RentPaymentStatus{
        @SerializedName("pa_type")
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class HydroelectricPaymentStatus{
        @SerializedName("hy_pay_state")
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }


    public String getSignedCount() {
        return signedCount;
    }

    public void setSignedCount(String signedCount) {
        this.signedCount = signedCount;
    }

    public String getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(String appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public RentPaymentStatus getRentPaymentStatus() {
        return rentPaymentStatus;
    }

    public void setRentPaymentStatus(RentPaymentStatus rentPaymentStatus) {
        this.rentPaymentStatus = rentPaymentStatus;
    }

    public HydroelectricPaymentStatus getHydroelectricPaymentStatus() {
        return hydroelectricPaymentStatus;
    }

    public void setHydroelectricPaymentStatus(HydroelectricPaymentStatus hydroelectricPaymentStatus) {
        this.hydroelectricPaymentStatus = hydroelectricPaymentStatus;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

}
