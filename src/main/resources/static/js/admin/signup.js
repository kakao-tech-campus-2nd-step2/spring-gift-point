document.getElementById('loginForm').addEventListener('submit',
    function (event) {
      event.preventDefault();

      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;

      const loginData = {
        email: email,
        password: password
      };

      fetch('/api/members/register', {
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
          alert("회원가입 실패. 중복된 이메일입니다. 다른 이메일을 사용해주세요.")
        }
      })
    });