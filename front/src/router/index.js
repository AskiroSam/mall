import {createRouter, createWebHistory} from 'vue-router'
import IndexView from "@/views/admin/IndexView.vue";
import HomeView from "@/views/admin/HomeView.vue";
import AdminListView from "@/views/admin/AdminListView.vue";
import CategoryListView from "@/views/admin/CategoryListView.vue";
import GoodsListView from "@/views/admin/GoodsListView.vue";
import OrderListView from "@/views/admin/OrderListView.vue";
import UserListView from "@/views/admin/UserListView.vue";
import LoginView from "@/views/admin/LoginView.vue";
import UserLoginView from "@/views/user/LoginView.vue"
import UserHomeView from "@/views/user/HomeView.vue"
import UserIndexView from "@/views/user/IndexView.vue"
import SearchView from "@/views/user/SearchView.vue"
import RegView from "@/views/user/RegView.vue";
import { useTokenStore } from "@/stores/token";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
          path: '/',
          name: 'user_home',
          component: UserHomeView,
          redirect: '/user/index',
          children: [
              {
                  path: '/user/login',
                  name: 'user_login',
                  component: UserLoginView
              },
              {
                  path: '/user/index',
                  name: 'user_index',
                  component: UserIndexView
              },
              {
                  path: '/user/search',
                  name: 'user_search',
                  component: SearchView
              },
              {
                  path: '/user/reg',
                  name: 'user_reg',
                  component: RegView
              }
          ]
        },
        {
          path: '/admin/login',
          name: '/admin_login',
          component: LoginView
        },
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

/*
* 导航守卫
*   1.为什么要配置导航守卫
*       判断页面跳转是不是合法
*           合法 - 跳转
*           不合法 - 不跳转/跳转到特定的页面
*   2.应用
*       判断是否已经登录
*           登录 - 允许跳转
*           未登录 - 跳转到登录页
* */
router.beforeEach((to, from) => {
    //to - 要访问的位置 "/"
    //from - 起始位置
    // TODO 将来还会对其它的页面放行（例如：前台首页，搜索页，详情页，用户登录/注册页）
    if (to.path == '/admin/login' ||
        to.path == '/' ||
        to.path == '/user/login' ||
        to.path == "/user/index" ||
        to.path == "/user/reg" ||
        to.path == "/user/search") {
        return true;
    } else {
        //判断是否有token 有-放行 没有-跳转到登录页
        const tokenStore = useTokenStore();
        if (tokenStore.tokenStr) {
            return true;
        } else {
            //判断跳转到管理员登录页还是用户登录页
            let currentPath = router.currentRoute.value.path;
            if (to.path.startsWith("/admin")) {
                return "/admin/login";
            } else {
                return "/user/login";
            }
        }
    }
})


export default router
