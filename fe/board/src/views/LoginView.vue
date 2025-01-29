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
                label="이메일"
                label-for="email"
              >
                <b-form-input
                  id="email"
                  v-model="email"
                  type="email"
                  placeholder="이메일을 입력하세요"
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
              <p class="mb-0">
                계정이 없으신가요? 
                <b-link to="/signup">회원가입</b-link>
              </p>
            </div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      email: '',
      password: ''
    }
  },
  methods: {
    async handleLogin() {
      try {
        await this.$store.dispatch('login', {
          email: this.email,
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
