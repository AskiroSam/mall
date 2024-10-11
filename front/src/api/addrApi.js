import service from "@/api/index.js";
import qs from "qs";

const addrApi = {
    insert(addr) {
        return service.post("/addr/", addr);
    },
    delete(id) {
        return service.delete(`/addr/${id}`);
    },
    update(addr) {
        return service.put("/addr", addr);
    },
    selectByUser() {
        return service.get("/addr");
    },
    selectById(id) {
        return service.get(`/addr/${id}`);
    }
}
export default addrApi;