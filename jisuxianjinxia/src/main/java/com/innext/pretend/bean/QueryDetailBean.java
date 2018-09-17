package com.innext.pretend.bean;

import java.io.Serializable;
import java.util.List;

public class QueryDetailBean implements Serializable {
    /**
     * error : N
     * message : {"id_age":"34","final_score":"125","address_detect":"{\"bank_card_address\":null,\"true_ip_address\":null,\"mobile_address_city\":\"深圳市\",\"id_card_city\":\"萍乡市\",\"mobile_address\":\"广东省深圳市\",\"wifi_address\":null,\"id_card_province\":\"江西省\",\"cell_address\":null,\"mobile_address_province\":\"广东省\",\"id_card_address\":\"江西省萍乡市湘东区\"}","risk_item":[{"score":"30","risk_name":"与近180天相比30天内借款人新增的多平台个数占比异常_02"},{"score":"25","risk_name":"与近180天相比60天内借款人新增的多平台个数占比异常_02"},{"score":"20","risk_name":"与近180天相比14天内借款人新增的多平台个数占比_02"},{"score":"15","risk_name":"与近180天相比90天内借款人新增的多平台个数占比异常_02"},{"score":"10","risk_name":"与近180天相比7天内借款人新增的多平台个数占比异常_02"},{"score":"10","risk_name":"8段_56天内每7天为单位的借款人新增的多平台个数占比标准差_180天基础异常"},{"score":"15","risk_name":"6段_180天内每30天为单位的借款人新增的多平台个数占比标准差_180天基础异常_01"}]}
     */

    public String error;
    public MessageBean message;

    public static class MessageBean implements Serializable{
        /**
         * id_age : 34
         * final_score : 125
         * address_detect : {"bank_card_address":null,"true_ip_address":null,"mobile_address_city":"深圳市","id_card_city":"萍乡市","mobile_address":"广东省深圳市","wifi_address":null,"id_card_province":"江西省","cell_address":null,"mobile_address_province":"广东省","id_card_address":"江西省萍乡市湘东区"}
         * risk_item : [{"score":"30","risk_name":"与近180天相比30天内借款人新增的多平台个数占比异常_02"},{"score":"25","risk_name":"与近180天相比60天内借款人新增的多平台个数占比异常_02"},{"score":"20","risk_name":"与近180天相比14天内借款人新增的多平台个数占比_02"},{"score":"15","risk_name":"与近180天相比90天内借款人新增的多平台个数占比异常_02"},{"score":"10","risk_name":"与近180天相比7天内借款人新增的多平台个数占比异常_02"},{"score":"10","risk_name":"8段_56天内每7天为单位的借款人新增的多平台个数占比标准差_180天基础异常"},{"score":"15","risk_name":"6段_180天内每30天为单位的借款人新增的多平台个数占比标准差_180天基础异常_01"}]
         */

        public String id_age;
        public String final_score;
        public String address_detect;
        public List<QueryResultBean.QueryDetailBean.MessageBean.RiskItemBean> risk_item;

        public static class RiskItemBean implements Serializable{
            /**
             * score : 30
             * risk_name : 与近180天相比30天内借款人新增的多平台个数占比异常_02
             */

            public String score;
            public String risk_name;
        }
    }
}