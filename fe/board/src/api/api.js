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
  async login(credentials) {
    // 헤더 설정 제거 - interceptor에서 처리
    const response = await axios.post('/auth/login', credentials)
    return response
  },
  
  signup(userData) {
    return axios.post('/auth/signup', userData)
  }
}

export const boardAPI = {
  // 게시글 목록 조회 (검색 포함)
  getPosts: ({ keyword = '', page = 0, sort = 'CREATED_DESC' } = {}) => {
    const params = new URLSearchParams()
    if (keyword) params.append('keyword', keyword)
    params.append('page', page)
    params.append('sort', sort)
    
    return axios.get(`/board?${params.toString()}`)
      .then(response => response.data)
  },

  // 게시글 상세 조회
  async getPost(id) {
    try {
      const response = await axios.get(`/board/${id}`)
      return response.data
    } catch (error) {
      throw error
    }
  },

  // 게시글 작성
  createPost(formData) {
    return axios.post('/board', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 게시글 수정
  async updatePost(id, { title, content, authorID }, file) {
    const formData = new FormData()
    
    // modifyBoardRequest를 JSON 문자열로 변환하여 추가
    const modifyBoardRequest = {
      title,
      content,
      authorID
    }
    
    formData.append('modifyBoardRequest', 
      new Blob([JSON.stringify(modifyBoardRequest)], {
        type: 'application/json'
      })
    )
    
    // 파일이 있는 경우에만 추가
    if (file) {
      formData.append('file', file)
    }
    
    return axios.put(`/board/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 게시글 삭제
  deletePost(id) {
    return axios.delete(`/board/${id}`)
  },

  // JSON 파일 업로드를 통한 게시글 등록
  async importPosts(jsonFile) {
    const formData = new FormData()
    formData.append('file', jsonFile)
    
    return axios.post('/board/json', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 조회수 증가
  async increaseViews(id) {
    try {
      const response = await axios.post(`/board/${id}/view`)
      return response.data
    } catch (error) {
      throw error
    }
  },

  // 파일 다운로드 메소드 추가
  downloadFile: (filePath) => {
    return axios.get(`/board/download/${filePath}`, {
      responseType: 'blob'  // 파일 다운로드를 위한 responseType 설정
    })
  },
}
