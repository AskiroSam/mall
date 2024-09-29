import service from "@/api/index.js";
import qs from "qs";

const adminApi = {
    //添加
    insert(admin) {
        return service.post('/admin', admin);
    },
    //删除
    delete(id) {
        return service.delete(`/admin/${id}`);
    },
    //修改
    update(admin) {
        return service.put('/admin', admin);
    },
    //修改可用状态
    restPwd(id) {
        return service.put(`/admin/${id}`);
    },
    //根据id查询
    selectById(id) {
        return service.get(`/admin/${id}`);
    },
    //分页搜索
    selectByPage(admin, pageNum, pageSize) {
        admin.pageNum = pageNum;
        admin.pageSize = pageSize;
        return service.get('/admin/search', {
            params: admin
        });
    },
    //获取验证码
    captcha() {
        return service.get("/admin/captcha");
    },
    //登录
    login(admin) {
        return service.post("/admin/login", qs.stringify(admin));
    },
    //获取已登录用户的信息
    getInfo() {
        return service.get("/admin/info");
    },
    //修改密码
    chgPwd(chgPwdObj) {
        return service.put("/admin/chgPwd", qs.stringify(chgPwdObj));
    },
    //修改个人信息
    chgInfo(admin) {
        return service.put("/admin/chgInfo", admin);
    },
}
export default adminApi;