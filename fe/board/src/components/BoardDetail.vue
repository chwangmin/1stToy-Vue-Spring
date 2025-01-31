<template>
  <b-modal
    v-model="showModal"
    :title="isEditing ? '게시글 수정' : post.title"
    size="lg"
    hide-footer
    @hidden="onHidden"
  >
    <div class="post-detail">
      <!-- 게시글 정보 헤더 -->
      <div class="post-header mb-3">
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <strong>작성자:</strong> {{ post.authorID }}
          </div>
          <div>
            <strong>작성일:</strong> {{ formatDate(post.createdDate) }}
          </div>
        </div>
        <div class="mt-2 d-flex justify-content-between">
          <div>
            <strong>조회수:</strong> {{ post.views }}
          </div>
          <div>
            <strong>수정일:</strong> {{ formatDate(post.modifiedDate) }}
          </div>
        </div>
      </div>

      <!-- 게시글 내용 (수정 모드) -->
      <div v-if="isEditing" class="edit-form">
        <b-form-group label="제목">
          <b-form-input
            v-model="editedPost.title"
            required
            placeholder="제목을 입력하세요"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="내용" class="mt-3">
          <b-form-textarea
            v-model="editedPost.content"
            rows="10"
            required
            placeholder="내용을 입력하세요"
          ></b-form-textarea>
        </b-form-group>

        <!-- 파일 업로드 -->
        <b-form-group v-if="post.fileName" label="현재 파일" class="mt-3">
          <div>{{ post.fileName }}</div>
        </b-form-group>

        <b-form-group label="새 파일 첨부" class="mt-3">
          <b-form-file
            v-model="editedPost.newFile"
            placeholder="파일을 선택하거나 드래그하세요"
            drop-placeholder="파일을 여기에 놓으세요"
            browse-text="파일 선택"
          ></b-form-file>
        </b-form-group>
      </div>

      <!-- 게시글 내용 (조회 모드) -->
      <div v-else>
        <div class="post-content mb-4">
          <div class="content-text">
            {{ post.content }}
          </div>
        </div>

        <!-- 첨부 파일 -->
        <div v-if="post.fileName" class="post-files mb-4">
          <h6>첨부 파일:</h6>
          <b-link @click="handleFileDownload">
            <i class="fas fa-download"></i> {{ post.fileName }}
          </b-link>
        </div>
      </div>

      <!-- 버튼 그룹 -->
      <div class="text-right mt-3">
        <template v-if="isEditing">
          <b-button
            variant="success"
            class="mr-2"
            @click="saveEdit"
          >
            저장
          </b-button>
          <b-button
            variant="secondary"
            @click="cancelEdit"
          >
            취소
          </b-button>
        </template>
        <template v-else>
          <b-button
            variant="danger"
            class="mr-2"
            @click="handleDelete"
          >
            삭제
          </b-button>
          <b-button
            variant="primary"
            class="mr-2"
            @click="startEdit"
          >
            수정
          </b-button>
          <b-button
            variant="secondary"
            @click="closeModal"
          >
            닫기
          </b-button>
        </template>
      </div>
    </div>
  </b-modal>
</template>

<script>
import { boardAPI } from '../api/api'

export default {
  name: 'BoardDetail',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    post: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isEditing: false,
      editedPost: {
        title: '',
        content: '',
        authorID: '',
        newFile: null
      }
    }
  },
  computed: {
    showModal: {
      get() {
        return this.show
      },
      set(value) {
        this.$emit('update:show', value)
      }
    }
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },
    closeModal() {
      this.showModal = false
    },
    onHidden() {
      this.isEditing = false
      this.$emit('hidden')
    },
    startEdit() {
      this.editedPost = {
        title: this.post.title,
        content: this.post.content,
        authorID: this.post.authorID,
        newFile: null
      }
      this.isEditing = true
    },
    cancelEdit() {
      this.isEditing = false
      this.editedPost = {
        title: '',
        content: '',
        authorID: '',
        newFile: null
      }
    },
    async saveEdit() {
      try {
        const boardData = {
          title: this.editedPost.title,
          content: this.editedPost.content,
          authorID: this.editedPost.authorID
        }

        await this.$emit('save-edit', {
          id: this.post.id,
          boardData: boardData,
          file: this.editedPost.newFile
        })
        
        this.isEditing = false
        this.closeModal()
      } catch (error) {
        console.error('수정 실패:', error)
      }
    },
    handleDelete() {
      this.$bvModal.msgBoxConfirm('정말 삭제하시겠습니까?', {
        title: '게시글 삭제',
        okVariant: 'danger',
        okTitle: '삭제',
        cancelTitle: '취소',
        hideHeaderClose: false,
        centered: true
      })
      .then(value => {
        if (value) {
          this.$emit('delete-post', this.post.id)
          this.closeModal()
        }
      })
    },
    async handleFileDownload() {
      try {
        const response = await boardAPI.downloadFile(this.post.filePath)
        
        // Blob 객체 생성
        const blob = new Blob([response.data])
        
        // 다운로드 링크 생성
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = this.post.fileName
        
        // 링크 클릭하여 다운로드 시작
        document.body.appendChild(link)
        link.click()
        
        // cleanup
        window.URL.revokeObjectURL(url)
        document.body.removeChild(link)
      } catch (error) {
        console.error('파일 다운로드 실패:', error)
        this.$bvToast.toast('파일 다운로드에 실패했습니다.', {
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
.post-detail {
  padding: 1rem;
}

.post-header {
  border-bottom: 1px solid #dee2e6;
  padding-bottom: 1rem;
}

.post-content {
  padding: 1rem 0;
}

.content-text {
  white-space: pre-line;
}

.post-files {
  border-top: 1px solid #dee2e6;
  padding-top: 1rem;
}

.edit-form {
  padding: 1rem 0;
}
</style>
