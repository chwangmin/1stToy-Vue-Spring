<template>
  <div class="comments-section mt-4">
    <h5 class="mb-4 border-bottom pb-2">댓글</h5>
    
    <!-- 댓글 목록 -->
    <div v-if="comments.length > 0" class="comments-list mb-3">
      <div v-for="comment in comments" :key="comment.id" class="comment-item mb-4 bg-light rounded p-3">
        <div class="d-flex justify-content-between align-items-start">
          <div class="w-100">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <div>
                <strong class="text-primary">{{ comment.authorId }}</strong>
                <small class="text-muted ml-2">{{ formatDate(comment.createDate) }}</small>
                <small v-if="comment.modifyDate !== comment.createDate" class="text-muted ml-2">
                  <i class="fas fa-pen-fancy"></i> {{ formatDate(comment.modifyDate) }}
                </small>
              </div>
              <!-- 댓글 수정/삭제 버튼 -->
              <div v-if="accessToken" class="comment-actions">
                <b-button
                  size="sm"
                  variant="outline-secondary"
                  class="mr-2 py-0"
                  @click="startCommentEdit(comment)"
                >
                  <i class="fas fa-edit"></i> 수정
                </b-button>
                <b-button
                  size="sm"
                  variant="outline-danger"
                  class="py-0"
                  @click="deleteComment(comment.id)"
                >
                  <i class="fas fa-trash-alt"></i> 삭제
                </b-button>
              </div>
            </div>
            <div class="comment-content">
              <div v-if="editingCommentId === comment.id">
                <b-form-textarea
                  v-model="editedCommentContent"
                  rows="3"
                  class="mb-2"
                  placeholder="댓글을 입력하세요"
                ></b-form-textarea>
                <div class="text-right">
                  <b-button-group size="sm">
                    <b-button
                      variant="success"
                      @click="saveCommentEdit(comment.id)"
                    >
                      <i class="fas fa-check"></i> 저장
                    </b-button>
                    <b-button
                      variant="secondary"
                      @click="cancelCommentEdit"
                    >
                      <i class="fas fa-times"></i> 취소
                    </b-button>
                  </b-button-group>
                </div>
              </div>
              <div v-else class="py-2">
                {{ comment.content }}
              </div>
            </div>

            <!-- 답글 관련 섹션 -->
            <div class="reply-section mt-2">
              <div class="d-flex align-items-center">
                <div 
                  v-if="comment.replyNum > 0" 
                  class="text-primary mr-2 cursor-pointer d-flex align-items-center"
                  @click="loadReplies(comment.id)"
                >
                  <i 
                    class="fas"
                    :class="comment.showReplies ? 'fa-chevron-down' : 'fa-chevron-right'"
                  ></i>
                  <span class="ml-1">답글 {{ comment.replyNum }}개</span>
                </div>
                <b-button
                  v-if="accessToken"
                  size="sm"
                  variant="link"
                  class="p-0"
                  @click="showReplyForm(comment.id)"
                >
                  답글 작성
                </b-button>
              </div>

              <!-- 답글 작성 폼 -->
              <div v-if="replyingToId === comment.id" class="reply-form mt-2 ml-4">
                <b-card class="bg-white">
                  <b-form @submit.prevent="submitReply(comment.id)">
                    <b-form-textarea
                      v-model="newReply"
                      placeholder="답글을 입력하세요"
                      rows="3"
                      :state="replyState"
                      class="mb-2"
                    ></b-form-textarea>
                    <b-form-invalid-feedback :state="replyState">
                      답글 내용을 입력해주세요.
                    </b-form-invalid-feedback>
                    <div class="text-right">
                      <b-button-group size="sm">
                        <b-button
                          variant="secondary"
                          @click="cancelReply"
                        >
                          <i class="fas fa-times"></i> 취소
                        </b-button>
                        <b-button
                          type="submit"
                          variant="primary"
                        >
                          <i class="fas fa-paper-plane"></i> 답글 작성
                        </b-button>
                      </b-button-group>
                    </div>
                  </b-form>
                </b-card>
              </div>

              <!-- 답글 목록 -->
              <transition name="slide-fade">
                <div v-if="comment.showReplies" class="replies-list mt-2 ml-4">
                  <div 
                    v-for="reply in comment.replies" 
                    :key="reply.id" 
                    class="reply-item bg-white rounded p-3 border"
                  >
                    <div class="d-flex justify-content-between align-items-start">
                      <div class="w-100">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                          <div>
                            <strong class="text-info">{{ reply.authorId }}</strong>
                            <small class="text-muted ml-2">{{ formatDate(reply.createDate) }}</small>
                            <small v-if="reply.modifyDate !== reply.createDate" class="text-muted ml-2">
                              <i class="fas fa-pen-fancy"></i> {{ formatDate(reply.modifyDate) }}
                            </small>
                          </div>
                          <div v-if="accessToken" class="reply-actions">
                            <b-button-group size="sm">
                              <b-button
                                variant="outline-secondary"
                                class="py-0"
                                @click="startCommentEdit(reply)"
                              >
                                <i class="fas fa-edit"></i> 수정
                              </b-button>
                              <b-button
                                variant="outline-danger"
                                class="py-0"
                                @click="deleteComment(reply.id)"
                              >
                                <i class="fas fa-trash-alt"></i> 삭제
                              </b-button>
                            </b-button-group>
                          </div>
                        </div>
                        <div class="reply-content py-2">
                          {{ reply.content }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="text-center text-muted mb-4 py-3 bg-light rounded">
      <i class="fas fa-comments fa-2x mb-2"></i>
      <p class="mb-0">첫 댓글을 작성해보세요!</p>
    </div>

    <!-- 댓글 작성 폼 -->
    <div v-if="accessToken" class="comment-form">
      <b-card class="border-0 shadow-sm">
        <b-form @submit.prevent="submitComment">
          <b-form-group>
            <b-form-textarea
              v-model="newComment"
              placeholder="댓글을 입력하세요"
              rows="3"
              :state="commentState"
              class="mb-2"
            ></b-form-textarea>
            <b-form-invalid-feedback :state="commentState">
              댓글 내용을 입력해주세요.
            </b-form-invalid-feedback>
          </b-form-group>
          <div class="text-right">
            <b-button type="submit" variant="primary">
              <i class="fas fa-paper-plane"></i> 댓글 작성
            </b-button>
          </div>
        </b-form>
      </b-card>
    </div>
    <div v-else class="text-center text-muted py-3 bg-light rounded">
      <i class="fas fa-lock fa-2x mb-2"></i>
      <p class="mb-0">댓글을 작성하려면 로그인이 필요합니다.</p>
    </div>
  </div>
</template>

<script>
import { commentAPI } from '../api/api'

export default {
  name: 'CommentSection',
  
  props: {
    postId: {
      type: String,
      required: true
    },
    accessToken: {
      type: String,
      default: null
    }
  },

  data() {
    return {
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

  created() {
    this.loadComments()
  },

  methods: {
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },

    async loadComments() {
      try {
        const response = await commentAPI.getComments(this.postId)
        this.comments = response.data
      } catch (error) {
        console.error('댓글 로드 실패:', error)
      }
    },

    async submitComment() {
      this.commentState = this.newComment.trim() !== ''
      if (!this.commentState) return

      try {
        await commentAPI.createComment(this.postId, this.newComment.trim())
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
          this.postId,
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
          await commentAPI.deleteComment(this.postId, commentId)
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
          this.postId,
          commentId,
          this.newReply.trim()
        )
        this.replyingToId = null
        this.newReply = ''
        this.replyState = null
        
        // 답글 목록 갱신하되 showReplies는 true로 유지
        const comment = this.comments.find(c => c.id === commentId)
        if (comment) {
          const response = await commentAPI.getReplies(this.postId, commentId)
          this.$set(comment, 'replies', response.data)
          // showReplies를 true로 설정하여 답글이 계속 보이도록 함
          this.$set(comment, 'showReplies', true)
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
            const response = await commentAPI.getReplies(this.postId, commentId)
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
/* ... existing styles ... */
</style>
