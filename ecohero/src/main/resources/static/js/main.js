// 섹션 요소들 및 내비게이션 점 요소들을 선택
const sections = document.querySelectorAll('.section');
const navDots = document.getElementById('navDots');
let currentIndex = 0; // 현재 활성화된 섹션 인덱스

// 내비게이션 점 생성
sections.forEach((_, index) => {
  const dot = document.createElement('div'); // 내비게이션 점 요소 생성
  dot.classList.add('nav-dot'); // 클래스를 추가하여 스타일 적용
  if (index === 0) dot.classList.add('active'); // 첫 번째 섹션에 해당하는 점은 'active' 클래스를 추가
  dot.dataset.index = index; // 점에 인덱스를 데이터로 추가
  navDots.appendChild(dot); // 내비게이션 점을 navDots 컨테이너에 추가
});

const dots = document.querySelectorAll('.nav-dot'); // 생성된 내비게이션 점들 선택

// 섹션으로 스크롤 이동하는 함수
function scrollToSection(index) {
  sections.forEach((section, idx) => {
    // 현재 활성화된 섹션에만 active 클래스 추가
    section.classList.toggle('active', idx === index);
  });

  // 부드럽게 스크롤 이동
  sections[index].scrollIntoView({ behavior: 'smooth' });

  // 내비게이션 점 활성화
  dots.forEach((dot, idx) => dot.classList.toggle('active', idx === index));

  currentIndex = index; // 현재 인덱스 업데이트
}


// 내비게이션 점 클릭 시 해당 섹션으로 스크롤
navDots.addEventListener('click', (e) => {
  if (e.target.classList.contains('nav-dot')) { // 클릭된 요소가 내비게이션 점인 경우
    const index = +e.target.dataset.index; // 클릭된 점의 인덱스 가져오기
    scrollToSection(index); // 해당 섹션으로 스크롤 이동
  }
});

// 휠 스크롤 이벤트 처리
document.addEventListener('wheel', (e) => {
  if (e.deltaY > 0 && currentIndex < sections.length - 1) { // 아래로 스크롤 시
    scrollToSection(currentIndex + 1); // 다음 섹션으로 이동
  } else if (e.deltaY < 0 && currentIndex > 0) { // 위로 스크롤 시
    scrollToSection(currentIndex - 1); // 이전 섹션으로 이동
  }
});

////////세션 3 /////////////////////
  // 이미지 슬라이더
  const imgs = document.querySelectorAll(".mainimg .group .img");
  const sendleft = document.querySelector(".sendleft");
  const sendright = document.querySelector(".sendright");
  const imgContainer = document.querySelector(".group");

  let currentImageIndex = 2; // 초기 선택된 이미지는 3번째 이미지

  // 이미지 위치 및 확대 상태 업데이트
  function updateImagePositions() {
    const imgWidth = imgs[0].clientWidth; // 이미지 너비 가져오기
    const gap = 20; // 이미지 간격
    const visibleArea = document.querySelector(".overlap-group").clientWidth; // 보이는 영역 너비

    // 이미지의 중앙 위치 계산
    const offset =
      -(currentImageIndex * (imgWidth + gap)) + (visibleArea / 2 - imgWidth / 2);

    imgContainer.style.transform = `translateX(${offset}px)`; // 중앙 정렬
    imgs.forEach((img, index) => {
      img.classList.toggle("selected", index === currentImageIndex);
    });
  }

  // 왼쪽 화살표 클릭
  sendleft.addEventListener("click", () => {
    if (currentImageIndex > 0) {
      currentImageIndex--;
      updateImagePositions();
    }
  });

  // 오른쪽 화살표 클릭
  sendright.addEventListener("click", () => {
    if (currentImageIndex < imgs.length - 1) {
      currentImageIndex++;
      updateImagePositions();
    }
  });

  // 이미지 클릭
  imgs.forEach((img, index) => {
    img.addEventListener("click", () => {
      currentImageIndex = index;
      updateImagePositions();
    });
  });

  // 화면 크기 변화 대응
  window.addEventListener("resize", updateImagePositions);

  // 초기화

  ///검색관련///////////////////////////////////////////////
  updateImagePositions();
  const locationInput = document.getElementById('region-input');
  const recyclingInput = document.getElementById('material-input');
  const searchResult = document.getElementById('result-box');

  const recyclingSearch = document.getElementById('recyclingSearch');

  locationSearch.addEventListener('click', function () {
    searchResult.classList.add('show');
    searchResult.innerHTML = `검색 결과: ${locationInput.value}`;
  });

  recyclingSearch.addEventListener('click', function () {
    searchResult.classList.add('show');
    searchResult.innerHTML = `검색 결과: ${recyclingInput.value}`;
  });




//세션4/////////////////////////////////////////////
let count = 15360; // 초기 값

// 숫자에 세 자리마다 쉼표 추가
function formatNumber(number) {
  return number.toLocaleString(); // 숫자를 쉼표로 구분
}

// 각 하트 아이콘을 클릭했을 때 동작할 함수
function handleHeartClick(heartId) {
  const heart = document.getElementById(heartId);
  const counter = document.getElementById('counter');

  // 하트 아이콘 변경 및 애니메이션 추가
  heart.src = 'images/heart.svg'; // 하트 아이콘을 클릭하면 heartstrock에서 채워진 heart.svg로 변경
  heart.classList.add('activated'); // 애니메이션 효과

  // 카운트 증가 및 숫자 포맷 적용
  count += 1;
  counter.textContent = formatNumber(count); // 포맷된 숫자 업데이트

  // 하트 아이콘 애니메이션 후 원래 크기로 돌아가게 하기
  setTimeout(() => {
    heart.classList.remove('activated');
  }, 600); // 애니메이션 시간과 동일하게 설정
}

// 하트 아이콘에 클릭 이벤트 추가
document.getElementById('heart1').addEventListener('click', () => handleHeartClick('heart1'));
document.getElementById('heart2').addEventListener('click', () => handleHeartClick('heart2'));
document.getElementById('heart3').addEventListener('click', () => handleHeartClick('heart3'));
document.getElementById('heart4').addEventListener('click', () => handleHeartClick('heart4'));
document.getElementById('heart5').addEventListener('click', () => handleHeartClick('heart5'));

// 카운트 하트 클릭 이벤트 추가
document.getElementById('heartCount').addEventListener('click', () => handleHeartClick('heartCount'));


// 로그아웃 후 자동 새로고침 처리
 if (window.location.pathname === '/home' && window.location.search.includes('logout')) {
     location.reload();  // 페이지 새로고침
 }