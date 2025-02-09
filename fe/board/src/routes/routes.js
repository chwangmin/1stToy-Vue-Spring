import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from '../views/MainView.vue'
import LoginView from '../views/LoginView.vue'
import SignUpView from '../views/SignUpView.vue'
import WriteBoard from '../views/WriteBoard.vue'

Vue.use(VueRouter)

const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'main',
            component: MainView,
            props: { boardType: 'all' }
        },
        {
            path: '/open',
            name: 'openBoard',
            component: MainView,
            props: { boardType: 'open' }
        },
        {
            path: '/question',
            name: 'questionBoard',
            component: MainView,
            props: { boardType: 'question' }
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/signup',
            name: 'signup',
            component: SignUpView
        },
        {
            path: '/write',
            name: 'write',
            component: WriteBoard
        }
    ]
})

export default router