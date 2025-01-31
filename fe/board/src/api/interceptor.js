import axios from 'axios'
import store from '../store/store'

// axios 인스턴스 생성
const instance = axios.create({
  baseURL: 'http://localhost:8021',
  timeout: 5000,
  withCredentials: true  // 모든 요청에 쿠키 포함
})

// 요청 인터셉터
instance.interceptors.request.use(
  (config) => {
    const token = store.getters.getAccessToken
    if (token) {
      // 기존 헤더 제거
      delete config.headers.common['Authorization']
      delete config.headers['Authorization']
      
      // Bearer 토큰 형식으로 새로 설정
      const bearerToken = token.replace('Bearer ', '').trim()
      config.headers.Authorization = `Bearer ${bearerToken}`
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
    
    // /auth/refresh 요청이 실패한 경우는 재시도하지 않음
    if (error.response.status === 401 && !originalRequest._retry && !originalRequest.url.includes('/auth/refresh')) {
      originalRequest._retry = true
      
      try {
        const response = await instance.post('/auth/refresh', {}, {
          withCredentials: true
        })
        const newToken = response.data.accessToken
        
        const bearerToken = newToken.replace('Bearer ', '').trim()
        store.commit('setAccessToken', `Bearer ${bearerToken}`)
        
        delete originalRequest.headers.common['Authorization']
        delete originalRequest.headers['Authorization']
        originalRequest.headers.Authorization = `Bearer ${bearerToken}`
        
        return instance(originalRequest)
      } catch (refreshError) {
        store.commit('logout')
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  }
)

export default instance
