import service from "@/api/index.js";
import qs from "qs";

const userApi = {
    //添加
    insert(user) {
        return service.post('/user', user);
    },
    //删除
    delete(id) {
        return service.delete(`/user/${id}`);
    },
    //修改
    update(user) {
        return service.put('/user', user);
    },
    //修改密码
    updatePwd(user) {
        return service.put('/user/pwd', user)
    },
    //修改密码
    updatePay(user) {
        return service.put('/user/pay', user)
    },
    //修改可用状态
    setStatus(status) {
        return service.put(`/user/${status}`);
    },
    //根据id查询
    selectById(id) {
        return service.get(`/user/${id}`);
    },
    //分页搜索
    selectByPage(user, pageNum, pageSize) {
        user.pageNum = pageNum;
        user.pageSize = pageSize;
        return service.get('/user/search', {
            params: user
        });
    },
    //验证码
    captcha() {
        return service.get("/user/captcha");
    },
    //注册
    reg(userInfo) {
        return service.post('/user/reg', qs.stringify(userInfo))
    },
    //登录
    login(userInfo) {
        return service.post('/user/login', qs.stringify(userInfo))
    },
    //获取用户信息
    getInfo() {
        return service.get('/user/info')
    }
}
export default userApi;