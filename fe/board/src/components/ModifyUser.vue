<template>
  <b-modal
    v-model="showModal"
    title="회원정보 수정"
    @hidden="onHidden"
    hide-footer
  >
    <b-form @submit.prevent="handleSubmit">
      <b-form-group label="아이디">
        <b-form-input
          v-model="form.memberId"
          readonly
          disabled
        ></b-form-input>
      </b-form-group>

      <b-form-group 
        label="비밀번호"
        :state="passwordState"
        :invalid-feedback="passwordFeedback"
      >
        <div class="password-input-group">
          <b-form-input
            v-model="form.password"
            :type="showPassword ? 'text' : 'password'"
            required
            placeholder="비밀번호를 입력하세요"
            :state="passwordState"
          ></b-form-input>
          <b-button 
            @click="showPassword = !showPassword" 
            class="show-password-btn"
            variant="outline-secondary"
          >
            👁
          </b-button>
        </div>
        <b-form-text>
          비밀번호는 다음을 포함해야 합니다:
          <ul class="mb-0">
            <li>최소 8자 이상</li>
            <li>대문자 1개 이상</li>
            <li>소문자 1개 이상</li>
            <li>숫자 1개 이상</li>
            <li>특수문자 1개 이상 (!@#$%^&amp;*(),.?&quot;:{}|&lt;&gt;)</li>
          </ul>
        </b-form-text>
      </b-form-group>

      <b-form-group
        label="비밀번호 확인"
        :state="passwordConfirmState"
        :invalid-feedback="passwordConfirmFeedback"
      >
        <b-form-input
          v-model="form.passwordConfirm"
          type="password"
          required
          placeholder="비밀번호를 다시 입력하세요"
          :state="passwordConfirmState"
        ></b-form-input>
      </b-form-group>

      <b-form-group label="한글 이름">
        <b-form-input
          v-model="form.koName"
          required
          placeholder="한글 이름을 입력하세요"
        ></b-form-input>
      </b-form-group>

      <b-form-group label="영문 이름">
        <b-form-input
          v-model="form.enName"
          required
          placeholder="영문 이름을 입력하세요"
        ></b-form-input>
      </b-form-group>

      <b-form-group 
        label="이메일"
        :state="emailState"
        :invalid-feedback="emailFeedback"
      >
        <b-form-input
          v-model="form.email"
          type="email"
          required
          placeholder="이메일을 입력하세요"
          :state="emailState"
        ></b-form-input>
      </b-form-group>

      <b-form-group 
        label="전화번호"
        :state="phoneState"
        :invalid-feedback="phoneFeedback"
      >
        <b-form-input
          v-model="form.phoneNumber"
          required
          placeholder="전화번호를 입력하세요"
          :state="phoneState"
          @input="validatePhone"
        ></b-form-input>
      </b-form-group>

      <div class="text-right mt-3">
        <b-button 
          type="submit" 
          variant="primary" 
          class="mr-2"
          :disabled="!emailState || !phoneState"
        >
          수정하기
        </b-button>
        <b-button variant="secondary" @click="closeModal">
          취소
        </b-button>
      </div>
    </b-form>
  </b-modal>
</template>

<script>
import { mapActions } from 'vuex'
import axios from '../api/interceptor'
import { memberAPI } from '../api/api'

export default {
  name: 'ModifyUser',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    userInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      form: {
        memberId: '',
        password: '',
        passwordConfirm: '',
        koName: '',
        enName: '',
        email: '',
        phoneNumber: ''
      },
      showPassword: false
    }
  },
  computed: {
    showModal: {
      get() {
        return this.show
      },
      set(value) {
        this.$emit('update:show', value)
      }
    },
    passwordState() {
      if (this.form.password.length === 0) return null
      
      const hasUpperCase = /[A-Z]/.test(this.form.password)
      const hasLowerCase = /[a-z]/.test(this.form.password)
      const hasNumbers = /[0-9]/.test(this.form.password)
      const hasSpecialChar = /[!@#$%^&amp;*(),.?&quot;:{}|&lt;&gt;]/.test(this.form.password)
      const isLongEnough = this.form.password.length >= 8
      
      return hasUpperCase && hasLowerCase && hasNumbers && hasSpecialChar && isLongEnough
    },
    passwordFeedback() {
      if (this.form.password.length === 0) return '비밀번호를 입력해주세요.'
      if (this.form.password.length < 8) return '비밀번호는 8자 이상이어야 합니다.'
      if (!/[A-Z]/.test(this.form.password)) return '대문자가 하나 이상 필요합니다.'
      if (!/[a-z]/.test(this.form.password)) return '소문자가 하나 이상 필요합니다.'
      if (!/[0-9]/.test(this.form.password)) return '숫자가 하나 이상 필요합니다.'
      if (!/[!@#$%^&amp;*(),.?&quot;:{}|&lt;&gt;]/.test(this.form.password)) return '특수문자가 하나 이상 필요합니다.'
      return '비밀번호가 유효합니다.'
    },
    passwordConfirmState() {
      if (this.form.passwordConfirm.length === 0) return null
      return this.form.password === this.form.passwordConfirm
    },
    passwordConfirmFeedback() {
      return '비밀번호가 일치하지 않습니다.'
    },
    phoneState() {
      if (this.form.phoneNumber.length === 0) return null
      return /^0\d{10}$/.test(this.form.phoneNumber)
    },
    phoneFeedback() {
      if (this.form.phoneNumber.length === 0) return '전화번호를 입력해주세요.'
      if (!this.form.phoneNumber.startsWith('0')) return '전화번호는 0으로 시작해야 합니다.'
      if (this.form.phoneNumber.length !== 11) return '전화번호는 정확히 11자리여야 합니다.'
      return '올바른 전화번호 형식이 아닙니다. (예: 01012345678)'
    },
    emailState() {
      if (this.form.email.length === 0) return null
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)
    },
    emailFeedback() {
      if (this.form.email.length === 0) return '이메일을 입력해주세요.'
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) return '올바른 이메일 형식이 아닙니다.'
      return '올바른 이메일 형식입니다.'
    },
    isFormValid() {
      return this.passwordState && 
             this.passwordConfirmState &&
             this.form.koName.length > 0 &&
             this.form.enName.length > 0 &&
             this.form.email.length > 0 &&
             this.phoneState
    }
  },
  watch: {
    userInfo: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.form = {
            memberId: newVal.memberId || '',
            password: '',
            passwordConfirm: '',
            koName: newVal.koName || '',
            enName: newVal.enName || '',
            email: newVal.email || '',
            phoneNumber: newVal.phoneNumber || ''
          }
        }
      }
    }
  },
  methods: {
    ...mapActions(['updateUserInfo']),
    
    validatePhone() {
      let filtered = this.form.phoneNumber.replace(/[^0-9]/g, '')
      if (filtered.length > 11) {
        filtered = filtered.slice(0, 11)
      }
      this.form.phoneNumber = filtered
    },
    async handleSubmit() {
      if (!this.isFormValid) {
        if (!this.emailState || !this.form.email) {
          this.$bvToast.toast('올바른 이메일 형식을 입력해주세요.', {
            title: '입력 오류',
            variant: 'danger',
            solid: true,
            autoHideDelay: 3000
          })
        } else if (!this.phoneState || !this.form.phoneNumber) {
          this.$bvToast.toast('올바른 전화번호 형식을 입력해주세요. (예: 01012345678)', {
            title: '입력 오류',
            variant: 'danger',
            solid: true,
            autoHideDelay: 3000
          })
        }
        return
      }

      try {
        const memberData = {
          password: this.form.password,
          koName: this.form.koName,
          enName: this.form.enName,
          email: this.form.email,
          phoneNumber: this.form.phoneNumber
        }
        
        await memberAPI.modifyMemberInfo(memberData)
        
        await this.updateUserInfo(this.form.koName)
        this.$emit('update-success', this.form.koName)
        this.closeModal()
      } catch (error) {
        console.error('회원정보 수정 실패:', error)
        this.$bvToast.toast('회원정보 수정에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    closeModal() {
      this.showModal = false
    },
    onHidden() {
      this.form = {
        memberId: this.userInfo.memberId || '',
        password: '',
        passwordConfirm: '',
        koName: this.userInfo.koName || '',
        enName: this.userInfo.enName || '',
        email: this.userInfo.email || '',
        phoneNumber: this.userInfo.phoneNumber || ''
      }
      this.showPassword = false
    }
  }
}
</script>

<style scoped>
.password-input-group {
  position: relative;
  display: flex;
}

.show-password-btn {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  border: none;
  background: transparent;
}
</style>
