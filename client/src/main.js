import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import qs from 'qs'
import {Notify, Toast, Dialog} from 'vant'
import {Col, Row, Field, Button, Form, Tab, Tabs, NavBar, Tabbar, TabbarItem, Icon, DropdownMenu, DropdownItem,
  DatetimePicker, List, CellGroup, Cell, Grid, GridItem, NumberKeyboard, Popup, Panel, Picker, Divider, Loading, Overlay} from 'vant';
import './assets/css/iconfont.css'
import VeLine from 'v-charts/lib/line.common.min'
// 引用echarts模块文件是为了将echarts属性用在v-charts
import 'echarts/lib/component/title'

Vue.config.productionTip = true;

axios.defaults.baseURL = '/api';
// axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
Vue.prototype.$axios = axios;
Vue.prototype.$qs = qs;
Vue.prototype.$notify = Notify;
Vue.prototype.$toast = Toast;
Vue.prototype.$dialog = Dialog
Vue.use(Col);
Vue.use(Row);
Vue.use(Field);
Vue.use(Button);
Vue.use(Form);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(NavBar);
Vue.use(Tabbar);
Vue.use(TabbarItem);
Vue.use(Icon);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(DatetimePicker);
Vue.use(List);
Vue.use(CellGroup);
Vue.use(Cell);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(NumberKeyboard);
Vue.use(Popup);
Vue.use(Panel);
Vue.use(Picker);
Vue.use(Divider);
Vue.use(Loading);
Vue.use(Overlay);
Vue.component(VeLine.name, VeLine);

/*路由守卫 如果未Login则跳到/login*/
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (JSON.stringify(store.state.user) === "{}") {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else {
      if (!store.state.isLogin) {
        let params = {account: store.jsonParse(store.state.user)["userName"], password: store.jsonParse(store.state.user)["password"]};
        axios.post("/login", qs.stringify(params)).then();
        store.setIsLoginAction(true);
      }
      next();
    }
  } else {
    next();
  }
});

/*全局状态存储*/
let store = {
  debug: true,
  state: {
    isLogin: false,
    user: localStorage.getItem("user") ? localStorage.getItem("user") : {},
    currentDate: new Date()
  },
  jsonParse(json) {
    return typeof json === "string" ? JSON.parse(json) : json;
  },
  setIsLoginAction(isLogin) {
    if (this.debug) console.log('setIsLoginAction triggered with', isLogin);
    this.state.isLogin = isLogin;
  },
  setUserAction(user) {
    if (this.debug) console.log('setUserAction triggered with', user);
    this.state.user = user;
  },
  clearUserAction() {
    if (this.debug) console.log('clearUserAction triggered');
    this.state.user = {};
  },
  setCurrentDate(currentDate) {
    if (this.debug) console.log('setCurrentDate triggered with', currentDate);
    this.state.currentDate = currentDate;
  },
  format(date) {
    let month = (date.getMonth() + 1).toString();
    month = month.length < 2 ? `0${month}` : month;
    let day = date.getDate().toString();
    day = day.length < 2 ? `0${day}` : day;
    return `${date.getFullYear()}-${month}-${day}`;
  },
  formatChinaDate(date) {
    if (typeof date !== typeof new Date())
      return null;
    let month = (date.getMonth() + 1).toString();
    month = month.length < 2 ? `0${month}` : month;
    let day = date.getDate().toString();
    day = day.length < 2 ? `0${day}` : day;
    let week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    return `${date.getFullYear()}年${month}月${day}日 ${week[date.getDay()]}`;
  }
};
Vue.prototype.$store = store;

new Vue({
  render: h => h(App),
  router
}).$mount('#app');