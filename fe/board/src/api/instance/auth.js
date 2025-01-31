import { auth } from "./index-instance";

export function fechAuthLogin(memberId, password) {
    return auth.post("/login", { memberId, password });
}