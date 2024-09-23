import axios from "axios";

//创建axios的对象(只能面向一个服务)
const service = axios.create({
    baseURL: import.meta.env.VITE_SERVER_ADDR
});

//响应拦截器
service.interceptors.response.use(resp => {
    return resp.data;
}, error => {

});

export default service;