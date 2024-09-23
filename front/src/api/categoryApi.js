import service from "@/api/index.js";

const categoryApi = {
    //添加
    insert(category) {
        return service.post('/category', category);
    },
    //删除
    delete(id) {
        return service.delete(`/category/${id}`);
    },
    //修改
    update(category) {
        return service.put('/category', category);
    },
    //分页搜索
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNUm = pageNum;
        condition.pageSize = pageSize;
        return service.get('/category', {
            params: condition
        })
    },
    //根据id搜索
    selectById(id) {
        return service.get(`/category/${id}`);
    },
    //查询所有的夫分类
    selectAllParent() {
        return service.get('/category/allParent');
    }
};

export default categoryApi;