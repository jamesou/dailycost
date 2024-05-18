<template>
    <div>
        <NavBar/>
        <div class="top">
      
       
                <van-cell-group>
                    <van-cell title="UserId" :value="userInfo.userId" />
                    <van-cell title="UserName" :value="userInfo.userName" />
                    <van-cell title="Email" :value="userInfo.userMail" />
                </van-cell-group>
            <br />
     
            <div class="top">
            <van-button color="linear-gradient(to right, #ff6034, #ee0a24)" style="width: 100%;" @click="LogOut">
        Logout
        </van-button>
            </div>
 
        </div>
 
        <Tabbar/>
    </div>
</template>

<script>
    import NavBar from "@/components/NavBar";
    import Tabbar from "@/components/Tabbar";
 
    export default {
        name: "Mine",
        components: {
            NavBar, Tabbar
        },
        data() {
            try {
            // 尝试从localStorage获取并解析用户信息
            const userStr = localStorage.getItem("user") ? localStorage.getItem("user") : {};
            const userInfo = userStr ? JSON.parse(userStr) : null;
            return {
                userInfo, // 如果成功解析，返回解析后的用户信息；否则返回null
            };
            } catch (error) {
            // 如果解析失败，处理错误并返回默认的userInfo（例如一个空对象或null）
            console.error('Error parsing user data from localStorage:', error);
            return {
                userInfo: {},  
            };
            }
        },
        // mounted() { 
             
        //      this.$notify({type: 'primary', message: this.userInfo})
        // },
        methods: {
            LogOut() {
                localStorage.clear();
                this.$router.replace('/login');
            }
        }     
    };
</script>

<style scoped>

</style>