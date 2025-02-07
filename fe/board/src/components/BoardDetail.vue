<template>
  <b-modal
    v-model="showModal"
    :title="isEditing ? '게시글 수정' : post.title"
    size="lg"
    hide-footer
    @hidden="onHidden"
    class="board-detail-modal"
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
        <b-form @submit.prevent="saveEdit">
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

          <!-- 구분선 추가 -->
          <hr class="my-3">

          <!-- 저장/취소 버튼 -->
          <div class="text-right mb-4">
            <b-button
              type="submit"
              variant="success"
              class="mr-2"
            >
              저장
            </b-button>
            <b-button
              type="button"
              variant="secondary"
              @click="cancelEdit"
            >
              취소
            </b-button>
          </div>
        </b-form>
      </div>

      <!-- 게시글 내용 (조회 모드) -->
      <div v-else>
        <div class="post-content mb-4">
          <div class="content-text">
            {{ post.content }}
          </div>
        </div>

        <!-- 첨부 파일 -->
        <div v-if="post.filePath" class="post-files mb-4">
          <h6>첨부 파일:</h6>
          <b-link @click="handleFileDownload" class="file-link">
            <i class="fas fa-file-download mr-1"></i> {{ post.fileName }}
          </b-link>
        </div>

        <!-- 구분선 추가 -->
        <hr class="my-3">

        <!-- 토큰이 있을 때만 수정/삭제 버튼 표시 -->
        <div v-if="accessToken" class="text-right mb-4">
          <b-button
            variant="danger"
            class="mr-2"
            @click="handleDelete"
          >
            삭제
          </b-button>
          <b-button
            variant="primary"
            @click="startEdit"
          >
            수정
          </b-button>
        </div>
      </div>

      <!-- 구분선 -->
      <hr class="my-4">

      <!-- 댓글 섹션 -->
      <comment-section
        :post-id="post.id"
        :access-token="accessToken"
      />

      <!-- 버튼 그룹 -->
      <div class="text-right mt-3">
        <b-button
          variant="secondary"
          @click="closeModal"
        >
          닫기
        </b-button>
      </div>
    </div>
  </b-modal>
</template>

<script>
import { boardAPI, commentAPI } from '../api/api'
import CommentSection from './CommentSection.vue'

export default {
  name: 'BoardDetail',
  components: {
    CommentSection
  },
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
      },
      titleState: null,
      contentState: null,
      comments: [],
      newComment: '',
      commentState: null,
      editingCommentId: null,
      editedCommentContent: '',
      replyingToId: null,
      newReply: '',
      replyState: null
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
    },
    accessToken() {
      return this.$store.state.accessToken
    }
  },
  watch: {
    showModal(newVal) {
      if (newVal) {
        this.loadComments()
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
      if (!this.accessToken) {
        this.$bvToast.toast('로그인이 필요한 기능입니다.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
        return
      }
      this.editedPost = {
        title: this.post.title,
        content: this.post.content,
        authorID: this.post.authorID,
        newFile: null
      }
      this.titleState = null
      this.contentState = null
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
        const formData = new FormData()
        
        const boardData = {
          title: this.editedPost.title.trim(),
          content: this.editedPost.content.trim(),
          authorID: this.editedPost.authorID
        }

        // modifyBoardRequest를 JSON Blob으로 변환
        formData.append('modifyBoardRequest', 
          new Blob([JSON.stringify(boardData)], {
            type: 'application/json'
          })
        )

        // 새 파일이 있는 경우에만 추가
        if (this.editedPost.newFile) {
          formData.append('file', this.editedPost.newFile)
        }

        await boardAPI.updatePost(this.post.id, boardData, this.editedPost.newFile)
        
        // 게시글 상세 정보 다시 불러오기
        const updatedPost = await boardAPI.getPost(this.post.id)
        this.$emit('post-updated', updatedPost.boardDto)
        
        this.isEditing = false
      } catch (error) {
        console.error('수정 실패:', error)
        this.$bvToast.toast('자신의 게시글만 수정할 수 있습니다.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
      }
    },
    handleDelete() {
      if (!this.accessToken) {
        this.$bvToast.toast('로그인이 필요한 기능입니다.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
        return
      }
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
    },
    async loadComments() {
      try {
        const response = await commentAPI.getComments(this.post.id)
        this.comments = response.data
      } catch (error) {
        console.error('댓글 로드 실패:', error)
      }
    },
    async submitComment() {
      this.commentState = this.newComment.trim() !== ''
      if (!this.commentState) return

      try {
        await commentAPI.createComment(this.post.id, this.newComment.trim())
        this.newComment = ''
        this.commentState = null
        await this.loadComments()
      } catch (error) {
        this.$bvToast.toast('댓글 작성에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    startCommentEdit(comment) {
      this.editingCommentId = comment.id
      this.editedCommentContent = comment.content
    },
    cancelCommentEdit() {
      this.editingCommentId = null
      this.editedCommentContent = ''
    },
    async saveCommentEdit(commentId) {
      if (!this.editedCommentContent.trim()) {
        this.$bvToast.toast('댓글 내용을 입력해주세요.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
        return
      }

      try {
        await commentAPI.modifyComment(
          this.post.id,
          commentId,
          this.editedCommentContent.trim()
        )
        this.editingCommentId = null
        this.editedCommentContent = ''
        await this.loadComments()
      } catch (error) {
        this.$bvToast.toast('해당 댓글을 작성한 작성자가 아닙니다.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
      }
    },
    async deleteComment(commentId) {
      try {
        const confirmed = await this.$bvModal.msgBoxConfirm('정말 삭제하시겠습니까?', {
          title: '댓글 삭제',
          okVariant: 'danger',
          okTitle: '삭제',
          cancelTitle: '취소',
          hideHeaderClose: false,
          centered: true
        })

        if (confirmed) {
          await commentAPI.deleteComment(this.post.id, commentId)
          await this.loadComments()
        }
      } catch (error) {
        this.$bvToast.toast('해당 댓글을 작성한 작성자가 아닙니다.', {
          title: '알림',
          variant: 'warning',
          solid: true
        })
      }
    },
    showReplyForm(commentId) {
      this.replyingToId = commentId
      this.newReply = ''
      this.replyState = null
    },
    cancelReply() {
      this.replyingToId = null
      this.newReply = ''
      this.replyState = null
    },
    async submitReply(commentId) {
      this.replyState = this.newReply.trim() !== ''
      if (!this.replyState) return

      try {
        await commentAPI.createReply(
          this.post.id,
          commentId,
          this.newReply.trim()
        )
        this.replyingToId = null
        this.newReply = ''
        this.replyState = null
        
        // 답글 목록 갱신
        const comment = this.comments.find(c => c.id === commentId)
        if (comment) {
          const response = await commentAPI.getReplies(this.post.id, commentId)
          this.$set(comment, 'replies', response.data)
          // showReplies는 변경하지 않음 (true 유지)
          if (!comment.showReplies) {
            this.$set(comment, 'showReplies', true)
          }
        }
      } catch (error) {
        this.$bvToast.toast('답글 작성에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    async loadReplies(commentId) {
      try {
        const comment = this.comments.find(c => c.id === commentId)
        if (comment) {
          if (!comment.showReplies) {
            const response = await commentAPI.getReplies(this.post.id, commentId)
            this.$set(comment, 'replies', response.data)
          }
          this.$set(comment, 'showReplies', !comment.showReplies)
        }
      } catch (error) {
        console.error('답글 로드 실패:', error)
        this.$bvToast.toast('답글을 불러오는데 실패했습니다.', {
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

.comments-section {
  border-top: 1px solid #dee2e6;
  padding-top: 1rem;
}

.comment-item {
  border-bottom: 1px solid #eee;
  padding: 1rem 0;
}

.comment-content {
  white-space: pre-line;
}

.comment-actions button {
  font-size: 0.875rem;
}

.comment-form {
  margin-top: 1rem;
}

.cursor-pointer {
  cursor: pointer;
}

.slide-fade-enter-active {
  transition: all 0.3s ease;
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}

.fas {
  font-size: 12px;
  width: 12px;
  transition: transform 0.3s ease;
}

.fa-chevron-down {
  transform: rotate(0deg);
}

.fa-chevron-right {
  transform: rotate(0deg);
}

.reply-section {
  margin-left: 1rem;
}

.replies-list {
  border-left: 2px solid #eee;
  padding-left: 1rem;
}

.reply-item {
  border-bottom: 1px solid #f5f5f5;
  padding: 0.5rem 0;
}

.file-link {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #007bff;
}

.file-link:hover {
  text-decoration: underline;
}
</style>
