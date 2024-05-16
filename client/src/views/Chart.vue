<template>
    <div>
        <nav-bar/>
        <div class="head">
            <van-tabs v-model="category"
                      type="card"
                      color="#1989fa"
                      title-active-color="#eee"
                      title-inactive-color="#444"
            >
                <van-tab title="Expense"/>
                <van-tab title="Income"/>
            </van-tabs>
            <van-tabs type="card"
                      color="#1989fa"
                      title-active-color="#eee"
                      title-inactive-color="#444"
            >
                <van-tab title="周"/>
                <van-tab title="月"/>
                <van-tab title="年"/>
            </van-tabs>
        </div>
        <van-tabs color="#1989fa"
                  title-active-color="#333"
                  title-inactive-color="#999"
        >
            <van-tab v-for="index in 8" :key="index" :title="'标签 ' + index"/>
        </van-tabs>
        <ve-line
                height="300px"
                :settings="chartSettings"
                :data="chartData"
                :title="chartOptions.title"
                :legend-visible="false"
        />
        <tabbar/>
    </div>
</template>

<script>
    import NavBar from "@/components/NavBar";
    import Tabbar from "@/components/Tabbar";

    export default {
        name: "Chart",
        components: {
            NavBar, Tabbar
        },
        data() {
            return {
                category: 0,
                money: 1200,
                average: 600,
                chartSettings: {
                },
                chartData: {
                    columns: ['日期', '访问用户'],
                    rows: [
                        { '日期': '1/1', '访问用户': 1393},
                        { '日期': '1/2', '访问用户': 3530},
                        { '日期': '1/3', '访问用户': 2923},
                        { '日期': '1/4', '访问用户': 1723},
                        { '日期': '1/5', '访问用户': 3792},
                        { '日期': '1/6', '访问用户': 4593}
                    ]
                },
                chartOptions: {
                    title: {
                        text: ``,
                        textAlign: 'left',
                        padding: [15, 0, 0, 10],
                        textStyle: {
                            color: '#666',
                            fontSize: 14,
                            fontWeight: 'normal'
                        }
                    }
                }
            }
        },
        watch: {
            category(newValue) {
                this.chartOptions.title.text =
                    `${newValue == 0 ? "总Expense" : "总Income"}：${this.money}\n平均值：${this.average}`;
            }
        },
        mounted() {
            this.initTitle();
        },
        methods: {
            initTitle() {
                this.chartOptions.title.text =
                    `${this.category == 0 ? "总Expense" : "总Income"}：${this.money}\n平均值：${this.average}`;
            }
        }
    }
</script>

<style scoped>
    .head {
        margin-top: 10px;
    }
    .head > .van-tabs {
        display: inline-block;
    }
    .head > .van-tabs:first-child {
        width: 40%;
    }
    .head > .van-tabs:last-child {
        width: 60%;
    }
</style>