import axios from "axios";
import { useTokenStore } from "@/stores/token.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

//创建axios的对象(只能面向一个服务)
const service = axios.create({
    baseURL: import.meta.env.VITE_SERVER_ADDR
});

//Axios的请求拦截器
// service.interceptors.request.use(req => {
//     const tokenStore = useTokenStore();
//     if (tokenStore.tokenStr) {
//         req.headers.token = tokenStore.tokenStr;
//     }
//     return req;
// }, error => {
//
// });
service.interceptors.request.use(function (config) {
    const tokenStore = useTokenStore();
    // 如果不是登录请求就要在请求头中添加token
    if (!config.url.startsWith("/admin/login")) {
        //将store中的token放在请求头中
        config.headers.token = tokenStore.tokenStr;
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

//响应拦截器
service.interceptors.response.use(resp => {
    //获取续期的JWT
    let token = resp.headers.token;
    if (token) {
        // 将续期后的jwt放在store中
        const tokenStore = useTokenStore();
        tokenStore.update(token);
    }
    return resp.data;
}, error => {
    if (error.status == 403) {
        ElMessage.error({
            message: '令牌错误，请重新登录',
            duration: 1200,
            onClose: () => {
                //删除sessionStorage的token
                // sessionStorage.removeItem('token');
                const tokenStore = useTokenStore();
                tokenStore.$reset();
                //判断跳转到管理员登录页还是用户登录页
                let currentPath = router.currentRoute.value.path;
                if (currentPath.startsWith("/admin")) {
                    router.push("/admin/login");
                } else {
                    router.push("/user/login");
                }
            }
        });
    }
});

export default service;