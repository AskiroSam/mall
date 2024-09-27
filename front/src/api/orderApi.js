import service from "@/api/index.js";

const orderApi = {
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
    }
}
export default orderApi;