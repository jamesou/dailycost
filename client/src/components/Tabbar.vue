<template>
    <van-tabbar route>
        <van-tabbar-item replace to="/details" icon="balance-list-o">明细</van-tabbar-item>
        <!-- <van-tabbar-item replace to="/chart" icon="chart-trending-o">图表</van-tabbar-item> -->
        <van-tabbar-item to="/bookkeeping">
            <template #icon><van-icon name="add" color="#1989fa"/></template>
            记账
        </van-tabbar-item>
        <van-tabbar-item replace to="/bill" icon="bill-o">账单</van-tabbar-item>
        <van-tabbar-item replace to="/mine" icon="user-circle-o">我的</van-tabbar-item>
    </van-tabbar>
</template>

<script>
    export default {
        name: "Tabbar"
    }
</script>

<style scoped>

</style>