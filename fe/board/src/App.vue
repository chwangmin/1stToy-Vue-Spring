<template>
  <div id="app">
    <tool-bar></tool-bar>
    <router-view></router-view>
  </div>
</template>

<script>
import router from './routes/routes'
import ToolBar from './components/ToolBar.vue'
import axios from './api/interceptor'
import { jwtDecode } from 'jwt-decode'

export default {
  name: 'App',
  components: {
    ToolBar
  },
  router,
  async created() {
    try {
      // 페이지 로드 시 토큰 갱신 시도
      const response = await axios.post('/auth/refresh', {}, {
        withCredentials: true
      })
      
      // 헤더에서 토큰 가져오기
      const authToken = response.headers['authorization']
    
      if (authToken) {
        // Bearer 형식으로 저장
        const tokenValue = authToken.replace('Bearer ', '').trim()
        this.$store.commit('setAccessToken', `Bearer ${tokenValue}`)
        
        // 토큰에서 사용자 정보 추출
        const decoded = jwtDecode(tokenValue)
        if (decoded) {
          this.$store.commit('setUser', {
            id: decoded.sub,
            name: decoded.name
          })
        }
      }
    } catch (error) {
      console.error('토큰 갱신 실패:', error)
      this.$store.commit('logout')
    }
  }
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
}

#app {
  font-family: Arial, sans-serif;
}
</style>
