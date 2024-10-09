import service from "@/api/index.js";
import qs from "qs";

const cartApi = {
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    }
}
export default cartApi;