import axios from './interceptor'

// 더미 데이터
const dummyPosts = [
  {
    id: 1,
    title: '첫 번째 게시글입니다.',
    author: '홍길동',
    createdAt: '2024-01-29T10:00:00',
    views: 15,
    content: '첫 번째 게시글의 상세 내용입니다.\n\n여러 줄의 내용을 포함할 수 있습니다.',
    files: [
      { id: 1, name: '문서1.pdf' },
      { id: 2, name: '이미지1.jpg' }
    ]
  },
  {
    id: 2,
    title: '안녕하세요! 두 번째 게시글입니다.',
    author: '김철수',
    createdAt: '2024-01-29T11:30:00',
    views: 22,
    content: '두 번째 게시글의 상세 내용입니다.\n\n여러 줄의 내용을 포함할 수 있습니다.',
    files: [
      { id: 3, name: '문서2.pdf' },
      { id: 4, name: '이미지2.jpg' }
    ]
  },
  {
    id: 3,
    title: '세 번째 게시글 작성합니다.',
    author: '이영희',
    createdAt: '2024-01-29T14:20:00',
    views: 8,
    content: '세 번째 게시글의 상세 내용입니다.\n\n여러 줄의 내용을 포함할 수 있습니다.',
    files: [
      { id: 5, name: '문서3.pdf' },
      { id: 6, name: '이미지3.jpg' }
    ]
  },
  {
    id: 4,
    title: '네 번째 게시글입니다.',
    author: '박민수',
    createdAt: '2024-01-29T15:45:00',
    views: 31,
    content: '네 번째 게시글의 상세 내용입니다.\n\n여러 줄의 내용을 포함할 수 있습니다.',
    files: [
      { id: 7, name: '문서4.pdf' },
      { id: 8, name: '이미지4.jpg' }
    ]
  },
  {
    id: 5,
    title: '다섯 번째 게시글입니다.',
    author: '정수진',
    createdAt: '2024-01-29T16:15:00',
    views: 12,
    content: '다섯 번째 게시글의 상세 내용입니다.\n\n여러 줄의 내용을 포함할 수 있습니다.',
    files: [
      { id: 9, name: '문서5.pdf' },
      { id: 10, name: '이미지5.jpg' }
    ]
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
    // return axios.get(`/posts/${id}`)
    const post = dummyPosts.find(p => p.id === id)
    if (post) {
      return post
    }
    throw new Error('게시글을 찾을 수 없습니다.')
  },

  // 게시글 작성
  createPost(formData) {
    return axios.post('/posts', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 게시글 수정
  updatePost(id, formData) {
    return axios.put(`/posts/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 게시글 삭제
  deletePost(id) {
    // return axios.delete(`/posts/${id}`)
    const index = dummyPosts.findIndex(p => p.id === id)
    if (index !== -1) {
      dummyPosts.splice(index, 1)
      return true
    }
    throw new Error('게시글을 찾을 수 없습니다.')
  },

  // 게시글 데이터 일괄 업로드
  async importPosts(jsonData) {
    // return axios.post('/posts/import', jsonData)
    
    // 더미 데이터용 구현
    const newPosts = jsonData.map((post, index) => ({
      id: dummyPosts.length + index + 1,
      title: post.title,
      content: post.content,
      author: post.author,
      createdAt: new Date().toISOString(),
      views: 0,
      files: []
    }))

    dummyPosts.push(...newPosts)
    return true
  }
}
