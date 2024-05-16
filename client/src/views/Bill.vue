<template>
    <div>
        <nav-bar/>
        <van-divider>结余</van-divider>
        <van-overlay :show="loading" class-name="loading-overlay">
            <van-loading color="#1989fa" size="38px"/>
        </van-overlay>
        <van-row id="balance-box">
            <van-col span="8" offset="8" style="font-size: 28px">{{balanceTotal.toFixed(2)}}</van-col>
            <van-col span="8">
                <van-dropdown-menu>
                    <van-dropdown-item :title="currentYear + '年'" @close="dropdownItemClose" title-class="dropdown-title">
                        <van-picker :columns="years" :default-index="years.length - 1" ref="yearPicker"/>
                    </van-dropdown-item>
                </van-dropdown-menu>
            </van-col>
        </van-row>
        <van-row>
            <van-col span="12"><span class="title-span">Income</span><span class="value-span">{{income.toFixed(2)}}</span></van-col>
            <van-col span="12"><span class="title-span">Expense</span><span class="value-span">{{expend.toFixed(2)}}</span></van-col>
        </van-row>
        <van-cell-group>
            <van-cell>
                <van-col span="6">月份</van-col>
                <van-col span="6">Income</van-col>
                <van-col span="6">Expense</van-col>
                <van-col span="6">结余</van-col>
            </van-cell>
            <van-cell v-for="(balance, index) in balances" :key="index">
                <van-col span="6">{{balance['months']}}月</van-col>
                <van-col span="6">{{balance['income'].toFixed(2)}}</van-col>
                <van-col span="6">{{balance['expend'].toFixed(2)}}</van-col>
                <van-col span="6">{{balance['balance'].toFixed(2)}}</van-col>
            </van-cell>
        </van-cell-group>

        <tabbar/>
    </div>
</template>

<script>
    import NavBar from "@/components/NavBar";
    import Tabbar from "@/components/Tabbar";

    export default {
        name: "Bill",
        components: {
            NavBar, Tabbar
        },
        data() {
            return {
                years: [],
                currentYear: '',
                balanceTotal: 0,
                expend: 0,
                income: 0,
                balances: [],
                loading: true
            }
        },
        mounted() {
            this.$axios.get(`/getBillTime/${this.$store.jsonParse(this.$store.state.user)["userId"]}`)
                .then(response => {
                    const years = response.data.years;
                    this.years = years;
                    this.currentYear = years[years.length - 1];
                    this.setBalances();
                });
        },
        methods: {
            dropdownItemClose() {
                this.currentYear = this.$refs.yearPicker.getValues()[0];
                this.setBalances();
            },
            setBalances() {
                this.$axios.get('/getBalance', {
                    params: {
                        userId: this.$store.jsonParse(this.$store.state.user)["userId"],
                        year: this.currentYear
                    }
                })
                    .then(response => {
                        const balances = response.data;
                        this.balanceTotal = balances.balanceTotal;
                        this.expend = balances.expend;
                        this.income = balances.income;
                        this.balances = balances['balance'];
                        this.loading = false;
                    })
                    .catch(error => {
                        console.log(error);
                    });
            }
        }
    }
</script>

<style scoped>
    .van-col {
        text-align: center;
    }
    .van-dropdown-menu {
        padding-right: 20px;
        float: right;
    }
    #balance-box {
        height: 28px;
        line-height: 28px;
        padding-bottom: 20px;
    }
    #balance-box .van-col,
    .van-dropdown-menu {
        height: inherit;
        line-height: inherit;
    }
    .van-dropdown-menu::after {
        border: none;
    }
    .van-dropdown-menu >>> .dropdown-title {
        font-size: 16px;
        /*font-weight: bold;*/
    }
    .title-span {
        color: #999;
        font-size: 14px;
        padding-right: 15px;
    }
    .value-span {
        font-size: 18px;
    }
    .van-cell-group {
        margin-top: 30px;
        height: calc(100% - 254px);
        overflow-y: auto;
    }
    .loading-overlay {
        height: calc(100% - 152px);
    }
</style>