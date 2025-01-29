<template>
  <div id="app">
    <tool-bar></tool-bar>
    <router-view></router-view>
  </div>
</template>

<script>
import router from './routes/routes'
import ToolBar from './components/ToolBar.vue'

export default {
  name: 'App',
  components: {
    ToolBar
  },
  router
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
}

#app {
  font-family: Arial, sans-serif;
}
</style>
