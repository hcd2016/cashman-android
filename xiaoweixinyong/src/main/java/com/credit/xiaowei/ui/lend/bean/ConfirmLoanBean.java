package com.credit.xiaowei.ui.lend.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 2016/9/22.
 */
public class ConfirmLoanBean implements Parcelable {
    private String verify_loan_pass;
    private String bank_name;
    private String protocol_msg;
    private String real_pay_pwd_status;
    private String true_money;
    private String money;
    private String protocol_url;
    private String card_no;
    private String tips;
    private String counter_fee;
    private String period;
    private String withholding_url;  //扣款协议
    private String platformservice_url;//扣款协议

    public String getVerify_loan_pass() {
        return verify_loan_pass;
    }

    public void setVerify_loan_pass(String verify_loan_pass) {
        this.verify_loan_pass = verify_loan_pass;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getProtocol_msg() {
        return protocol_msg;
    }

    public void setProtocol_msg(String protocol_msg) {
        this.protocol_msg = protocol_msg;
    }

    public String getReal_pay_pwd_status() {
        return real_pay_pwd_status;
    }

    public void setReal_pay_pwd_status(String real_pay_pwd_status) {
        this.real_pay_pwd_status = real_pay_pwd_status;
    }

    public String getTrue_money() {
        return true_money;
    }

    public void setTrue_money(String true_money) {
        this.true_money = true_money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getProtocol_url() {
        return protocol_url;
    }

    public void setProtocol_url(String protocol_url) {
        this.protocol_url = protocol_url;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getCounter_fee() {
        return counter_fee;
    }

    public void setCounter_fee(String counter_fee) {
        this.counter_fee = counter_fee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWithholding_url() {
        return withholding_url;
    }

    public void setWithholding_url(String withholding_url) {
        this.withholding_url = withholding_url;
    }

    public String getPlatformservice_url() {
        return platformservice_url;
    }

    public void setPlatformservice_url(String platformservice_url) {
        this.platformservice_url = platformservice_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.verify_loan_pass);
        dest.writeString(this.bank_name);
        dest.writeString(this.protocol_msg);
        dest.writeString(this.real_pay_pwd_status);
        dest.writeString(this.true_money);
        dest.writeString(this.money);
        dest.writeString(this.protocol_url);
        dest.writeString(this.card_no);
        dest.writeString(this.tips);
        dest.writeString(this.counter_fee);
        dest.writeString(this.period);
        dest.writeString(this.withholding_url);
        dest.writeString(this.platformservice_url);
    }

    public ConfirmLoanBean() {
    }

    protected ConfirmLoanBean(Parcel in) {
        this.verify_loan_pass = in.readString();
        this.bank_name = in.readString();
        this.protocol_msg = in.readString();
        this.real_pay_pwd_status = in.readString();
        this.true_money = in.readString();
        this.money = in.readString();
        this.protocol_url = in.readString();
        this.card_no = in.readString();
        this.tips = in.readString();
        this.counter_fee = in.readString();
        this.period = in.readString();
        this.withholding_url = in.readString();
        this.platformservice_url = in.readString();
    }

    public static final Parcelable.Creator<ConfirmLoanBean> CREATOR = new Parcelable.Creator<ConfirmLoanBean>() {
        @Override
        public ConfirmLoanBean createFromParcel(Parcel source) {
            return new ConfirmLoanBean(source);
        }

        @Override
        public ConfirmLoanBean[] newArray(int size) {
            return new ConfirmLoanBean[size];
        }
    };
}
