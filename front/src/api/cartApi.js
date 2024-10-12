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
    },
    selectByIds(cartIds) {
        //localhost:8080/cart/ids?cartIds=1&cartIds=2...
        let params = "?cartIds=";
        for (let i = 0; i < cartIds.length; i++) {
            params += cartIds[i];
            if (i != cartIds.length - 1) {
                params += '&cartIds='
            }
        }
        return service.get(`/cart/ids${params}`);
    }
}
export default cartApi;