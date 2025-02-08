<template>
  <b-container class="mt-5">
    <b-row class="justify-content-center">
      <b-col cols="12" md="6">
        <b-card>
          <b-card-header class="text-center">
            <h3>비밀번호 찾기</h3>
          </b-card-header>

          <b-card-body>
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

              <b-button
                type="submit"
                variant="primary"
                class="w-100 mt-3"
                :disabled="isLoading"
              >
                {{ isLoading ? '처리중...' : '비밀번호 찾기' }}
              </b-button>
            </b-form>

            <div class="text-center mt-3">
              <b-link to="/login">로그인으로 돌아가기</b-link>
            </div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import { memberAPI } from '@/api/api'

export default {
  name: 'FindPassword',
  data() {
    return {
      email: '',
      isLoading: false
    }
  },
  methods: {
    async handleFindPassword() {
      this.isLoading = true
      try {
        await memberAPI.findPassword(this.email)
        this.$bvToast.toast('이메일로 비밀번호 재설정 링크가 전송되었습니다.', {
          title: '알림',
          variant: 'success',
          solid: true
        })
        this.$router.push('/login')
      } catch (error) {
        const errorMessage = error.response?.data?.errorMessage || '비밀번호 찾기에 실패했습니다.'
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
