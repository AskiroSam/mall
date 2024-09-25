import service from "@/api/index.js";

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
    setStatus(id) {
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
    }
}
export default adminApi;