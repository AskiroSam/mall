import service from "@/api/index.js";
import qs from "qs";

const cartApi = {
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    },
    delete(id) {
        return service.delete(`/cart/${id}`);
    },
    update(cart) {
        return service.put("/cart", cart);
    },
    selectCartList() {
        return service.get("/cart");
    }
}
export default cartApi;