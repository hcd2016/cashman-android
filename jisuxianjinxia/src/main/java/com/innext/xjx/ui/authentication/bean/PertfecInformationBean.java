package com.innext.xjx.ui.authentication.bean;

import java.util.List;

/**
 * 作者：${黑哥} on 2017/2/14 0014 15:16
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PertfecInformationBean {
    private int real_verify_status;//
    private int contacts_status;//认证状态
    private List<AuthenticationinformationBean> list;

    public int getReal_verify_status() {
        return real_verify_status;
    }

    public void setReal_verify_status(int real_verify_status) {
        this.real_verify_status = real_verify_status;
    }

    public int getContacts_status() {
        return contacts_status;
    }

    public void setContacts_status(int contacts_status) {
        this.contacts_status = contacts_status;
    }

    public List<AuthenticationinformationBean> getList() {
        return list;
    }

    public void setList(List<AuthenticationinformationBean> list) {
        this.list = list;
    }
}
