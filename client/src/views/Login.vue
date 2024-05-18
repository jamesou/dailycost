<template>
    <div>
        <van-nav-bar :border="false">
            <template #title><img src="../assets/images/ic_launcher.png" style="padding-top: 20px;" alt=""></template>
        </van-nav-bar>
        <van-tabs
                style="height: calc(100% - 74px)"
                v-model="active"
                color="#6083B8"
                title-active-color="#000"
                title-inactive-color="#ccc"
                :border="false"
                animated
                swipeable>
            <van-tab title="Login" title-style="font-size: 20px; font-weight: bold;">
                <van-form @submit="login">
                    <van-field
                            v-model="account"
                            name="account"
                            placeholder="Username/Email"
                            size="large"
                    />
                    <van-field
                            v-model="password"
                            type="password"
                            name="password"
                            placeholder="Please input password"
                            size="large"
                    />
                    <div style="margin: 16px; text-align: center">
                        <van-button class="button-radius"
                                    :disabled="loginDisabled"
                                    color="linear-gradient(to right, #4bb0ff, #6149f6)"
                                    native-type="submit"
                                    square
                                    block
                        >
                            Login
                        </van-button>
                        <!-- <a class="tag-a">忘记密码</a> -->
                    </div>
                </van-form>
            </van-tab>
            <van-tab title="Register" title-style="font-size: 20px; font-weight: bold;">
                <van-form @submit="register">
                    <!-- <van-field
                            v-model="userNickname"
                            name="userNickname"
                            placeholder="输入昵称"
                            size="large"
                            value="adminadmin" 
                            :rules="[{ pattern: /^\S{3,}$/, message: '用户名至少需要3个字符且不包括空格' }]"
                            hidden
                    /> -->
                    <van-field
                            v-model="userName"
                            name="userName"
                            placeholder="Please input username"
                            size="large"
                            :rules="[{ pattern: /^\S+$/, message: 'Can not be empty' }]"
                            required
                    />
                
                    <van-field
                            v-model="userMail"
                            name="userMail"
                            placeholder="Please input email address"
                            size="large"
                            :rules="[{ pattern: /^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: 'Incorrect email format' }]"
                            required
                    />
                    <!-- <van-field
                            v-model="userPhone"
                            name="userPhone"
                            placeholder="输入手机号"
                            size="large"
                    /> -->
                    <van-field
                            @input="passwordCheck"
                            v-model="userPassword"
                            type="password"
                            name="userPassword"
                            placeholder="Please input password"
                            size="large"
                            :rules="[{ pattern: /^\S+$/, message: 'Password can not be empty' }]"
                            required
                    />
                    <van-field
                            @blur="passwordCheck"
                            v-model="againPassword"
                            type="password"
                            name="userPassword"
                            placeholder="Confirm password"
                            size="large"
                            :error="error"
                            :error-message="errorMessage"
                            required
                    />
                    <div style="margin: 16px;">
                        <van-button class="button-radius"
                                    :disabled="registerDisabled"
                                    color="linear-gradient(to right, #4bb0ff, #6149f6)"
                                    native-type="submit"
                                    square
                                    block
                        >
                            Register
                        </van-button>
                    </div>
                </van-form>
            </van-tab>
        </van-tabs>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                active: 0,
                account: '',
                password: '',
                loginDisabled: true,
                userNickname: 'admin',
                userName: '',
                userMail: '',
                userPhone: '',
                userPassword: '',
                againPassword: '',
                registerDisabled: true,
                error: false,
                errorMessage: ''
            };
        },
        methods: {
            generatePhoneNumber() {
                // 手机号码的前缀，你可以根据需要添加更多前缀
                const prefixes = ['138', '139', '137', '151', '159'];
                const randomPrefix = prefixes[Math.floor(Math.random() * prefixes.length)];

                // 随机生成8位数字作为手机号码的后缀
                const suffix = Math.floor(10000000 + Math.random() * 90000000);

                // 组合前缀和后缀形成完整的手机号码
                const phoneNumber = `${randomPrefix}${suffix.toString().padStart(8, '0')}`;
 
                return phoneNumber;
            },
            /*Login*/
            login(params) {
                params.userNickname='admin'
                params.userPhone=''
                this.$axios.post("/login", this.$qs.stringify(params))
                    .then(response => {
                        // console.log(response)
                        //network no errors
                        if (response.status === 200) {
                            // console.log(response.data.loginState==false)
                            if(response.data.loginState == false){
                                this.$notify({type: 'primary', message: response.data.msg})
                            }
                            if(response.data.loginState == true){
                            this.$notify({type: 'primary', message: response.data.msg})
                            //store user into localStorage 
                            response.data.password = params.password;
                            localStorage.setItem("user", JSON.stringify(response.data));
                            //store user in browser/webview
                            this.$store.setIsLoginAction(true);
                            this.$store.setUserAction(response.data);
                            const redirect = this.$route.query.redirect ? this.$route.query.redirect : "/details";
                            this.$router.replace({path: redirect});
                            }
                        }
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            /*Register*/
            register(params) {
                params.phoneNumber = this.generatePhoneNumber();
                this.$axios.post("/user", params)
                    .then(response => {
                        if (response.status === 200) {
                            this.$toast.success("Register Successfully");
                            let data = {
                                account: params.userName,
                                password: params.userPassword
                            };
                            this.login(data);
                        }
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            /*参数校验*/
            paramsPattern() {
                return /^\S+$/.test(this.userName)
                    && /^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.userMail)
                    && /^\S+$/.test(this.userPassword);
            },
            /*密码校验*/
            passwordCheck() {
                if (this.againPassword === '')
                    return;
                if (this.againPassword === this.userPassword) {
                    this.error = false;
                    this.errorMessage = '';
                    this.registerDisabled = !this.paramsPattern();// 只有参数校验Successfully才可以启用Register按钮
                } else {
                    this.error = true;
                    this.errorMessage = 'Password is inconsistent, please re-enter';
                    this.registerDisabled = true;
                }
            }
        },
      
        computed: {
            /*整合Login参数*/
            loginParams: function () {
                const {account, password} = this;
                return {account, password};
            },
            /*整合Register参数*/
            registerParams: function () {
                const {userNickname, userName, userPassword, againPassword} = this;
                return {userNickname, userName, userPassword, againPassword}
            }
        },
        watch: {
            /*监听Login参数*/
            loginParams: function (params) {
                if (params.account !== '' && params.password !== '') {
                    this.loginDisabled = false;
                }
            },
            /*监听Register参数*/
            registerParams: function (params) {
                if (params.userNickname !== ''
                    && params.userName !== ''
                    && params.userPassword !== ''
                    && params.againPassword !== '') {
                    this.passwordCheck();
                }
            }
        }
    }
</script>

<style scoped>
    .van-form {
        padding: 0 20px;
    }
    .van-field {
        font-size: 16px;
    }
    .button-radius {
        border-radius: 10px;
    }
    .tag-a {
        display: block;
        margin-top: 5px;
        font-size: 14px;
        font-weight: 500;
        color: #6083B8;
    }
    div >>> .van-nav-bar {
        height: 74px;
        line-height: 74px;
        margin-top: 40px;
    }
    .van-tabs >>> .van-tabs__wrap {
        margin-bottom: 60px;
    }
    .van-tabs >>> .van-tabs__content {
        height: calc(100% - 104px);
    }
</style>