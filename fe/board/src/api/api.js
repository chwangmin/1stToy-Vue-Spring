import axios from './interceptor'

// 더미 데이터
const dummyPosts = [
  {
    id: 1,
    title: '첫 번째 게시글입니다.',
    author: '홍길동',
    createdAt: '2024-01-29T10:00:00',
    views: 15
  },
  {
    id: 2,
    title: '안녕하세요! 두 번째 게시글입니다.',
    author: '김철수',
    createdAt: '2024-01-29T11:30:00',
    views: 22
  },
  {
    id: 3,
    title: '세 번째 게시글 작성합니다.',
    author: '이영희',
    createdAt: '2024-01-29T14:20:00',
    views: 8
  },
  {
    id: 4,
    title: '네 번째 게시글입니다.',
    author: '박민수',
    createdAt: '2024-01-29T15:45:00',
    views: 31
  },
  {
    id: 5,
    title: '다섯 번째 게시글입니다.',
    author: '정수진',
    createdAt: '2024-01-29T16:15:00',
    views: 12
  }
]

// API 함수들
export const authAPI = {
  login(credentials) {
    return axios.post('/auth/login', credentials)
  },
  signup(userData) {
    return axios.post('/auth/signup', userData)
  }
}

export const boardAPI = {
  // 게시글 목록 조회
  getPosts() {
    // return axios.get('/posts')
    return dummyPosts
  },

  // 게시글 상세 조회
  getPost(id) {
    return axios.get(`/posts/${id}`)
  },

  // 게시글 작성
  createPost(postData) {
    return axios.post('/posts', postData)
  }
}
