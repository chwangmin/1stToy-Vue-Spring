<template>
  <b-navbar toggleable="lg" type="light" class="navigation">
    <b-navbar-brand href="#" @click.prevent="goToMain" class="logo">
      <img src="../assets/logo.png" alt="로고" class="logo-image">
    </b-navbar-brand>

    <b-navbar-nav class="ml-auto d-flex flex-row align-items-center">
      <template v-if="isAuthenticated">
        <span class="mr-3 user-name">{{ userName }}님</span>
        <b-button 
          variant="outline-danger" 
          size="sm"
          @click="handleLogout"
        >로그아웃</b-button>
      </template>
      <template v-else>
        <b-button variant="primary" size="sm" @click="goToLogin">로그인</b-button>
      </template>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from '../api/interceptor'

export default {
  name: 'ToolBar',
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    userName() {
      return this.currentUser && this.currentUser.name ? this.currentUser.name : '사용자'
    }
  },
  methods: {
    goToLogin() {
      this.$router.push('/login')
    },
    goToMain() {
      this.$router.push('/')
    },
    async handleLogout() {
      try {
        await this.$store.dispatch('logout')
        this.$router.push('/login')
      } catch (error) {
        console.error('로그아웃 실패:', error)
      }
    }
  }
}
</script>

<style scoped>
.navigation {
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 0.5rem 2rem;
}

.logo {
  height: 40px;
  cursor: pointer;
}

.logo-image {
  height: 100%;
  object-fit: contain;
}

.user-name {
  white-space: nowrap;
}

/* 모바일 화면에서의 스타일 */
@media (max-width: 768px) {
  .navigation {
    padding: 0.5rem 1rem;
  }
  
  .user-name {
    font-size: 0.9rem;
  }
}
</style>
