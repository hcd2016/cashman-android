package com.innext.xjx.ui.repayment.bean;

/**
 * author：wangzuzhen on 2016/9/21 0021 13:18
 */
public class RepaymentListBean {

    private String debt;//实际欠款金额
    private String principal;//借款本金
    private String counter_fee;//服务费
    private String receipts;//实际到账金额
    private String interests;//利息
    private String late_fee;//逾期还款滞纳金
    private String plan_fee_time;//应还日期
    private String text_tip;//文本提示
    private String url;//跳转h5页面url地址

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCounter_fee() {
        return counter_fee;
    }

    public void setCounter_fee(String counter_fee) {
        this.counter_fee = counter_fee;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getPlan_fee_time() {
        return plan_fee_time;
    }

    public void setPlan_fee_time(String plan_fee_time) {
        this.plan_fee_time = plan_fee_time;
    }

    public String getText_tip() {
        return text_tip;
    }

    public void setText_tip(String text_tip) {
        this.text_tip = text_tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
