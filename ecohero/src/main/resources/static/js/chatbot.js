document.addEventListener("DOMContentLoaded", function () {
    const regionInput = document.getElementById("region-input");
    const materialInput = document.getElementById("material-input");
    const sendButton = document.getElementById("send-button");
    const resultBox = document.getElementById("result-box");

    // 검색 버튼 클릭 이벤트
    sendButton.addEventListener("click", function () {
        const region = regionInput.value.trim(); // 입력된 지역 정보
        const material = materialInput.value.trim(); // 입력된 물질 정보

        // 입력값 검증 (material만 있어도 요청 가능)
        if (!material) {
            resultBox.textContent = "물질 정보를 입력해주세요.";
            resultBox.style.color = "red";
            return;
        }

        // Fetch API를 사용하여 서버로 요청 보내기
        const query = `/api/chatbot?${region ? `region=${encodeURIComponent(region)}&` : ""}material=${encodeURIComponent(material)}`;

        fetch(query)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("서버 응답 실패");
                }
                return response.text(); // 서버 응답을 텍스트로 변환
            })
            .then((data) => {
                // 결과 표시
                resultBox.textContent = data;
                resultBox.style.color = "black";
                resultBox.classList.add('show');
            })
            .catch((error) => {
                console.error("에러 발생:", error);
                resultBox.textContent = "문제가 발생했습니다. 잠시 후 다시 시도해주세요.";
                resultBox.style.color = "red";
            });
    });

});
