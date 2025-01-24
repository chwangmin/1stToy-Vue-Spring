<template>
  <div>
    <b-button @click="toggleBusy">Toggle Busy State</b-button>

    <b-table :items="localBoards" :busy="isBusy" :per-page="perPage" :current-page="currentPage" class="mt-3" outlined>
      <template #table-busy>
        <div class="text-center text-danger my-2">
          <b-spinner class="align-middle"></b-spinner>
          <strong>Loading...</strong>
        </div>
      </template>
    </b-table>

    <b-pagination
      v-model="currentPage"
      :total-rows="totalElements"
      :per-page="perPage"
      align="center"
      class="mt-3"
    ></b-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isBusy: true,
      totalPages: 1,
      totalElements: 0,
      currentPage: 2,
      perPage: 10,
      localBoards: []
    };
  },
  async created() {
    try {
      await this.$store.dispatch('FETCH_BOARD_LIST')
      // 받아온 데이터 확인
      console.log('store의 boards:', this.$store.state.boards)
      
      // 데이터가 배열인지 확인
      if (Array.isArray(this.$store.state.boards)) {
        this.localBoards = this.$store.state.boards
        this.totalElements = this.localBoards.length
      } else {
        console.error('boards가 배열이 아닙니다:', this.$store.state.boards)
        this.localBoards = [] // 기본값으로 빈 배열 설정
      }
    } catch (error) {
      console.error('게시판 데이터를 불러오는데 실패했습니다:', error)
      this.localBoards = [] // 에러 시 빈 배열로 설정
    } finally {
      this.isBusy = false
    }
  },
  methods: {
    toggleBusy() {
      this.isBusy = !this.isBusy;
    },
  },
};
</script>
