/* --------------- e-mail check --------------- */

function checkDuplicateEmail(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 막기

    const email = document.getElementById("email").value; // 사용자가 입력한 값
    const emailMessage = document.getElementById("email-message"); // 메시지를 표시할 요소

    // 기본적인 이메일 유효성 검사를 위한 정규 표현식
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) { // 입력한 값이 없으면
        emailMessage.textContent = "이메일을 입력하세요.";
        emailMessage.style.color = "red";
    } else if (!emailRegex.test(email)) { // 입력한 값이 유효하지 않으면
        emailMessage.textContent = "유효한 이메일 주소를 입력하세요.";
        emailMessage.style.color = "red";
    } else { // 입력한 값이 유효한 경우 중복 검사
        // fetch API를 사용하여 서버에 GET 요청 보내기
        fetch(`/algoy/check-email-duplicate?email=${encodeURIComponent(email)}`)
            .then(response => {
                // 서버 응답이 성공적인지 확인 (HTTP 상태 코드가 200번대인지 확인)
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json(); // 응답 본문을 JSON으로 파싱하여 반환
            })
            .then(data => {
                // 서버에서 반환된 데이터에 'exists' 필드가 있는지 확인하여 이메일 중복 여부 판단
                if (data.exists) { // 입력한 이메일이 이미 존재하는 경우
                    emailMessage.textContent = "이미 사용 중인 이메일입니다!";
                    emailMessage.style.color = "red";
                } else { // 입력한 이메일이 이미 존재하지 않는(사용 가능한) 경우
                    emailMessage.textContent = "사용 가능한 이메일입니다!";
                    emailMessage.style.color = "green";
                }
            })
            .catch(error => { // fetch 요청 또는 JSON 파싱 중 오류가 발생한 경우
                console.error("Error checking email:", error);
                emailMessage.textContent = "이메일 확인 중 오류가 발생했습니다.";
                emailMessage.style.color = "red";
            });
    }
}

/* --------------- nickname check --------------- */

function checkDuplicateNickname(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 막기

    const nickname = document.getElementById("nickname").value; // 사용자가 입력한 값
    const nicknameMessage = document.getElementById("nickname-message"); // 메시지를 표시할 요소

    // 닉네임 입력란에 값이 있는지 확인
    if (nickname) { // 닉네임 입력란에 값이 있으면
        // fetch API를 사용하여 서버에 GET 요청 보내기
        fetch(`/algoy/check-nickname-duplicate?nickname=${encodeURIComponent(nickname)}`)
            .then(response => {
                // 서버 응답이 성공적인지 확인 (HTTP 상태 코드가 200번대인지 확인)
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json(); // 응답 본문을 JSON으로 파싱하여 반환
            })
            .then(data => {
                // 서버에서 반환된 데이터에 'exists' 필드가 있는지 확인하여 닉네임 중복 여부 판단
                if (data.exists) { // 입력한 닉네임이 이미 존재하는 경우
                    nicknameMessage.textContent = "이미 사용 중인 닉네임입니다!";
                    nicknameMessage.style.color = "red";
                } else { // 입력한 닉네임이 이미 존재하지 않는(사용 가능한) 경우
                    nicknameMessage.textContent = "사용 가능한 닉네임입니다!";
                    nicknameMessage.style.color = "green";
                }
            })
            .catch(error => { // fetch 요청 또는 JSON 파싱 중 오류가 발생한 경우
                console.error("Error checking email:", error);
                nicknameMessage.textContent = "닉네임 확인 중 오류가 발생했습니다.";
                nicknameMessage.style.color = "red";
            });
    } else { // 입력란이 비어있으면
        nicknameMessage.textContent = "사용하실 닉네임을 입력하세요.";
        nicknameMessage.style.color = "red";
    }
}

/* --------------- password check --------------- */

let isMatch = false; // 비밀번호 확인 결과 일치 여부

// 비밀번호 확인 시 일치 여부를 판단하는 함수
function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const messageElement = document.getElementById('password-message');

    if (password === confirmPassword) {
        messageElement.textContent = '비밀번호가 일치합니다.';
        messageElement.style.color = 'green';
        isMatch = true;
    } else {
        messageElement.textContent = '비밀번호가 일치하지 않습니다!';
        messageElement.style.color = 'red';
    }
}

// 폼 제출(회원 가입 버튼 클릭) 시 비밀번호 일치 여부 확인
document.querySelector('form').addEventListener('submit', function (event) {
    if (!isMatch) { // 재입력한 비밀번호가 일치하지 않으면
        event.preventDefault(); // 폼 제출 중지
        alert('비밀번호가 일치하지 않습니다. 다시 입력해 주세요.'); // 메시지 출력
    }
});