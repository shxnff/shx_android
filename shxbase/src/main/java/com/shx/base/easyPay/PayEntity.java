package com.shx.base.easyPay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 功能介绍：支付的数据
 */

public class PayEntity implements Parcelable {

    //必须参数
    @EasyPay.PayChannel
    transient int channel;
    //这下面是微信的参数
    public String appId;
    public String partnerId;//商户id
    public String prepayId;//预支付id
    public String sign;//签名
    public String nonceStr;//临时数据
    public String timeStamp;//时间戳
    public String extData;//扩展数据
    public String packageValue;//Sign=WXPay

    //支付宝与银联的
    public String orderInfo;//订单信息，银联的TN号
    /*****************************************************************
     * mode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    public transient String mode = "01";


    public PayEntity() {
    }

    private PayEntity(int channel) {
        this.channel = channel;
    }

    public static PayEntity get(@EasyPay.PayChannel int channel) {
        return new PayEntity(channel);
    }

    public PayEntity setChannel(@EasyPay.PayChannel int channel) {
        this.channel = channel;
        return this;
    }

    public PayEntity setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public PayEntity setExtData(String extData) {
        this.extData = extData;
        return this;
    }

    public PayEntity setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public PayEntity setPrepayId(String prepayId) {
        this.prepayId = prepayId;
        return this;
    }

    public PayEntity setPartnerId(String partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    public PayEntity setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public PayEntity setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public PayEntity setPackageValue(String packageValue) {
        this.packageValue = packageValue;
        return this;
    }

    //支付宝与银联的
    public PayEntity setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
        return this;
    }

    //银联的测试模式
    public PayEntity setUpDebug(boolean isDebug) {
        this.mode = isDebug ? "01" : "00";
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.channel);
        dest.writeString(this.appId);
        dest.writeString(this.partnerId);
        dest.writeString(this.prepayId);
        dest.writeString(this.sign);
        dest.writeString(this.nonceStr);
        dest.writeString(this.timeStamp);
        dest.writeString(this.extData);
        dest.writeString(this.packageValue);
        dest.writeString(this.orderInfo);
        dest.writeString(this.mode);
    }

    protected PayEntity(Parcel in) {
        this.channel = in.readInt();
        this.appId = in.readString();
        this.partnerId = in.readString();
        this.prepayId = in.readString();
        this.sign = in.readString();
        this.nonceStr = in.readString();
        this.timeStamp = in.readString();
        this.extData = in.readString();
        this.packageValue = in.readString();
        this.orderInfo = in.readString();
        this.mode = in.readString();
    }

    public static final Parcelable.Creator<PayEntity> CREATOR = new Parcelable.Creator<PayEntity>() {
        @Override
        public PayEntity createFromParcel(Parcel source) {
            return new PayEntity(source);
        }

        @Override
        public PayEntity[] newArray(int size) {
            return new PayEntity[size];
        }
    };
}
