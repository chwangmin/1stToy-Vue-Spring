import axios from 'axios'
import store from '../store/store'

// axios 인스턴스 생성
const instance = axios.create({
  baseURL: 'http://localhost:8021',
  timeout: 5000
})

// 요청 인터셉터
instance.interceptors.request.use(
  (config) => {
    const token = store.getters.getAccessToken
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터
instance.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    
    // 토큰이 만료되었을 때의 처리
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      
      try {
        // 리프레시 토큰으로 새로운 액세스 토큰 발급 요청
        const response = await instance.post('/auth/refresh')
        const newToken = response.data.accessToken
        
        // 새로운 토큰을 Vuex store에 저장
        store.commit('setAccessToken', newToken)
        
        // 원래 요청 재시도
        originalRequest.headers.Authorization = `Bearer ${newToken}`
        return instance(originalRequest)
      } catch (refreshError) {
        // 리프레시 토큰도 만료된 경우 로그아웃
        store.commit('logout')
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  }
)

export default instance
