package com.credit.xiaowei.ui.authentication.bean;

import java.util.List;

public class OtherProductBean {
    /**
     * currentPage : 1
     * items : [{"display":"N","icon_url":"/files/2019-03-01/674ae34e-e47e-444b-9819-643da2acd739.jpg","id":7,"loan_cycle":"30DAY_UP","loan_max":1100,"loan_min":11,"loan_range":11,"loan_range_unit":"DAY_INNER","loan_rate":11,"loan_rate_unit":"DAY","online":"N","operator":"admin","order_factor":4,"product_desc":"11秒放款|日息 11%|可借11天内","product_memo":"11","product_name":"11","product_url":"www.baidu.com","release_loan_time":11,"release_loan_time_unit":"SEC","update_date":1550656272000},{"display":"N","icon_url":"/files/2019-03-01/674ae34e-e47e-444b-9819-643da2acd739.jpg","id":2,"loan_cycle":"30DAY_UP","loan_max":2222,"loan_min":111,"loan_range":111,"loan_range_unit":"DAY_INNER","loan_rate":1111,"loan_rate_unit":"DAY","online":"N","operator":"admin","order_factor":2,"product_desc":"1秒放款|日息 1111%|可借111天内","product_memo":"fff","product_name":"测试产品","product_url":"www.baidu.com","release_loan_time":1,"release_loan_time_unit":"SEC","update_date":1550656207000},{"display":"N","icon_url":"/files/2019-03-01/674ae34e-e47e-444b-9819-643da2acd739.jpg","id":1,"loan_cycle":"30DAY_UP","loan_max":222,"loan_min":33,"loan_range":11,"loan_range_unit":"DAY_INNER","loan_rate":1,"loan_rate_unit":"DAY","online":"N","operator":"admin","order_factor":1,"product_desc":"1天放款|日息 1%|可借11天内","product_memo":"ff","product_url":"www.baidu.com","release_loan_time":1,"release_loan_time_unit":"DAY","update_date":1550656196000}]
     * maps : {}
     * pageSize : 3
     * startResult : 0
     * totalPageNum : 3
     * totalPages : 3
     * totalResultSize : 7
     */

    public int currentPage;
    public MapsBean maps;
    public int pageSize;
    public int startResult;
    public int totalPageNum;
    public int totalPages;
    public int totalResultSize;
    public List<ItemsBean> items;

    public static class MapsBean {
    }

    public static class ItemsBean {
        /**
         * display : N
         * icon_url : /files/2019-03-01/674ae34e-e47e-444b-9819-643da2acd739.jpg
         * id : 7
         * loan_cycle : 30DAY_UP
         * loan_max : 1100
         * loan_min : 11
         * loan_range : 11
         * loan_range_unit : DAY_INNER
         * loan_rate : 11
         * loan_rate_unit : DAY
         * online : N
         * operator : admin
         * order_factor : 4
         * product_desc : 11秒放款|日息 11%|可借11天内
         * product_memo : 11
         * product_name : 11
         * product_url : www.baidu.com
         * release_loan_time : 11
         * release_loan_time_unit : SEC
         * update_date : 1550656272000
         */

        public String display;
        public String icon_url;
        public int id;
        public String loan_cycle;
        public int loan_max;
        public int loan_min;
        public int loan_range;
        public String loan_range_unit;
        public int loan_rate;
        public String loan_rate_unit;
        public String online;
        public String operator;
        public int order_factor;
        public String product_desc;
        public String product_memo;
        public String product_name;
        public String product_url;
        public int release_loan_time;
        public String release_loan_time_unit;
        public long update_date;
    }
}
