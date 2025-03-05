<template>
  <b-container class="mt-5 mb-5">
    <b-row class="justify-content-center">
      <b-col cols="12" md="6">
        <b-card>
          <b-card-header class="text-center">
            <h3>회원가입</h3>
          </b-card-header>

          <b-card-body>
            <b-form @submit.prevent="handleSignUp">
              <b-form-group
                label="아이디"
                label-for="username"
                :state="usernameState"
                :invalid-feedback="usernameFeedback"
              >
                <b-form-input
                  id="username"
                  v-model="form.username"
                  type="text"
                  placeholder="아이디를 입력하세요"
                  required
                  :state="usernameState"
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
                <b-form-text>
                  비밀번호는 다음을 포함해야 합니다:
                  <ul class="mb-0">
                    <li>최소 8자 이상</li>
                    <li>대문자 1개 이상</li>
                    <li>소문자 1개 이상</li>
                    <li>숫자 1개 이상</li>
                    <li>특수문자 1개 이상 (@$!%*?&#.)</li>
                  </ul>
                </b-form-text>
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
                label="한국 이름"
                label-for="koreanName"
                class="mt-3"
              >
                <b-form-input
                  id="koreanName"
                  v-model="form.koreanName"
                  placeholder="한국 이름을 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="영어 이름"
                label-for="englishName"
                class="mt-3"
              >
                <b-form-input
                  id="englishName"
                  v-model="form.englishName"
                  placeholder="영어 이름을 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="이메일"
                label-for="email"
                class="mt-3"
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
                label="생년월일"
                label-for="birthdate"
                class="mt-3"
              >
                <b-form-input
                  id="birthdate"
                  v-model="form.birthdate"
                  type="date"
                  required
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="전화번호"
                label-for="phone"
                class="mt-3"
                :state="phoneState"
                :invalid-feedback="phoneFeedback"
              >
                <b-form-input
                  id="phone"
                  v-model="form.phone"
                  type="tel"
                  placeholder="숫자만 입력하세요 (예: 010-1234-5678)"
                  required
                  :state="phoneState"
                  @input="validatePhone"
                  maxlength="13"
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
        username: '',
        password: '',
        passwordConfirm: '',
        koreanName: '',
        englishName: '',
        email: '',
        birthdate: '',
        phone: ''
      }
    }
  },
  computed: {
    usernameState() {
      if (this.form.username.length === 0) return null
      return this.form.username.length >= 4
    },
    usernameFeedback() {
      return '아이디는 4자 이상이어야 합니다.'
    },
    passwordState() {
      if (this.form.password.length === 0) return null
      
      const hasUpperCase = /[A-Z]/.test(this.form.password)
      const hasLowerCase = /[a-z]/.test(this.form.password)
      const hasNumbers = /[0-9]/.test(this.form.password)
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(this.form.password)
      const isLongEnough = this.form.password.length >= 8
      
      return hasUpperCase && hasLowerCase && hasNumbers && hasSpecialChar && isLongEnough
    },
    passwordFeedback() {
      if (this.form.password.length === 0) return '비밀번호를 입력해주세요.'
      if (this.form.password.length < 8) return '비밀번호는 8자 이상이어야 합니다.'
      if (!/[A-Z]/.test(this.form.password)) return '대문자가 하나 이상 필요합니다.'
      if (!/[a-z]/.test(this.form.password)) return '소문자가 하나 이상 필요합니다.'
      if (!/[0-9]/.test(this.form.password)) return '숫자가 하나 이상 필요합니다.'
      if (!/[!@#$%^&*(),.?":{}|<>]/.test(this.form.password)) return '특수문자가 하나 이상 필요합니다.'
      return '비밀번호가 유효합니다.'
    },
    passwordConfirmState() {
      if (this.form.passwordConfirm.length === 0) return null
      return this.form.password === this.form.passwordConfirm
    },
    passwordConfirmFeedback() {
      return '비밀번호가 일치하지 않습니다.'
    },
    emailState() {
      if (this.form.email.length === 0) return null
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)
    },
    emailFeedback() {
      return '올바른 이메일 형식이 아닙니다.'
    },
    phoneState() {
      if (this.form.phone.length === 0) return null
      // 하이픈을 포함한 전화번호 형식 검사 (010-0000-0000)
      return /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/.test(this.form.phone)
    },
    phoneFeedback() {
      return '올바른 전화번호 형식이 아닙니다. (010-0000-0000)'
    },
    isFormValid() {
      return this.usernameState && 
             this.passwordState && 
             this.passwordConfirmState && 
             this.form.koreanName.length > 0 &&
             this.form.englishName.length > 0 &&
             this.emailState &&
             this.form.birthdate &&
             this.phoneState
    }
  },
  methods: {
    validatePhone() {
      // 숫자만 입력되도록 필터링
      let numbers = this.form.phone.replace(/[^0-9]/g, '')
      
      // 11자리로 제한
      if (numbers.length > 11) {
        numbers = numbers.slice(0, 11)
      }
      
      // 전화번호 형식으로 변환 (010-0000-0000)
      if (numbers.length > 0) {
        let formatted = ''
        if (numbers.length > 3) {
          formatted = numbers.substring(0, 3) + '-'
          if (numbers.length > 7) {
            formatted += numbers.substring(3, 7) + '-' + numbers.substring(7)
          } else {
            formatted += numbers.substring(3)
          }
        } else {
          formatted = numbers
        }
        this.form.phone = formatted
      } else {
        this.form.phone = numbers
      }
    },
    async handleSignUp() {
      if (!this.isFormValid) return

      try {
        // 전화번호에서 하이픈 제거하고 서버에 전송
        const phoneWithoutHyphens = this.form.phone.replace(/-/g, '')
        
        console.log('회원가입 시도:', {
          username: this.form.username,
          password: this.form.password,
          koreanName: this.form.koreanName,
          englishName: this.form.englishName,
          email: this.form.email,
          birthdate: this.form.birthdate,
          phone: phoneWithoutHyphens
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
