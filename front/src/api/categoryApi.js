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
    //是否推荐
    chgRecom(category) {
        return service.put(`/category/chgRecom`, category);
    },
    //是否上架
    chgStatus(category) {
        return service.put(`/category/chgStatus`, category);
    },
    //分页搜索
    selectByPage(condition, pageNum, pageSize) {
        if (pageNum) {
            condition.pageNum = pageNum;
        }

        if (pageSize) {
            condition.pageSize = pageSize;
        }

        return service.get('/category/search', {
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