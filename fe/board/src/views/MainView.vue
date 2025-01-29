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
            <template #cell(createdAt)="data">
              {{ formatDate(data.value) }}
            </template>
          </b-table>
        </b-card>

        <!-- 글쓰기 버튼 -->
        <div class="text-right mt-3">
          <b-button variant="primary" @click="writePost">
            글쓰기
          </b-button>
        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import { boardAPI } from '../api/api'

export default {
  name: 'MainView',
  data() {
    return {
      isLoading: false,
      fields: [
        { key: 'id', label: '번호', sortable: true },
        { key: 'title', label: '제목', sortable: true },
        { key: 'author', label: '작성자', sortable: true },
        { key: 'createdAt', label: '작성일', sortable: true },
        { key: 'views', label: '조회수', sortable: true }
      ],
      posts: []
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
    fetchPosts() {
      this.isLoading = true
      try {
        this.posts = boardAPI.getPosts() // 바로 데이터 할당
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
        const detailPost = await boardAPI.getPost(post.id)
        console.log('게시글 상세:', detailPost)
        // 나중에 게시글 상세 페이지로 이동하는 로직 구현
        // this.$router.push(`/post/${post.id}`)
      } catch (error) {
        console.error('게시글 조회 실패:', error)
      }
    },
    writePost() {
      console.log('글쓰기 버튼 클릭')
      // 나중에 글쓰기 페이지로 이동하는 로직 구현
      // this.$router.push('/post/write')
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
