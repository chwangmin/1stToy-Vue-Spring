import { fetchBoardList } from "../api/instance/board";

export default {
    FETCH_BOARD_LIST({commit}) {
        return fetchBoardList().then((res) => {
            commit("SET_BOARD_LIST", res.data);
        });
    }
}