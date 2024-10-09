import { defineStore } from "pinia";
import { computed, ref } from "vue";


export const useUserStore = defineStore("user", () => {
    //定义state - 需要分享的数据都要定义为state
    const user = ref(null);

    //返回user
    const userInfo = computed(() => {
        return user.value;
    })

    //更新user
    function updateUser(u) {
        user.value = u;
    }

    //重置state
    function $reset() {
        user.value = null;
    }

    return { user, userInfo, updateUser, $reset};
}, {
    persist: {
        key: 'user',
        storage: sessionStorage,
        paths: ['user']
    }
})
