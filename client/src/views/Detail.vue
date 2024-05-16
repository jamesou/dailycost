<template>
    <div style="background-color: #F8F8F8">
        <nav-bar :go-back="true"/>
        <van-panel>
            <template #header>
                <van-cell :title="categoryName" title-class="panel-header" size="large">
                    <template #icon>
                        <van-icon class-prefix="iconfont" :name="categoryIcon" size="30px"/>
                    </template>
                </van-cell>
            </template>
            <div class="panel-body">
                <van-cell title="Category" :value="categoryState === 0 ? 'Expense' : 'Income'" value-class="cell-value"/>
                <van-cell title="Amount" :value="billAmount" value-class="cell-value"/>
                <van-cell title="Date" :value="this.$store.formatChinaDate(billTime)" value-class="cell-value"/>
                <van-cell title="Comment" :value="billRemark" value-class="cell-value"/>
            </div>
        </van-panel>
        <van-row>
            <van-col span="12"><van-button color="#92B8F0" icon="edit" round @click="editBill">Modify</van-button></van-col>
            <van-col span="12"><van-button color="#FCB040" icon="delete" round @click="deleteBill">Delete</van-button></van-col>
        </van-row>
    </div>
</template>

<script>
    import NavBar from "@/components/NavBar";

    export default {
        name: "Detail",
        components: {
            NavBar
        },
        data() {
            return {
                billId: 0,
                categoryId: 0,
                categoryIcon: '',
                categoryName: '',
                categoryState: '',
                billAmount: '',
                billTime: '',
                billRemark: ''
            }
        },
        mounted() {
            let billId = this.$route.params.billId;
            if (!billId) {
                this.$router.replace('/details');
                return;
            }
            this.$axios.get(`/bill/${this.$route.params.billId}`)
                .then(response => {
                    this.billId = response.data['billId'];
                    this.categoryId = response.data['categoryId'];
                    this.categoryIcon = response.data['categoryIcon'];
                    this.categoryName = response.data['categoryName'];
                    this.categoryState = response.data['categoryState'];
                    this.billAmount = response.data['billAmount'].toString();
                    this.billTime = new Date(response.data['billTime']);
                    this.billRemark = response.data['billRemark'];
                })
                .catch(error => {
                    console.log(error);
                });
        },
        methods: {
            /*修改账单明细*/
            editBill() {
                this.$router.push({
                    name: 'bookkeeping',
                    params: {
                        billId: this.billId,
                        categoryState: this.categoryState,
                        categoryId: this.categoryId,
                        billAmount: this.categoryState === 0 ? this.billAmount.split('-')[1] : this.billAmount,
                        billTime: this.billTime,
                        billRemark: this.billRemark,
                    }
                });
            },
            /*Delete 账单明细*/
            deleteBill() {
                this.$dialog.confirm({
                    title: 'Prompt',
                    message: 'Confirm del the record?'
                }).then(() => {
                    // on confirm
                    this.$axios.delete(`/bill/${this.billId}`)
                        .then(response => {
                            if (response.data === 1)
                                this.$notify({type: 'primary', message: 'Delete Successfully!'});
                            else
                                this.$notify({type: 'warning', message: 'Delete Failed!'});
                            this.$router.go(-1);
                        })
                        .catch(error => {
                            console.log(error);
                        });
                }).catch(() => {
                    // on cancel
                });
            }
        }
    }
</script>

<style scoped>
    .van-panel {
        margin: 30px 10px 10px;
        box-shadow: 2px 2px 5px #ccc;
    }
    .panel-header {
        margin-left: 10px;
    }
    .panel-body {
        padding: 10px;
    }
    .cell-value {
        /*text-align: left;*/
        flex: 5;
    }
    .van-row {
        margin-top: 60px;
    }
    .van-col {
        text-align: center;
    }
    .van-button {
        width: 60%;
        box-shadow: 2px 2px 3px #ccc;
    }
</style>