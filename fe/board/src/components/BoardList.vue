<template>
  <div class="board-container">
    <!-- 검색 및 필터 섹션 -->
    <div class="search-section mb-4">
      <b-card class="border-0 shadow-sm">
        <b-row align-v="center">
          <b-col md="3" sm="12" class="mb-2 mb-md-0">
            <b-form-select
              v-model="searchType"
              :options="[
                { value: 'title', text: '제목' },
                { value: 'content', text: '내용' },
                { value: 'author', text: '작성자' }
              ]"
              class="mb-2 mb-md-0"
            ></b-form-select>
          </b-col>
          <b-col md="6" sm="12">
            <b-input-group>
              <b-form-input
                v-model="searchKeyword"
                placeholder="검색어를 입력하세요"
                @keyup.enter="searchPosts"
              ></b-form-input>
              <b-input-group-append>
                <b-button variant="primary" @click="searchPosts">
                  <i class="fas fa-search"></i> 검색
                </b-button>
              </b-input-group-append>
            </b-input-group>
          </b-col>
          <b-col md="3" sm="12" class="text-md-right mt-2 mt-md-0">
            <b-button
              v-if="accessToken"
              variant="success"
              @click="$emit('show-create-modal')"
            >
              <i class="fas fa-plus"></i> 글쓰기
            </b-button>
          </b-col>
        </b-row>
      </b-card>
    </div>

    <!-- 게시글 목록 -->
    <b-card class="border-0 shadow-sm">
      <b-table
        :items="posts"
        :fields="[
          { key: 'title', label: '제목', tdClass: 'title-cell' },
          { key: 'authorID', label: '작성자', tdClass: 'text-center' },
          { key: 'createDate', label: '작성일', tdClass: 'text-center' },
          { key: 'views', label: '조회수', tdClass: 'text-center' }
        ]"
        hover
        responsive
        class="mb-0"
        @row-clicked="handlePostClick"
      >
        <!-- 제목 열 커스텀 템플릿 -->
        <template #cell(title)="data">
          <div class="d-flex align-items-center">
            <span class="title-text">{{ data.item.title }}</span>
            <div class="ml-2 d-flex align-items-center">
              <b-badge
                v-if="data.item.commentCount > 0"
                variant="info"
                pill
                class="mr-1"
              >
                {{ data.item.commentCount }}
              </b-badge>
              <i
                v-if="data.item.fileName"
                class="fas fa-paperclip text-muted"
                title="첨부파일"
              ></i>
            </div>
          </div>
        </template>

        <!-- 작성일 열 커스텀 템플릿 -->
        <template #cell(createDate)="data">
          {{ formatDate(data.item.createDate) }}
        </template>

        <!-- 조회수 열 커스텀 템플릿 -->
        <template #cell(views)="data">
          <span class="text-muted">
            <i class="fas fa-eye"></i> {{ data.item.views }}
          </span>
        </template>
      </b-table>

      <!-- 데이터가 없을 때 표시할 내용 -->
      <div v-if="!posts.length" class="text-center py-5">
        <div class="empty-state">
          <i class="fas fa-clipboard-list fa-3x mb-3 text-muted"></i>
          <p class="text-muted mb-0">게시글이 없습니다.</p>
        </div>
      </div>
    </b-card>

    <!-- 페이지네이션 -->
    <div class="d-flex justify-content-between align-items-center mt-4">
      <div class="text-muted">
        총 {{ totalPosts }}개의 게시글
      </div>
      <b-pagination
        v-model="currentPage"
        :total-rows="totalPosts"
        :per-page="perPage"
        align="center"
        class="mb-0"
      ></b-pagination>
      <div style="width: 100px"></div> <!-- 페이지네이션 중앙 정렬을 위한 더미 div -->
    </div>
  </div>
</template>

<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 테이블 스타일링 */
::v-deep .table {
  margin-bottom: 0;
}

::v-deep .table th {
  background-color: #f8f9fa;
  border-bottom: 2px solid #dee2e6;
  color: #495057;
  font-weight: 600;
  padding: 1rem;
}

::v-deep .table td {
  padding: 1rem;
  vertical-align: middle;
}

::v-deep .table-hover tbody tr:hover {
  background-color: rgba(0, 123, 255, 0.05);
  cursor: pointer;
}

/* 제목 셀 스타일링 */
.title-cell {
  min-width: 300px;
}

.title-text {
  color: #2c3e50;
  font-weight: 500;
}

/* 뱃지 스타일링 */
.badge {
  font-weight: 500;
  font-size: 0.75rem;
  padding: 0.25em 0.6em;
}

/* 카드 스타일링 */
.card {
  transition: box-shadow 0.3s ease;
}

.card:hover {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1) !important;
}

/* 버튼 스타일링 */
.btn {
  padding: 0.5rem 1rem;
  font-weight: 500;
}

.btn-success {
  background-color: #28a745;
  border-color: #28a745;
}

.btn-success:hover {
  background-color: #218838;
  border-color: #1e7e34;
}

/* 페이지네이션 스타일링 */
::v-deep .pagination {
  margin-bottom: 0;
}

::v-deep .page-link {
  color: #007bff;
  padding: 0.5rem 0.75rem;
  border-radius: 0;
}

::v-deep .page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}

/* 빈 상태 스타일링 */
.empty-state {
  padding: 2rem;
  color: #6c757d;
}

/* 반응형 스타일링 */
@media (max-width: 768px) {
  .board-container {
    padding: 10px;
  }

  ::v-deep .table th,
  ::v-deep .table td {
    padding: 0.75rem;
  }

  .title-cell {
    min-width: auto;
  }
}

/* 애니메이션 */
.board-container {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

<script>
export default {
  name: 'BoardList',
  props: {
    posts: {
      type: Array,
      required: true
    },
    totalPosts: {
      type: Number,
      required: true
    },
    currentPage: {
      type: Number,
      required: true
    },
    perPage: {
      type: Number,
      required: true
    },
    accessToken: {
      type: String,
      default: null
    }
  },

  data() {
    return {
      searchType: 'title',
      searchKeyword: ''
    }
  },

  methods: {
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    },

    handlePostClick(item) {
      this.$emit('post-click', item)
    },

    searchPosts() {
      this.$emit('search', {
        type: this.searchType,
        keyword: this.searchKeyword
      })
    }
  }
}
</script> 