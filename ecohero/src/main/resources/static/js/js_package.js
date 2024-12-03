/// 헤더 로드 함수
function loadHeader() {
 fetch('/header.html') // 'static/header.html'
     .then(response => response.text())
     .then(data => document.getElementById('header-container').innerHTML = data)
     .catch(error => console.error('Error loading header:', error));

}

 // 푸터 로드 함수
 function loadFooter() {
     fetch('/footer.html')
         .then(response => response.text())
         .then(data => document.getElementById('footer-container').innerHTML = data)
         .catch(error => console.error('Error loading footer:', error));
 }

 // 헤더 및 푸터 로드
 window.onload = function () {
     loadHeader();
     loadFooter();
 };
 function checkUsername() {
     const username = document.getElementById('username').value;

     // 아이디가 빈 값일 경우 경고
     if (username === "") {
         alert("아이디를 입력해주세요.");
         return;
     }

     // 서버에 중복 여부를 요청하는 부분 (예시: AJAX 사용)
     fetch(`/check-username?username=${username}`)
         .then(response => response.json())
         .then(data => {
             if (data.exists) {
                 alert("이미 존재하는 아이디입니다.");
             } else {
                 alert("사용 가능한 아이디입니다.");
             }
         })
         .catch(error => {
             console.error("중복 확인 중 오류가 발생했습니다.", error);
             alert("중복 확인에 실패했습니다. 다시 시도해주세요.");
         });
 }

function checkUsername() {
     const username = document.getElementById('username').value;

     if (!username) {
         alert("아이디를 입력해주세요.");
         return;
     }

     fetch(`/check-username?username=${encodeURIComponent(username)}`) // URL 인코딩 추가
         .then(response => {
             if (!response.ok) {
                 throw new Error("Server error");
             }
             return response.json();
         })
         .then(data => {
             if (data.exists) {
                 alert("이미 존재하는 아이디입니다.");
             } else {
                 alert("사용 가능한 아이디입니다.");
             }
         })
         .catch(error => {
             console.error("중복 확인 중 오류 발생:", error);
             alert("중복 확인에 실패했습니다. 다시 시도해주세요.");
         });
 }



