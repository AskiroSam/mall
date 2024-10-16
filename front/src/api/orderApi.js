import service from "@/api/index.js";

const orderApi = {
    //添加
    insert(orderVo) {
        return service.post('/order', orderVo);
    },
    //删除
    delete(id) {
        return service.delete(`/user/${id}`);
    },
    //修改
    update(order) {
        return service.put('/order', order);
    },
    //修改可用状态
    setStatus(status) {
        return service.put(`/user/${status}`);
    },
    //根据id查询
    selectById(id) {
        return service.get(`/order/${id}`);
    },
    //分页搜索
    selectByPage(order, pageNum, pageSize) {
        order.pageNum = pageNum;
        order.pageSize = pageSize;
        return service.get('/order', {
            params: order
        });
    },
    //查询所有
    selectAll() {
        return service.get('/order/all');
    },
    //支付
    pay(orderVo) {
        return service.post('/order/pay', orderVo);
    },
    //订单超时
    checkTime(id) {
        return service.get(`/order/checkTime/${id}`);
    }
}
export default orderApi;