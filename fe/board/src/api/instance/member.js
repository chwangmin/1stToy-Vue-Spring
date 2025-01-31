import { member } from "./index-instance";

export function fetchMemberInfo() {
    return member.get(`/info`);
}

export function signup(memberData) {
    console.log("회원가입 정보 cc:", memberData);

    const requestBody = {
        memberId: memberData.memberId,
        password: memberData.password,
        koName: memberData.koName,
        enName: memberData.enName,
        email: memberData.email,
        birthdate: memberData.birthdate,
        phoneNumber: memberData.phoneNumber
    };

    console.log("회원가입 정보 cc:", requestBody);
    
    return member.post("/signup", requestBody);
}

