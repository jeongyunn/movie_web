document.addEventListener('DOMContentLoaded', () => {
  const kakaoLoginBtn = document.getElementById('kakao-login-btn');
  const googleLoginBtn = document.getElementById('google-login-btn');
  const loginButtonsDiv = document.querySelector('.login-buttons');
  const navElement = document.querySelector('nav');

  if (kakaoLoginBtn) {
    kakaoLoginBtn.addEventListener('click', () => {
      Kakao.Auth.login({
        success: function(authObj) {
          console.log(authObj); // 인증 정보 확인
          // 여기서 백엔드로 authObj.access_token을 보내서 사용자 정보를 가져오고 로그인 처리
          // 임시로 로그인 성공 시 메뉴를 보이도록 처리
          loginButtonsDiv.style.display = 'none';
          navElement.style.display = 'block';
        },
        fail: function(err) {
          console.error(err);
        }
      });
    });
  }

  // Google 로그인은 HTML <script> 태그에서 처리됩니다.
});