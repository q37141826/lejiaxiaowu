package com.qixiu.lejia.core.me.sign;

import android.os.Parcel;
import android.os.Parcelable;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

public class PayListBean extends BaseBean<List<PayListBean.OBean>> {
        public static class OBean implements Parcelable {



            /**
             * id : 10
             * equipment_type : elemeter
             * equipment_name : 主卧
             * equipment_uuid : 0890b07de45ca66ca52095510fef0d27
             * room_id : 6569
             * store_name : 店铺名称
             * ro_number : 房号
             */

            private String id;
            private String equipment_type;
            private String equipment_name;
            private String equipment_uuid;
            private String room_id;
            private String store_name;
            private String ro_number;
            private String yd_home_id;
            private String store_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEquipment_type() {
                return equipment_type;
            }

            public void setEquipment_type(String equipment_type) {
                this.equipment_type = equipment_type;
            }

            public String getEquipment_name() {
                return equipment_name;
            }

            public void setEquipment_name(String equipment_name) {
                this.equipment_name = equipment_name;
            }

            public String getEquipment_uuid() {
                return equipment_uuid;
            }

            public void setEquipment_uuid(String equipment_uuid) {
                this.equipment_uuid = equipment_uuid;
            }

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getRo_number() {
                return ro_number;
            }

            public void setRo_number(String ro_number) {
                this.ro_number = ro_number;
            }

            public String getYd_home_id() {
                return yd_home_id;
            }

            public void setYd_home_id(String yd_home_id) {
                this.yd_home_id = yd_home_id;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.equipment_type);
                dest.writeString(this.equipment_name);
                dest.writeString(this.equipment_uuid);
                dest.writeString(this.room_id);
                dest.writeString(this.store_name);
                dest.writeString(this.ro_number);
                dest.writeString(this.yd_home_id);
                dest.writeString(this.store_id);
            }

            public OBean() {
            }

            protected OBean(Parcel in) {
                this.id = in.readString();
                this.equipment_type = in.readString();
                this.equipment_name = in.readString();
                this.equipment_uuid = in.readString();
                this.room_id = in.readString();
                this.store_name = in.readString();
                this.ro_number = in.readString();
                this.yd_home_id = in.readString();
                this.store_id = in.readString();
            }

            public static final Parcelable.Creator<OBean> CREATOR = new Parcelable.Creator<OBean>() {
                @Override
                public OBean createFromParcel(Parcel source) {
                    return new OBean(source);
                }

                @Override
                public OBean[] newArray(int size) {
                    return new OBean[size];
                }
            };
        }
    }