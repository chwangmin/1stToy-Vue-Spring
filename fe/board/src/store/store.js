import Vue from 'vue'
import Vuex from 'vuex'
import axios from '../api/interceptor'  // interceptor에서 설정한 axios 인스턴스 사용
import { jwtDecode } from 'jwt-decode'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    accessToken: null,
    user: null
  },
  mutations: {
    setAccessToken(state, token) {
      // 토큰 저장 시 Bearer 형식으로 저장
      const tokenValue = token.replace('Bearer ', '').trim()
      state.accessToken = `Bearer ${tokenValue}`
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
        const response = await axios.post('/auth/login', {
          memberId: credentials.username,
          password: credentials.password
        })
        
        const authToken = response.headers['authorization']
        if (authToken) {
          // Bearer 형식으로 저장
          const tokenValue = authToken.replace('Bearer ', '').trim()
          commit('setAccessToken', `Bearer ${tokenValue}`)
          
          // 토큰에서 사용자 정보 추출
          const decoded = jwtDecode(tokenValue)
          if (decoded) {
            commit('setUser', {
              id: decoded.sub,
              name: decoded.name
            })
          }
        }
        
        return response
      } catch (error) {
        console.error('로그인 에러:', error)
        throw error
      }
    },
    async logout({ commit }) {
      try {
        await axios.post('/auth/logout')
      } finally {
        commit('logout')
      }
    }
  },
  getters: {
    isAuthenticated: state => {
      return !!state.accessToken && !!state.user
    },
    currentUser: state => state.user,
    getAccessToken: state => state.accessToken
  }
})
