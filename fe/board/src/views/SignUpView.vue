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
                label-for="memberId"
                :state="memberIdState"
                :invalid-feedback="memberIdFeedback"
              >
                <b-form-input
                  id="memberId"
                  v-model="form.memberId"
                  type="text"
                  placeholder="아이디를 입력하세요"
                  required
                  :state="memberIdState"
                  autocomplete="username"
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="비밀번호"
                label-for="password"
                class="mt-3"
                :state="passwordState"
                :invalid-feedback="passwordFeedback"
              >
                <div class="password-input-group">
                  <b-form-input
                    id="password"
                    v-model="form.password"
                    :type="showPassword ? 'text' : 'password'"
                    placeholder="비밀번호를 입력하세요"
                    required
                    :state="passwordState"
                    autocomplete="new-password"
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
                    <li>특수문자 1개 이상 (!@#$%^&*(),.?":{}|<>)</li>
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

              <b-form-group label="한국 이름" label-for="koName" class="mt-3">
                <b-form-input
                  id="koName"
                  v-model="form.koName"
                  placeholder="한국 이름을 입력하세요"
                  required
                ></b-form-input>
              </b-form-group>

              <b-form-group label="영어 이름" label-for="enName" class="mt-3">
                <b-form-input
                  id="enName"
                  v-model="form.enName"
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

              <b-form-group label="생년월일" label-for="birthdate" class="mt-3">
                <b-form-input
                  id="birthdate"
                  v-model="form.birthdate"
                  type="date"
                  required
                  :max="getYesterday()"
                ></b-form-input>
              </b-form-group>

              <b-form-group
                label="전화번호"
                label-for="phoneNumber"
                class="mt-3"
                :state="phoneState"
                :invalid-feedback="phoneFeedback"
              >
                <b-form-input
                  id="phoneNumber"
                  v-model="form.phoneNumber"
                  type="tel"
                  placeholder="숫자만 입력하세요 (예: 01012345678)"
                  required
                  :state="phoneState"
                  @input="validatePhone"
                ></b-form-input>
              </b-form-group>

              <div class="text-right mt-3">
                <b-button
                  type="submit"
                  variant="primary"
                  class="w-100 mt-4"
                  :disabled="!emailState || !phoneState"
                >
                  회원가입
                </b-button>
              </div>
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
import { authAPI } from "../api/api";

export default {
  name: "SignUpView",
  data() {
    return {
      form: {
        memberId: "",
        password: "",
        passwordConfirm: "",
        koName: "",
        enName: "",
        email: "",
        birthdate: "",
        phoneNumber: "",
      },
      showPassword: false,
    };
  },
  computed: {
    memberIdState() {
      if (this.form.memberId.length === 0) return null;
      return this.form.memberId.length >= 4;
    },
    memberIdFeedback() {
      return "아이디는 4자 이상이어야 합니다.";
    },
    passwordState() {
      if (this.form.password.length === 0) return null;

      const hasUpperCase = /[A-Z]/.test(this.form.password);
      const hasLowerCase = /[a-z]/.test(this.form.password);
      const hasNumbers = /[0-9]/.test(this.form.password);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(this.form.password);
      const isLongEnough = this.form.password.length >= 8;

      return (
        hasUpperCase &&
        hasLowerCase &&
        hasNumbers &&
        hasSpecialChar &&
        isLongEnough
      );
    },
    passwordFeedback() {
      if (this.form.password.length === 0) return "비밀번호를 입력해주세요.";
      if (this.form.password.length < 8)
        return "비밀번호는 8자 이상이어야 합니다.";
      if (!/[A-Z]/.test(this.form.password))
        return "대문자가 하나 이상 필요합니다.";
      if (!/[a-z]/.test(this.form.password))
        return "소문자가 하나 이상 필요합니다.";
      if (!/[0-9]/.test(this.form.password))
        return "숫자가 하나 이상 필요합니다.";
      if (!/[!@#$%^&*(),.?":{}|<>]/.test(this.form.password))
        return "특수문자가 하나 이상 필요합니다.";
      return "비밀번호가 유효합니다.";
    },
    passwordConfirmState() {
      if (this.form.passwordConfirm.length === 0) return null;
      return this.form.password === this.form.passwordConfirm;
    },
    passwordConfirmFeedback() {
      return "비밀번호가 일치하지 않습니다.";
    },
    emailState() {
      if (this.form.email.length === 0) return null;
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email);
    },
    emailFeedback() {
      return "올바른 이메일 형식이 아닙니다.";
    },
    phoneState() {
      if (this.form.phoneNumber.length === 0) return null;
      return /^010\d{8}$/.test(this.form.phoneNumber);
    },
    phoneFeedback() {
      if (this.form.phoneNumber.length === 0) return "전화번호를 입력해주세요.";
      if (!this.form.phoneNumber.startsWith("010"))
        return "전화번호는 010으로 시작해야 합니다.";
      if (this.form.phoneNumber.length !== 11)
        return "전화번호는 11자리여야 합니다.";
      return "올바른 전화번호 형식입니다.";
    },
    isFormValid() {
      return (
        this.memberIdState &&
        this.passwordState &&
        this.passwordConfirmState &&
        this.form.koName.length > 0 &&
        this.form.enName.length > 0 &&
        this.emailState &&
        this.form.birthdate &&
        this.phoneState
      );
    },
  },
  methods: {
    getYesterday() {
      const today = new Date();
      const yesterday = new Date(today);
      yesterday.setDate(today.getDate() - 1);
      return yesterday.toISOString().split("T")[0];
    },
    validatePhone() {
      let value = this.form.phoneNumber.replace(/[^0-9]/g, "");

      if (value.length > 11) {
        value = value.slice(0, 11);
      }

      if (value.length >= 3 && !value.startsWith("010")) {
        value = "010" + value.slice(3);
      }

      this.form.phoneNumber = value;
    },
    async handleSignUp() {
      if (!this.isFormValid) {
        if (!this.emailState || !this.form.email) {
          this.$bvToast.toast("올바른 이메일 형식을 입력해주세요.", {
            title: "입력 오류",
            variant: "danger",
            solid: true,
            autoHideDelay: 3000,
          });
        } else if (!this.phoneState || !this.form.phoneNumber) {
          this.$bvToast.toast(
            "올바른 전화번호 형식을 입력해주세요. (예: 01012345678)",
            {
              title: "입력 오류",
              variant: "danger",
              solid: true,
              autoHideDelay: 3000,
            }
          );
        }
        return;
      }

      try {
        const userData = {
          memberId: this.form.memberId,
          password: this.form.password,
          koName: this.form.koName,
          enName: this.form.enName,
          email: this.form.email,
          birthdate: this.form.birthdate,
          phoneNumber: this.form.phoneNumber,
        };

        await authAPI.signup(userData);

        this.$router.push({
          path: "/login",
          query: {
            signupSuccess: "true",
            username: userData.memberId,
            koName: userData.koName,
          },
        });
      } catch (error) {
        console.error("회원가입 실패:", error);

        let errorMessage = "회원가입에 실패했습니다.";

        if (error.response && error.response.data) {
          const { errorMessage: serverMessage } = error.response.data;
          errorMessage = serverMessage;
        }

        this.$bvToast.toast(errorMessage, {
          title: "회원가입 실패",
          variant: "danger",
          solid: true,
          toaster: "b-toaster-top-right",
          appendToast: true,
          autoHideDelay: 3000,
        });
      }
    },
  },
};
</script>

<style scoped>
.card {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
}

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
