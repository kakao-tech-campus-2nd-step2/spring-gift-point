document.addEventListener("DOMContentLoaded", function () {
  let token = localStorage.getItem('token');
  let authLinks = document.getElementById('auth-links');

  if (token) {
    authLinks.innerHTML = '<li><a href="#" id="logout_btn">로그아웃</a></li>';

    document.getElementById('logout_btn').addEventListener('click',
        function () {
          localStorage.removeItem('userId');
          localStorage.removeItem('token');

          location.reload();
        });
  } else {
    authLinks.innerHTML = `
      <li><a href="/users/login">로그인</a></li>
      <li><a href="/users/register">회원가입</a></li>
    `
  }
});
