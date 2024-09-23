import {createRouter, createWebHistory} from 'vue-router'
import IndexView from "@/views/admin/IndexView.vue";
import HomeView from "@/views/admin/HomeView.vue";
import AdminListView from "@/views/admin/AdminListView.vue";
import CategoryListView from "@/views/admin/CategoryListView.vue";
import GoodsListView from "@/views/admin/GoodsListView.vue";
import OrderListView from "@/views/admin/OrderListView.vue";
import UserListView from "@/views/admin/UserListView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/admin/home',
            name: 'admin_home',
            component: HomeView,
            redirect: "/admin/index",
            children: [
                {
                    path: '/admin/index',
                    name: 'admin_index',
                    component: IndexView
                }, {
                    path: '/admin/adminlist',
                    name: 'admin_adminlist',
                    component: AdminListView
                }, {
                    path: '/admin/categorylist',
                    name: 'admin_categorylist',
                    component: CategoryListView
                }, {
                    path: '/admin/goodslist',
                    name: 'admin_goodslist',
                    component: GoodsListView
                }, {
                    path: '/admin/orderlist',
                    name: 'admin_orderlist',
                    component: OrderListView
                }, {
                    path: '/admin/userlist',
                    name: 'admin_userlist',
                    component: UserListView
                }
            ]
        }
    ]
})

export default router
