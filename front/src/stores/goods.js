import { defineStore } from "pinia";
import { computed, ref } from "vue";


export const useGoodsStore = defineStore("goods", () => {
    //定义state - 需要分享的数据都要定义为state
    const goods = ref(null);

    //返回goods
    const goodsInfo = computed(() => {
        return goods.value;
    })

    //更新goods
    function updateGoods(g) {
        goods.value = g;
    }

    //重置goods
    function $reset() {
        goods.value = null;
    }

    return { goods, goodsInfo, updateGoods, $reset};
}, {
    persist: {
        key: 'goods',
        storage: sessionStorage,
        paths: ['goods']
    }
})
