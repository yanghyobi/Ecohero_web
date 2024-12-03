
      // 초기 슬라이드 위치
      let slidePosition = 100;
      const slideContainer = document.getElementById('slideContainer');
      const slideWidth = 280 + 40; // 개별 슬라이드의 너비 + 간격
      const visibleSlides = 1440; // 보이는 영역의 너비
      const scrollAmount = slideWidth * 2; // 한 번에 3개의 슬라이드 이동

      // 오른쪽 버튼 클릭 시
      document.getElementById('rightButton').addEventListener('click', () => {
        if (slidePosition > -(slideWidth * 8 - visibleSlides)) {
          slidePosition -= scrollAmount;
          if (slidePosition < -(slideWidth * 8 - visibleSlides)) {
            slidePosition = -(slideWidth * 8 - visibleSlides);
          }
          slideContainer.style.left = slidePosition + 'px';
        }
      });

      // 왼쪽 버튼 클릭 시
      document.getElementById('leftButton').addEventListener('click', () => {
        if (slidePosition < 100) {
          slidePosition += scrollAmount;
          if (slidePosition > 100) {
            slidePosition = 100;
          }
          slideContainer.style.left = slidePosition + 'px';
        }
      });