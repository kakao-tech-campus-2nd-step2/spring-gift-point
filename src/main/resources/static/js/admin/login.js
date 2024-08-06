document.getElementById('loginForm').addEventListener('submit',
    function (event) {
      event.preventDefault();

      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;

      const loginData = {
        email: email,
        password: password
      };

      fetch('/api/members/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
      })
      .then(response => {
        if (response.ok) {
          const accessToken = response.headers.get('Authorization');
          localStorage.setItem('accessToken', 'Bearer ' + accessToken);
          window.location.href = '/admin';
        } else {
          alert("로그인 실패. 이메일과 비밀번호를 다시 확인해주세요.")
        }
      })
    });