package com.innext.xjx.ui.authentication.bean;

import java.util.List;

/**
 * 作者：${黑哥} on 2017/2/15 0015 18:04
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PersonalInformationBean {
    private List<EnterTimeAndSalaryBean> live_time_type_all;
    private List<EnterTimeAndSalaryBean> degrees_all;
    private List<EnterTimeAndSalaryBean> marriage_all;
    private int id;
    private String name;
    private String id_number;
    private String degrees;
    private String marriage;
    private String address;
    private String address_distinct;
    private String live_period;
    private int real_verify_status;//认证状态
    private String face_recognition_picture;
    private String id_number_z_picture;
    private String id_number_f_picture;

    public List<EnterTimeAndSalaryBean> getLive_time_type_all() {
        return live_time_type_all;
    }

    public void setLive_time_type_all(List<EnterTimeAndSalaryBean> live_time_type_all) {
        this.live_time_type_all = live_time_type_all;
    }

    public List<EnterTimeAndSalaryBean> getDegrees_all() {
        return degrees_all;
    }

    public void setDegrees_all(List<EnterTimeAndSalaryBean> degrees_all) {
        this.degrees_all = degrees_all;
    }

    public List<EnterTimeAndSalaryBean> getMarriage_all() {
        return marriage_all;
    }

    public void setMarriage_all(List<EnterTimeAndSalaryBean> marriage_all) {
        this.marriage_all = marriage_all;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_distinct() {
        return address_distinct;
    }

    public void setAddress_distinct(String address_distinct) {
        this.address_distinct = address_distinct;
    }

    public String getLive_period() {
        return live_period;
    }

    public void setLive_period(String live_period) {
        this.live_period = live_period;
    }

    public int getReal_verify_status() {
        return real_verify_status;
    }

    public void setReal_verify_status(int real_verify_status) {
        this.real_verify_status = real_verify_status;
    }

    public String getFace_recognition_picture() {
        return face_recognition_picture;
    }

    public void setFace_recognition_picture(String face_recognition_picture) {
        this.face_recognition_picture = face_recognition_picture;
    }

    public String getId_number_z_picture() {
        return id_number_z_picture;
    }

    public void setId_number_z_picture(String id_number_z_picture) {
        this.id_number_z_picture = id_number_z_picture;
    }

    public String getId_number_f_picture() {
        return id_number_f_picture;
    }

    public void setId_number_f_picture(String id_number_f_picture) {
        this.id_number_f_picture = id_number_f_picture;
    }
}
