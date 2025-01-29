import 'babel-polyfill'
import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import store from './store/store'
import router from './routes/routes'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
