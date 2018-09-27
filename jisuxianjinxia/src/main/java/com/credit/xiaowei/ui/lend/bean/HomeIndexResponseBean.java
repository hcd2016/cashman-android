package com.credit.xiaowei.ui.lend.bean;

import java.util.List;

public class HomeIndexResponseBean {
    /**
     * amount_days_list : {"amounts":["10000","20000","30000","40000","50000","60000","70000","80000","90000","100000"],"days":["7","14"],"interests":["9800","15000"]}
     * item : {"card_title":"现金侠","card_amount":"100000","card_verify_step":"认证0/5"}
     * user_loan_log_list : ["尾号8203，正常还款，成功提额至1050元11","尾号8203，正常还款，成功提额至1050元10","尾号8203，正常还款，成功提额至1050元9","尾号8203，正常还款，成功提额至1050元8","尾号8203，正常还款，成功提额至1050元7","尾号8203，正常还款，成功提额至1050元6","尾号8203，正常还款，成功提额至1050元5","尾号8203，正常还款，成功提额至1050元4","尾号8203，正常还款，成功提额至1050元3","尾号8203，正常还款，成功提额至1050元2"]
     * today_last_amount : 123400
     */

    private AmountDaysListBean amount_days_list;
    private ItemBean item;
    private String today_last_amount;
    private List<String> user_loan_log_list;

    public List<IndexImagesBean> getIndex_images() {
        return index_images;
    }

    public void setIndex_images(List<IndexImagesBean> index_images) {
        this.index_images = index_images;
    }

    /**
     * index_images : [{"title":"首页活动一","sort":"1","reurl":"url1","url":"http://192.168.1.82:9802/common/web/images/index_banner1.png"},{"title":"首页活动二","sort":"2","reurl":"http://192.168.1.82:9802/gotoDrawAwardsIndex","url":"http://192.168.1.82:9802/common/web/images/index_banner2.png"},{"title":"首页活动三","sort":"3","reurl":"http://192.168.1.82:9802/gotoAboutIndex","url":"http://192.168.1.82:9802/common/web/images/index_banner3.png"}]
     * amount_days_list : {"amounts":["20000","30000","40000","50000","60000","70000","80000","90000","100000","110000","120000","130000","140000","150000","160000","170000","180000","190000","200000","210000","220000","230000","240000","250000","260000","270000","280000","290000","300000","310000","320000","330000","340000","350000","360000","370000","380000","390000","400000","410000","420000","430000","440000","450000","460000","470000","480000","490000","500000","500000"],"days":["7","14"],"interests":["49000","75000"]}
     * item : {"verify_loan_pass":0,"card_title":"现金侠","card_amount":"500000","card_verify_step":"认证3/5","verify_loan_nums":3}
     * today_last_amount : 123400
     */

    private List<IndexImagesBean> index_images;

    /**
     * 活动实体类
     */
    public static class IndexImagesBean {//活动页的图片
        //        title	:	首页活动一
//        sort	:	1
//        reurl	:	url1
//        url	:	http://192.168.1.82:9802/common/web/images/index_banner1.png
        private int sort;
        private String title;
        private String reurl;
        private String url;

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReUrl() {
            return reurl;
        }

        public void setReUrl(String reUrl) {
            this.reurl = reUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public AmountDaysListBean getAmount_days_list() {
        return amount_days_list;
    }

    public void setAmount_days_list(AmountDaysListBean amount_days_list) {
        this.amount_days_list = amount_days_list;
    }

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public String getToday_last_amount() {
        return today_last_amount;
    }

    public void setToday_last_amount(String today_last_amount) {
        this.today_last_amount = today_last_amount;
    }

    public List<String> getUser_loan_log_list() {
        return user_loan_log_list;
    }

    public void setUser_loan_log_list(List<String> user_loan_log_list) {
        this.user_loan_log_list = user_loan_log_list;
    }

    public static class AmountDaysListBean {
        private List<Integer> amounts;
        private List<Integer> days;
        private List<Double> interests;

        public List<Integer> getAmounts() {
            return amounts;
        }

        public void setAmounts(List<Integer> amounts) {
            this.amounts = amounts;
        }

        public List<Integer> getDays() {
            return days;
        }

        public void setDays(List<Integer> days) {
            this.days = days;
        }

        public List<Double> getInterests() {
            return interests;
        }

        public void setInterests(List<Double> interests) {
            this.interests = interests;
        }
    }

    public static class ItemBean {
        /**
         * card_title : 现金侠
         * card_amount : 100000
         * card_verify_step : 认证0/5
         */

        private String card_title;
        private int card_amount;
        private String card_verify_step;
        private int verify_loan_pass;
        private int verify_loan_nums;//认证通过的数量
        private int next_loan_day;  //剩余可借款时间倒计时

        public int getNext_loan_day() {
            return next_loan_day;
        }

        public void setNext_loan_day(int next_loan_day) {
            this.next_loan_day = next_loan_day;
        }

        /**
         * loan_infos : {"lists":[{"title":"审核中","body":"已进入风控审核状态，请您耐心等待","tag":1},{"title":"申请提交成功","body":"申请借款500.00元，期限7天，手续费49.00元","tag":0}],"header_tip":"风控审核中，请耐心等待"}
         */


        private LoanInfosBean loan_infos;



        public int getVerify_loan_pass() {
            return verify_loan_pass;
        }

        public void setVerify_loan_pass(int verify_loan_pass) {
            this.verify_loan_pass = verify_loan_pass;
        }

        public int getVerify_loan_nums() {
            return verify_loan_nums;
        }

        public void setVerify_loan_nums(int verify_loan_nums) {
            this.verify_loan_nums = verify_loan_nums;
        }

        public String getCard_title() {
            return card_title;
        }

        public void setCard_title(String card_title) {
            this.card_title = card_title;
        }

        public int getCard_amount() {
            return card_amount;
        }

        public void setCard_amount(int card_amount) {
            this.card_amount = card_amount;
        }

        public String getCard_verify_step() {
            return card_verify_step;
        }

        public void setCard_verify_step(String card_verify_step) {
            this.card_verify_step = card_verify_step;
        }

        public LoanInfosBean getLoan_infos() {
            return loan_infos;
        }

        public void setLoan_infos(LoanInfosBean loan_infos) {
            this.loan_infos = loan_infos;
        }

        public static class LoanInfosBean {
            /**
             * lists : [{"title":"审核中","body":"已进入风控审核状态，请您耐心等待","tag":1},{"title":"申请提交成功","body":"申请借款500.00元，期限7天，手续费49.00元","tag":0}]
             * header_tip : 风控审核中，请耐心等待
             */

            private String header_tip;
            private List<ListsBean> lists;
            /**
             * button : {"msg":"","id":""}
             */

            private ButtonBean button;
            private int intoMoney;
            private String loanEndTime;
            private int lastRepaymentD;

            public int getLastRepaymentD() {
                return lastRepaymentD;
            }

            public void setLastRepaymentD(int lastRepaymentD) {
                this.lastRepaymentD = lastRepaymentD;
            }

            public int getIntoMoney() {
                return intoMoney;
            }

            public void setIntoMoney(int intoMoney) {
                this.intoMoney = intoMoney;
            }
            public String getLoanEndTime() {
                return loanEndTime;
            }

            public void setLoanEndTime(String loanEndTime) {
                this.loanEndTime = loanEndTime;
            }

            public String getHeader_tip() {
                return header_tip;
            }

            public void setHeader_tip(String header_tip) {
                this.header_tip = header_tip;
            }

            public List<ListsBean> getLists() {
                return lists;
            }

            public void setLists(List<ListsBean> lists) {
                this.lists = lists;
            }

            public ButtonBean getButton() {
                return button;
            }

            public void setButton(ButtonBean button) {
                this.button = button;
            }

            public static class ListsBean {
                /**
                 * title : 审核中
                 * body : 已进入风控审核状态，请您耐心等待
                 * tag : 1
                 */

                private String title;
                private String body;
                private int tag;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getBody() {
                    return body;
                }

                public void setBody(String body) {
                    this.body = body;
                }

                public int getTag() {
                    return tag;
                }

                public void setTag(int tag) {
                    this.tag = tag;
                }
            }

            public static class ButtonBean {
                /**
                 * msg :
                 * id :
                 */

                private String msg;
                private String id;

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }

    /*private HomeIndexBean item;
    private MoneyPeriodBean amount_days_list;
	private List<String> user_loan_log_list;
	private String today_last_amount;

	public List<String> getUser_loan_log_list() {
		return user_loan_log_list;
	}

	public void setUser_loan_log_list(List<String> user_loan_log_list) {
		this.user_loan_log_list = user_loan_log_list;
	}

	public String getToday_last_amount() {
		return today_last_amount;
	}

	public void setToday_last_amount(String today_last_amount) {
		this.today_last_amount = today_last_amount;
	}

	public HomeIndexBean getItem() {
		return item;
	}

	public void setItem(HomeIndexBean item) {
		this.item = item;
	}

	public MoneyPeriodBean getAmount_days_list()
	{
		return amount_days_list;
	}

	public void setAmount_days_list(MoneyPeriodBean amount_days_list)
	{
		this.amount_days_list = amount_days_list;
	}*/
}
