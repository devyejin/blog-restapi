//삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert("삭제가 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("삭제를 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", "api/articles" + id, null, success, fail);
    });
}

//수정 기능
const modifyButton = document.getElementById('modify-btn');

if(modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({ // html에서 읽은 value를 JSON 형식으로 변환해 전송
            title: document.getElementById('title').value,
            content: document.getElementById('content').value

        });

        function success() {
            alert("수정이 완료되었습니다.");
            location.replace("/articles/"+id);
        }

        function fail() {
            alert("수정 실패했습니다.");
            location.replace("/articles/"+id);
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);


    });
}

//등록 기능
const createButton = document.getElementById('create-btn');

if(createButton) {
    createButton.addEventListener("click", event => {

        let body = JSON.stringify({
            title : document.getElementById("title").value,
            content : document.getElementById("content").value,
        });

        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest

    })
}

//HTTP 요청 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method : method,
        headers : {
            // 로컬 스토리지에서 액세스 토큰 가져와서 헤더에 추가
            Authorization : "Bearer " + localStorage.getItem("access_token"),
            "Content-Type" : "application/json",
        },
        body : body,
    }).then((response) => {
        if(response.status == 200 || response.status == 201) {
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if(response.status == 401 && refresh_token) {
            fetch("/api/token", {
                method : "POST",
                headers : {
                    Authorization : "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type" : "application/json",
                },
                body : JSON.stringify({
                    refreshToken: getCookie("refresh_token"),
                }),
            })
                .then((res) => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then((result) => {
                    //재발급 성공 -> 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem("access_token", result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch((error) => fail());
        } else {
            return fail();
        }
    });
}


function getCookie(key) {
    let result = null;
    let cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ", "");

        let dic = item.split("=");

        if (key == dic[0]) {
             result =dic[1];
             return true;
        }
    });

    return result;
}