import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: localStorage.getItem("user") || null
  },
  mutations: {
    login(state, user) {
      localStorage.setItem("user", JSON.stringify(user));
      state.user = user;
    },
    logout(state) {
      // 移除token
      localStorage.removeItem("user");
      state.user = null;
    }
  },
  actions: {}
});
