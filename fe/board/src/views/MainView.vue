<template>
  <b-container class="mt-4">
    <b-row>
      <b-col>
        <b-card no-body>
          <b-table
            :items="posts"
            :fields="fields"
            striped
            hover
            responsive
            class="mb-0"
            @row-clicked="viewPost"
            :busy="isLoading"
          >
            <!-- 로딩 상태 표시 -->
            <template #table-busy>
              <div class="text-center my-2">
                <b-spinner class="align-middle"></b-spinner>
                <strong>Loading...</strong>
              </div>
            </template>
            
            <!-- 작성일 포맷팅을 위한 커스텀 필드 -->
            <template #cell(createdDate)="data">
              {{ formatDate(data.value) }}
            </template>
          </b-table>
        </b-card>

        <!-- 버튼 그룹 -->
        <div class="text-right mt-3 d-flex justify-content-between">
          <!-- 데이터 불러오기 버튼과 파일 입력 -->
          <div>
            <input
              type="file"
              ref="fileInput"
              accept=".json"
              style="display: none"
              @change="handleFileUpload"
            >
            <b-button 
              variant="success" 
              @click="$refs.fileInput.click()"
              :disabled="isLoading"
            >
              데이터 불러오기
            </b-button>
          </div>
          
          <!-- 글쓰기 버튼 -->
          <b-button variant="primary" @click="writePost">
            글쓰기
          </b-button>
        </div>
      </b-col>
    </b-row>

    <!-- 게시글 상세 모달 -->
    <board-detail
      :show.sync="showModal"
      :post="selectedPost"
      @download-file="downloadFile"
      @hidden="onModalHidden"
      @save-edit="saveEdit"
      @delete-post="deletePost"
    />
  </b-container>
</template>

<script>
import { boardAPI } from '../api/api'
import BoardDetail from '../components/BoardDetail.vue'

export default {
  name: 'MainView',
  components: {
    BoardDetail
  },
  data() {
    return {
      isLoading: false,
      fields: [
        { key: 'title', label: '제목', sortable: true },
        { key: 'authorID', label: '작성자', sortable: true },
        { key: 'createdDate', label: '작성일', sortable: true },
        { key: 'views', label: '조회수', sortable: true }
      ],
      posts: [],
      showModal: false,
      selectedPost: {
        title: '',
        authorID: '',
        createdDate: '',
        content: '',
        views: 0,
        fileName: '',
        filePath: ''
      }
    }
  },
  created() {
    this.fetchPosts()
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },
    async fetchPosts() {
      this.isLoading = true
      try {
        const response = await boardAPI.getPosts()
        this.posts = response
      } catch (error) {
        console.error('게시글 불러오기 실패:', error)
        this.$bvToast.toast('게시글을 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      } finally {
        this.isLoading = false
      }
    },
    async viewPost(post) {
      try {
        // 실제 API 연동 시에는 이 부분을 사용
        // const detailPost = await boardAPI.getPost(post.id)
        // this.selectedPost = detailPost

        // 임시로 더미 데이터 사용
        this.selectedPost = post
        this.showModal = true
      } catch (error) {
        console.error('게시글 조회 실패:', error)
        this.$bvToast.toast('게시글을 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    downloadFile(file) {
      console.log('파일 다운로드:', file.name)
    },
    writePost() {
      this.$router.push('/write')
    },
    onModalHidden() {
      this.selectedPost = {
        title: '',
        authorID: '',
        createdDate: '',
        content: '',
        views: 0,
        fileName: '',
        filePath: ''
      }
    },
    async editPost(post) {
      this.$router.push({
        path: '/write',
        query: { id: post.id }
      })
    },
    async deletePost(postId) {
      try {
        await boardAPI.deletePost(postId)
        this.$bvToast.toast('게시글이 삭제되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })
        this.fetchPosts() // 목록 새로고침
      } catch (error) {
        console.error('게시글 삭제 실패:', error)
        this.$bvToast.toast('게시글 삭제에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    async handleFileUpload(event) {
      const file = event.target.files[0]
      if (!file) return

      try {
        this.isLoading = true
        const reader = new FileReader()
        
        reader.onload = async (e) => {
          try {
            const jsonData = JSON.parse(e.target.result)
            
            // 데이터 형식 검증
            if (!Array.isArray(jsonData)) {
              throw new Error('올바른 데이터 형식이 아닙니다.')
            }

            // 각 항목의 필수 필드 검증
            const isValidData = jsonData.every(item => 
              item.title && 
              item.content && 
              item.authorID
            )

            if (!isValidData) {
              throw new Error('필수 필드가 누락된 데이터가 있습니다.')
            }

            // 서버로 데이터 전송
            await boardAPI.importPosts(jsonData)
            
            // 성공 메시지 표시
            this.$bvToast.toast('데이터가 성공적으로 업로드되었습니다.', {
              title: '성공',
              variant: 'success',
              solid: true
            })

            // 게시글 목록 새로고침
            this.fetchPosts()
          } catch (error) {
            console.error('JSON 파싱 실패:', error)
            this.$bvToast.toast('올바른 JSON 형식이 아닙니다.', {
              title: '에러',
              variant: 'danger',
              solid: true
            })
          }
        }

        reader.readAsText(file)
      } catch (error) {
        console.error('파일 업로드 실패:', error)
        this.$bvToast.toast('파일 업로드에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      } finally {
        this.isLoading = false
        // 파일 입력 초기화
        event.target.value = ''
      }
    },
    async saveEdit({ id, formData }) {
      try {
        await boardAPI.updatePost(id, formData)
        this.$bvToast.toast('게시글이 수정되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })
        this.fetchPosts() // 목록 새로고침
      } catch (error) {
        console.error('게시글 수정 실패:', error)
        this.$bvToast.toast('게시글 수정에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    }
  }
}
</script>

<style scoped>
.table-hover tbody tr {
  cursor: pointer;
}

/* 반응형 테이블을 위한 스타일 */
@media (max-width: 768px) {
  .table-responsive {
    font-size: 0.9rem;
  }
}
</style>
