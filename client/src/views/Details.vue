<template>
    <div style="background-color: white;">
        <nav-bar/>
        <van-row class="row-head-top" style="margin: 30px 0 0 0;">
            <van-col span="8" style="color: black; font-weight: bold">{{year}}</van-col>
            <van-col span="8" style="color: green; font-weight: bold">Monthly Income</van-col>
            <van-col span="8" style="color: red; font-weight: bold">Monthly Expense</van-col>
        </van-row>
        <van-row class="row-head-bottom">
            <van-col span="8" style="display: flex; justify-content: flex-start;">
                <van-dropdown-menu>
                    <van-dropdown-item @close="dropdownItemClose" :title="month" title-class="dropdown-title">
                        <van-datetime-picker
                                v-model="currentDate"
                                type="year-month"
                                :min-date="minDate"
                                :max-date="maxDate"
                                :show-toolbar="false"
                                :formatter="formatter"
                        />
                    </van-dropdown-item>
                </van-dropdown-menu>
            </van-col>
            <van-col span="8" style="line-height:50px;">{{amount["income"] ? amount['income'] : '0'}}</van-col>
            <van-col span="8" style="line-height:50px;">{{amount["expend"] ? amount['expend'] : '0'}}</van-col>
        </van-row>
        <van-row style="height: calc(100% - 148px)">
            <van-overlay :show="loading" class-name="loading-overlay">
                <van-loading color="#1989fa" size="38px"/>
            </van-overlay>
            <van-list style="height: 100%; overflow-y: auto;"
                    :finished="true"
                    finished-text="No more data"
            >
                <div v-for="(list, index) in lists" :key="index">
                    <van-cell class="cell-title" :title="list.date">
                        <span class="amount">Income: {{list['income']}}</span>
                        <span class="amount">Expense: {{list['expend']}}</span>
                    </van-cell>
                    <van-cell
                            v-for="(item, key) in list.details"
                            :key="key"
                            :billId="item['billId']"
                            icon-prefix="iconfont"
                            :icon="item.icon"
                            :title="item['remark']"
                            :clickable="true"
                            size="large"
                            @click="clickDetail"
                    >
                        {{item['money']}}
                    </van-cell>
                </div>
            </van-list>
        </van-row>
        <tabbar/>
    </div>
</template>

<script>
    import NavBar from "@/components/NavBar";
    import Tabbar from "@/components/Tabbar";

    export default {
        name: "Details",
        data() {
            return {
                currentDate: this.$store.state.currentDate, // 当前时间选择器选择的时间
                minDate: new Date(), // 时间选择器可选最早日期
                maxDate: new Date(), // 时间选择器可选最新日期
                amount: {}, // IncomeExpense金额
                lists: [], // 账单明细列表
                loading: true
            }
        },
        components: {
            NavBar, Tabbar
        },
        mounted() {
            this.getBills();
        },
        methods: {
            /*格式化下拉时间选择器*/
            formatter(type, val) {
                if (type === 'year') {
                    return `${val}`;
                } else if (type === 'month') {
                    return `${val}`;
                }
                return val;
            },
            /*下拉时间选择器关闭事件*/
            dropdownItemClose() {
                this.$store.setCurrentDate(this.currentDate);
                this.getBills();
            },
            /*查询账单明细*/
            getBills() {
                this.loading = true;
                this.$axios.get(`/bill/${this.$store.jsonParse(this.$store.state.user)["userId"]}/${this.currentDate}`)
                    .then(response => {
                        this.amount = response.data.amount;
                        this.lists = response.data.list;
                        this.loading = false;
                    })
                    .catch(error => {
                        console.log(error);
                    });
                this.$axios.get(`/getBillTime/${this.$store.jsonParse(this.$store.state.user)["userId"]}`)
                    .then(response => {
                        this.minDate = new Date(response.data.first);
                    });
            },
            /*单击账单明细*/
            clickDetail(event) {
                let billId = event.currentTarget.getAttribute("billId");
                this.$router.push({name: 'detail', params: {billId: billId}});
            }
        },
        computed: {
            /*时间选择器年标题*/
            year: function () {
                const year = this.currentDate.getFullYear();
                return `${year}`
            },
            /*时间选择器月标题*/
            month: function () {
                const month = this.currentDate.getMonth() + 1;
                return `${month < 10 ? '0' + month : month}`;
            }
        }
    }
</script>

<style scoped>
    .van-col {
        padding-left: 20px;
        padding-top: 5px;
    }
    .row-head-top,
    .row-head-bottom {
        background-color: #fff;
        /*box-shadow: 0 2px 3px #ccc;*/
    }
    .row-head-top {
        color: #ccc;
        font-size: 14px;
    }
    .row-head-bottom {
        font-size: 18px;
        font-weight: bold;
        color: #000;
        padding-bottom: 5px;
    }
    .row-head-bottom > .van-col {
        line-height: 18px;
    }
    .van-col >>> .dropdown-title {
        font-size: 18px;
        font-weight: bold;
    }
    .van-col >>> .van-hairline--top-bottom::after {
        border-width: 0;
    }
    .van-col >>> .van-dropdown-menu {
        height: auto;
    }
    .van-col >>> .van-dropdown-menu__title {
        padding: 0 8px 0 0;
    }
    .cell-title {
        color: #969799;
    }
    .cell-title > .van-cell__value {
        flex: 2;
    }
    .amount {
        width: 50%;
        display: inline-block;
    }
</style>