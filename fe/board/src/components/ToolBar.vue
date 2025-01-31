<template>
  <b-navbar toggleable="lg" type="light" class="navigation">
    <b-navbar-brand href="#" @click.prevent="goToMain" class="logo">
      <img src="../assets/logo.png" alt="로고" class="logo-image">
    </b-navbar-brand>

    <b-navbar-nav class="ml-auto d-flex flex-row align-items-center">
      <template v-if="isAuthenticated">
        <b-nav-item-dropdown right>
          <template #button-content>
            <span class="user-name">{{ userName }}님</span>
          </template>
          <b-dropdown-item @click="showModifyUser">회원정보 수정</b-dropdown-item>
          <b-dropdown-item @click="handleLogout">로그아웃</b-dropdown-item>
        </b-nav-item-dropdown>
      </template>
      <template v-else>
        <b-button variant="primary" size="sm" @click="goToLogin">로그인</b-button>
      </template>
    </b-navbar-nav>

    <!-- 회원정보 수정 모달 -->
    <modify-user
      v-if="showModifyModal"
      :show.sync="showModifyModal"
      :user-info="userInfo"
      @update-success="onUpdateSuccess"
    />
  </b-navbar>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import axios from '../api/interceptor'
import ModifyUser from './ModifyUser.vue'

export default {
  name: 'ToolBar',
  components: {
    ModifyUser
  },
  data() {
    return {
      showModifyModal: false,
      userInfo: null
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    userName() {
      return this.currentUser && this.currentUser.name ? this.currentUser.name : '사용자'
    }
  },
  methods: {
    ...mapActions(['updateUserInfo']),
    
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
    },
    async showModifyUser() {
      try {
        const response = await axios.get('/member/info')
        this.userInfo = response.data
        this.showModifyModal = true
      } catch (error) {
        console.error('회원정보 조회 실패:', error)
        this.$bvToast.toast('회원정보를 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    onUpdateSuccess(koName) {
      this.showModifyModal = false
      this.updateUserInfo(koName)
      this.$bvToast.toast('회원정보가 성공적으로 수정되었습니다.', {
        title: '성공',
        variant: 'success',
        solid: true
      })
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
  cursor: pointer;
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
