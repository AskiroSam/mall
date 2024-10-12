import service from "@/api/index.js";
import qs from "qs";

const collectApi = {
    //收藏
    insert(goodsId) {
        return service.post(`/collect/${goodsId}`);
    },
    //取消收藏
    delete(goodsId) {
        return service.delete(`/collect/${goodsId}`);
    },
    //根据商品id和用户id查询
    selectByGoodsIdAndUserId(goodsId) {
        return service.get(`/collect/${goodsId}`);
    },
    //分页搜索
    selectByPage(collect, pageNum, pageSize) {
        collect.pageNum = pageNum;
        collect.pageSize = pageSize;
        return service.get('/collect', {
            params: collect
        });
    },
    selectById(id) {
        return service.get(`/collect/${id}`)
    }
}
export default collectApi;