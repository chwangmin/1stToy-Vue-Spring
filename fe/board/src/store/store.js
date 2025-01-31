import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    accessToken: null,
    user: null
  },
  mutations: {
    setAccessToken(state, token) {
      state.accessToken = token
    },
    setUser(state, user) {
      state.user = user
    },
    logout(state) {
      state.accessToken = null
      state.user = null
    }
  },
  actions: {
    async login({ commit }, credentials) {
      try {
        const response = await Vue.axios.post('/auth/login', credentials)
        const { accessToken, user } = response.data
        
        commit('setAccessToken', accessToken)
        commit('setUser', user)
        
        return response
      } catch (error) {
        throw error
      }
    },
    async logout({ commit }) {
      try {
        await Vue.axios.post('/auth/logout')
      } finally {
        commit('logout')
      }
    }
  },
  getters: {
    isAuthenticated: state => !!state.accessToken,
    currentUser: state => state.user,
    getAccessToken: state => state.accessToken
  }
})
