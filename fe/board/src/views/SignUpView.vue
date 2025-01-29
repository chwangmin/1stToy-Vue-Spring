<template>
  <b-container class="mt-5">
    <b-row class="justify-content-center">
      <b-col cols="12" md="6">
        <b-card>
          <b-card-header class="text-center">
            <h3>회원가입</h3>
          </b-card-header>

          <b-card-body>
            <b-form @submit.prevent="handleSignUp">
              <b-form-group
                label="이메일"
                label-for="email"
                :state="emailState"
                :invalid-feedback="emailFeedback"
              >
                <b-form-input
                  id="email"
                  v-model="form.email"
                  type="email"
                  placeholder="이메일을 입력하세요"
                  required
                  :state="emailState"
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="비밀번호"
                label-for="password"
                class="mt-3"
                :state="passwordState"
                :invalid-feedback="passwordFeedback"
              >
                <b-form-input
                  id="password"
                  v-model="form.password"
                  type="password"
                  placeholder="비밀번호를 입력하세요"
                  required
                  :state="passwordState"
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="비밀번호 확인"
                label-for="passwordConfirm"
                class="mt-3"
                :state="passwordConfirmState"
                :invalid-feedback="passwordConfirmFeedback"
              >
                <b-form-input
                  id="passwordConfirm"
                  v-model="form.passwordConfirm"
                  type="password"
                  placeholder="비밀번호를 다시 입력하세요"
                  required
                  :state="passwordConfirmState"
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="이름"
                label-for="name"
                class="mt-3"
              >
                <b-form-input
                  id="name"
                  v-model="form.name"
                  placeholder="이름을 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-button
                type="submit"
                variant="primary"
                class="w-100 mt-4"
                :disabled="!isFormValid"
              >
                회원가입
              </b-button>
            </b-form>

            <div class="text-center mt-3">
              <p class="mb-0">
                이미 계정이 있으신가요? 
                <b-link to="/login">로그인</b-link>
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
  name: 'SignUpView',
  data() {
    return {
      form: {
        email: '',
        password: '',
        passwordConfirm: '',
        name: ''
      }
    }
  },
  computed: {
    emailState() {
      if (this.form.email.length === 0) return null
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)
    },
    emailFeedback() {
      return '올바른 이메일 형식이 아닙니다.'
    },
    passwordState() {
      if (this.form.password.length === 0) return null
      return this.form.password.length >= 8
    },
    passwordFeedback() {
      return '비밀번호는 8자 이상이어야 합니다.'
    },
    passwordConfirmState() {
      if (this.form.passwordConfirm.length === 0) return null
      return this.form.password === this.form.passwordConfirm
    },
    passwordConfirmFeedback() {
      return '비밀번호가 일치하지 않습니다.'
    },
    isFormValid() {
      return this.emailState && 
             this.passwordState && 
             this.passwordConfirmState && 
             this.form.name.length > 0
    }
  },
  methods: {
    async handleSignUp() {
      if (!this.isFormValid) return

      try {
        // 여기에 회원가입 API 호출 로직 구현
        console.log('회원가입 시도:', {
          email: this.form.email,
          password: this.form.password,
          name: this.form.name
        })
        
        // 성공 시 로그인 페이지로 이동
        this.$router.push('/login')
      } catch (error) {
        console.error('회원가입 실패:', error)
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
