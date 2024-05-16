import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import Details from "@/views/Details";
import Chart from "@/views/Chart";
import Bookkeeping from "@/views/Bookkeeping";
import Bill from "@/views/Bill";
import Mine from "@/views/Mine";
import Detail from "@/views/Detail";

Vue.use(VueRouter);

export default new VueRouter({
    routes: [
        {
            // 设置默认路由
            path: '*',
            redirect: {name: 'details'}
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/details',
            name: 'details',
            component: Details,
            meta: {requiresAuth: true}
        },
        {
            path: '/chart',
            name: 'chart',
            component: Chart,
            meta: {requiresAuth: true}
        },
        {
            path: '/bookkeeping',
            name: 'bookkeeping',
            component: Bookkeeping,
            meta: {requiresAuth: true}
        },
        {
            path: '/bill',
            name: 'bill',
            component: Bill,
            meta: {requiresAuth: true}
        },
        {
            path: '/mine',
            name: 'mine',
            component: Mine,
            meta: {requiresAuth: true}
        },
        {
            path: '/detail',
            name: 'detail',
            component: Detail,
            meta: {requiresAuth: true}
        }
    ]
})