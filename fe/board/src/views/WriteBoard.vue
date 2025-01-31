<template>
  <b-container class="mt-4">
    <b-card>
      <h4 class="mb-4">게시글 작성</h4>
      
      <b-form @submit.prevent="handleSubmit">
        <!-- 제목 입력 -->
        <b-form-group
          label="제목"
          label-for="title"
        >
          <b-form-input
            id="title"
            v-model="form.title"
            required
            placeholder="제목을 입력하세요"
          ></b-form-input>
        </b-form-group>

        <!-- 내용 입력 -->
        <b-form-group
          label="내용"
          label-for="content"
          class="mt-3"
        >
          <b-form-textarea
            id="content"
            v-model="form.content"
            rows="10"
            required
            placeholder="내용을 입력하세요"
          ></b-form-textarea>
        </b-form-group>

        <!-- 파일 업로드 -->
        <b-form-group
          label="파일 첨부"
          label-for="file"
          class="mt-3"
        >
          <b-form-file
            id="file"
            v-model="form.files"
            multiple
            placeholder="파일을 선택하거나 드래그하세요"
            drop-placeholder="파일을 여기에 놓으세요"
            browse-text="파일 선택"
            class="mb-2"
          ></b-form-file>
          
          <!-- 선택된 파일 목록 -->
          <div v-if="form.files && form.files.length > 0">
            <h6 class="mt-3">선택된 파일:</h6>
            <ul class="list-unstyled">
              <li v-for="(file, index) in form.files" :key="index">
                {{ file.name }} ({{ formatFileSize(file.size) }})
                <b-button 
                  size="sm" 
                  variant="danger" 
                  class="ml-2"
                  @click="removeFile(index)"
                >
                  삭제
                </b-button>
              </li>
            </ul>
          </div>
        </b-form-group>

        <!-- 버튼 그룹 -->
        <div class="mt-4 text-right">
          <b-button
            type="button"
            variant="secondary"
            class="mr-2"
            @click="goBack"
          >
            취소
          </b-button>
          <b-button
            type="submit"
            variant="primary"
          >
            등록
          </b-button>
        </div>
      </b-form>
    </b-card>
  </b-container>
</template>

<script>
import { boardAPI } from '../api/api'

export default {
  name: 'WriteBoard',
  data() {
    return {
      isEdit: false,
      postId: null,
      form: {
        title: '',
        content: '',
        files: []
      }
    }
  },
  created() {
    // URL 쿼리에서 게시글 ID를 확인
    const id = this.$route.query.id
    if (id) {
      this.loadPost(parseInt(id))
    }
  },
  methods: {
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },
    removeFile(index) {
      const newFiles = Array.from(this.form.files)
      newFiles.splice(index, 1)
      this.form.files = newFiles
    },
    async loadPost(id) {
      try {
        const post = await boardAPI.getPost(id)
        this.isEdit = true
        this.postId = id
        this.form.title = post.title
        this.form.content = post.content
        // 기존 파일 정보는 별도 처리 필요
      } catch (error) {
        console.error('게시글 로드 실패:', error)
        this.$bvToast.toast('게시글을 불러오는데 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
        this.$router.push('/')
      }
    },
    async handleSubmit() {
      try {
        const formData = new FormData()
        
        // board 객체 생성 (수정/생성 공통 사용)
        const boardData = {
          title: this.form.title,
          content: this.form.content,
          fileName: this.form.files && this.form.files.length > 0 ? this.form.files[0].name : '',
          filePath: ''
        }

        // FormData에 board 객체를 JSON 문자열로 변환하여 추가
        formData.append('board', new Blob([JSON.stringify(boardData)], {
          type: 'application/json'
        }))

        // 파일이 있는 경우 추가
        if (this.form.files && this.form.files.length > 0) {
          formData.append('file', this.form.files[0])
        }

        if (this.isEdit) {
          await boardAPI.updatePost(this.postId, formData)
        } else {
          await boardAPI.createPost(formData)
        }

        this.$bvToast.toast('게시글이 저장되었습니다.', {
          title: '성공',
          variant: 'success',
          solid: true
        })
        
        this.$router.push('/')
      } catch (error) {
        console.error('게시글 저장 실패:', error)
        this.$bvToast.toast('게시글 저장에 실패했습니다.', {
          title: '에러',
          variant: 'danger',
          solid: true
        })
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.custom-file-label::after {
  content: "찾아보기";
}
</style>
