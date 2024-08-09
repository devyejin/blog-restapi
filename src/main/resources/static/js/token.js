// 파라미터로 받은 토큰이 있다면, 클라이언트의 로컬 스토리지에 저장

const token = searchParam('token');

if (token) {
    localStorage.setItem("access_token", token);
}


function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}