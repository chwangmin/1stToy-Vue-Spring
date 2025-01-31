<template>
  <b-navbar toggleable="lg" type="light" class="navigation">
    <b-navbar-brand href="#" @click.prevent="goToMain" class="logo">
      <img src="../assets/logo.png" alt="로고" class="logo-image">
    </b-navbar-brand>

    <b-navbar-nav class="ml-auto">
      <b-button variant="primary" @click="goToLogin">로그인</b-button>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>
export default {
  name: 'ToolBar',
  methods: {
    goToLogin() {
      this.$router.push('/login')
    },
    goToMain() {
      this.$router.push('/')
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
</style>
