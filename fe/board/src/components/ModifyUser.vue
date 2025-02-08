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

      <b-form-group label="비밀번호">
        <b-form-input
          v-model="form.password"
          type="password"
          required
          placeholder="비밀번호를 입력하세요"
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

      <b-form-group label="이메일">
        <b-form-input
          v-model="form.email"
          type="email"
          required
          placeholder="이메일을 입력하세요"
        ></b-form-input>
      </b-form-group>

      <b-form-group label="전화번호">
        <b-form-input
          v-model="form.phoneNumber"
          required
          placeholder="전화번호를 입력하세요"
        ></b-form-input>
      </b-form-group>

      <div class="text-right mt-3">
        <b-button type="submit" variant="primary" class="mr-2">
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
        koName: '',
        enName: '',
        email: '',
        phoneNumber: ''
      }
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
    }
  },
  watch: {
    userInfo: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal }
        }
      }
    }
  },
  methods: {
    ...mapActions(['updateUserInfo']),
    
    async handleSubmit() {
      try {
        await axios.post('/member/modify', {
          password: this.form.password,
          koName: this.form.koName,
          enName: this.form.enName,
          email: this.form.email,
          phoneNumber: this.form.phoneNumber
        })
        
        // Vuex store 업데이트
        await this.updateUserInfo(this.form.koName)
        
        // 부모 컴포넌트에 성공 이벤트 발생
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
        memberId: '',
        password: '',
        koName: '',
        enName: '',
        email: '',
        phoneNumber: ''
      }
    }
  }
}
</script>
