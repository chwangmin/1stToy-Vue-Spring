<template>
  <b-container class="mt-5">
    <b-row class="justify-content-center">
      <b-col cols="12" md="6">
        <b-card>
          <b-card-header class="text-center">
            <h3>로그인</h3>
          </b-card-header>

          <b-card-body>
            <b-form @submit.prevent="handleLogin">
              <b-form-group
                label="아이디"
                label-for="username"
              >
                <b-form-input
                  id="username"
                  v-model="username"
                  type="text"
                  placeholder="아이디를 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="비밀번호"
                label-for="password"
                class="mt-3"
              >
                <b-form-input
                  id="password"
                  v-model="password"
                  type="password"
                  placeholder="비밀번호를 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-button
                type="submit"
                variant="primary"
                class="w-100 mt-3"
              >
                로그인
              </b-button>
            </b-form>

            <div class="text-center mt-3">
              <p class="mb-2">
                계정이 없으신가요? 
                <b-link to="/signup">회원가입</b-link>
              </p>
              <p class="mb-0">
                비밀번호를 잊으셨나요?
                <b-link href="#" @click.prevent="showFindPasswordModal = true">비밀번호 찾기</b-link>
              </p>
            </div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>

    <!-- 비밀번호 찾기 모달 -->
    <b-modal
      v-model="showFindPasswordModal"
      title="비밀번호 찾기"
      hide-footer
      centered
      @hidden="resetModal"
    >
      <b-form @submit.prevent="handleFindPassword">
        <b-form-group
          label="이메일"
          label-for="email"
          description="가입시 등록한 이메일을 입력해주세요"
        >
          <b-form-input
            id="email"
            v-model="email"
            type="email"
            placeholder="이메일을 입력하세요"
            required
          ></b-form-input>
        </b-form-group>

        <div class="text-center mt-3">
          <b-button
            type="submit"
            variant="primary"
            class="mr-2"
            :disabled="isLoading"
          >
            {{ isLoading ? '처리중...' : '비밀번호 찾기' }}
          </b-button>
          <b-button
            variant="secondary"
            @click="showFindPasswordModal = false"
          >
            취소
          </b-button>
        </div>
      </b-form>
    </b-modal>
  </b-container>
</template>

<script>
import { memberAPI } from '../api/api'

export default {
  name: 'LoginView',
  data() {
    return {
      username: '',
      password: '',
      showFindPasswordModal: false,
      email: '',
      isLoading: false
    }
  },
  methods: {
    async handleLogin() {
      try {
        await this.$store.dispatch('login', {
          username: this.username,
          password: this.password
        })
        
        // 로그인 성공 시 메인 페이지로 이동
        this.$router.push('/')
      } catch (error) {
        console.error('로그인 실패:', error)
        this.$bvToast.toast('로그인에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    resetModal() {
      this.email = ''
      this.isLoading = false
    },
    async handleFindPassword() {
      if (this.isLoading) return  // 이미 처리 중이면 중복 요청 방지
      
      this.isLoading = true
      try {
        await memberAPI.findPassword(this.email)
        this.$bvToast.toast('이메일로 비밀번호 재설정 링크가 전송되었습니다.', {
          title: '알림',
          variant: 'success',
          solid: true
        })
        this.showFindPasswordModal = false
      } catch (error) {
        let errorMessage = '비밀번호 찾기에 실패했습니다.'
        if (error.response && error.response.data && error.response.data.errorMessage) {
          errorMessage = error.response.data.errorMessage
        }
        this.$bvToast.toast(errorMessage, {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      } finally {
        this.isLoading = false
      }
    }
  }
}
</script>

<style scoped>
.card {
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
}
</style>
