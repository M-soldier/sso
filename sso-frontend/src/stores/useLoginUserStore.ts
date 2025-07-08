import { defineStore } from "pinia";
import { ref } from "vue";


export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.TokenUser>({
  });

  function setLoginUser(newLoginUser: API.TokenUser) {
    loginUser.value = newLoginUser;
  }

  return { loginUser, setLoginUser, };
});
