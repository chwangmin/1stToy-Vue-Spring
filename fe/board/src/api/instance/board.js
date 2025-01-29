import { board } from "./index-instance";

export function fetchBoardList() {
    return board.get("/");
}