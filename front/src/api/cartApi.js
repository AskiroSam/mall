import service from "@/api/index.js";
import qs from "qs";

const cartApi = {
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    },
    delete(id) {
        return service.delete(`/cart/${id}`);
    }
    // ,
    // deleteIdList(idList) {
    //     return service.delete("/cart",{
    //         data: idList // 确保传递的是一个对象，包含 data 字段
    //     });
    // },
    ,
    deleteIdList(idList) {
        return service.delete("/cart",{
            data: {
                idList
            }
        });
    },
    update(cart) {
        return service.put("/cart", cart);
    },
    selectCartList() {
        return service.get("/cart");
    },
    selectById(id) {
        return service.get(`/cart/${id}`);
    }
}
export default cartApi;