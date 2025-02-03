<template>
  <b-container class="mt-4">
    <!-- 검색과 정렬 기능 -->
    <b-row class="mb-3">
      <b-col sm="12" md="6" lg="4">
        <b-input-group>
          <b-form-input
            v-model="searchKeyword"
            placeholder="검색어를 입력하세요"
            @keyup.enter="search"
          ></b-form-input>
          <b-input-group-append>
            <b-button variant="outline-secondary" @click="search">검색</b-button>
          </b-input-group-append>
        </b-input-group>
      </b-col>
      <!-- 정렬 드롭다운 추가 -->
      <b-col sm="12" md="6" lg="4">
        <b-form-select
          v-model="selectedSort"
          :options="sortOptions"
          @change="handleSortChange"
        ></b-form-select>
      </b-col>
    </b-row>

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

        <!-- 페이징 정보 표시 수정 -->
        <div class="mt-3 d-flex justify-content-center">
          <b-pagination
            v-model="currentPage"
            :total-rows="maxBoardNum"
            :per-page="1"
            align="center"
            @change="handlePageChange"
            class="mb-0"
            first-text="«"
            prev-text="‹"
            next-text="›"
            last-text="»"
            pills
          ></b-pagination>
        </div>

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
        id: '',
        title: '',
        content: '',
        authorID: '',
        fileName: '',
        filePath: '',
        views: 0,
        createdDate: '',
        modifiedDate: ''
      },
      searchKeyword: '', // 검색어 상태 추가
      currentPage: 1,  // 항상 1로 시작
      maxBoardNum: 0,
      selectedSort: 'CREATED_DESC',
      sortOptions: [
        { value: 'CREATED_DESC', text: '최신순' },
        { value: 'CREATED_ASC', text: '오래된순' },
        { value: 'VIEWS_DESC', text: '조회수 높은순' },
        { value: 'VIEWS_ASC', text: '조회수 낮은순' }
      ]
    }
  },
  created() {
    
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },
    async fetchPosts() {
      try {
        this.isLoading = true
        const response = await boardAPI.getPosts({
          keyword: this.searchKeyword,
          page: this.currentPage - 1,
          sort: this.selectedSort
        })
        this.posts = response.boards
        this.currentPage = response.currentPage
        this.maxBoardNum = response.maxBoardNum
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
        // 1. 조회수 증가 API 호출
        await boardAPI.increaseViews(post.id)
        
        // 2. 상세 정보 조회
        const detailResponse = await boardAPI.getPost(post.id)
        this.selectedPost = detailResponse.boardDto
        this.showModal = true
        
        // 3. 전체 게시글 목록을 다시 불러와서 최신 조회수 반영
        await this.fetchPosts()
      } catch (error) {
        console.error('게시글 조회 실패:', error)
        this.$bvToast.toast('게시글을 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    writePost() {
      this.$router.push('/write')
    },
    onModalHidden() {
      this.selectedPost = {
        id: '',
        title: '',
        content: '',
        authorID: '',
        fileName: '',
        filePath: '',
        views: 0,
        createdDate: '',
        modifiedDate: ''
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
        // 게시글 삭제 API 호출
        await boardAPI.deletePost(postId)
        
        // 삭제 성공 후 전체 게시글 목록을 다시 불러옴
        await this.fetchPosts()
        
        this.$bvToast.toast('게시글이 삭제되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })
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
        
        // JSON 파일 확장자 검증
        if (!file.name.toLowerCase().endsWith('.json')) {
          throw new Error('JSON 파일만 업로드 가능합니다.')
        }

        // 파일 업로드 API 호출
        await boardAPI.importPosts(file)
        
        // 성공 메시지 표시
        this.$bvToast.toast('데이터가 성공적으로 업로드되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })

        // 게시글 목록 새로고침
        await this.fetchPosts()
      } catch (error) {
        console.error('파일 업로드 실패:', error)
        this.$bvToast.toast(
          error.message || '파일 업로드에 실패했습니다.', 
          {
            title: '에러',
            variant: 'danger',
            solid: true
          }
        )
      } finally {
        this.isLoading = false
        event.target.value = '' // 파일 입력 초기화
      }
    },
    async saveEdit({ id, boardData, file }) {
      try {
        // 게시글 수정 API 호출
        await boardAPI.updatePost(id, boardData, file)
        
        // 수정 성공 후 전체 게시글 목록을 다시 불러옴
        await this.fetchPosts()
        
        this.$bvToast.toast('게시글이 수정되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })
      } catch (error) {
        console.error('게시글 수정 실패:', error)
        this.$bvToast.toast('게시글 수정에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    // 검색 메소드 수정
    async search() {
      try {
        this.isLoading = true // 로딩 상태 추가
        const response = await boardAPI.getPosts({
          keyword: this.searchKeyword
        })
        this.posts = response.boards
        this.currentPage = response.currentPage
        this.maxBoardNum = response.maxBoardNum
      } catch (error) {
        console.error('검색 실패:', error)
        this.$bvToast.toast('검색에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      } finally {
        this.isLoading = false // 로딩 상태 해제
      }
    },
    // 페이지 변경 핸들러 수정
    async handlePageChange(page) {
      try {
        this.isLoading = true
        const response = await boardAPI.getPosts({
          keyword: this.searchKeyword,
          page: page - 1
        })
        
        // 데이터 업데이트
        this.posts = response.boards
        this.currentPage = page
        this.maxBoardNum = response.maxBoardNum
        
        // URL 업데이트 (새로고침 없이)
        const query = { ...this.$route.query, page: page }
        this.$router.push({
          path: this.$route.path,
          query: query
        }).catch(() => {})
        
      } catch (error) {
        console.error('페이지 로딩 실패:', error)
        this.$bvToast.toast('페이지를 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      } finally {
        this.isLoading = false
      }
    },
    // 정렬 변경 핸들러 추가
    async handleSortChange() {
      const query = { 
        ...this.$route.query, 
        sort: this.selectedSort,
        page: 1 // 정렬 변경 시 첫 페이지로 이동
      }
      this.$router.push({ query }).catch(() => {})
      await this.fetchPosts()
    }
  },
  // URL 파라미터 감시 수정
  watch: {
    '$route.query': {
      immediate: true,
      deep: true,
      handler(query) {
        if (query.page) {
          this.currentPage = parseInt(query.page)
        }
        if (query.sort) {
          this.selectedSort = query.sort
        }
        this.fetchPosts()
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

/* 페이지네이션 스타일 추가 */
.pagination {
  margin-bottom: 0;
}

.page-link {
  padding: 0.5rem 0.75rem;
  margin: 0 2px;
  border-radius: 4px;
}

.page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}
</style>
