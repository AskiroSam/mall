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
    }
}
export default collectApi;