const token = localStorage.getItem('token');

async function postRegister(event) {
  event.preventDefault();
  console.log(localStorage.getItem('token'));

  const email = event.target.email.value;
  const password = event.target.password.value;

  const data = { email, password };

  try {
    const response = await fetch('/api/members/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      const errorData = await response.json();
      alert(errorData.message);
      return;
    }

    const responseData = await response.json();
    alert('회원가입에 성공하였습니다.');

    // 토큰을 로컬 스토리지에 저장
    localStorage.setItem('token', responseData.token);

    getRequestWithToken('/api');

  } catch (error) {
    console.error('Error:', error);
    alert('예상치 못한 에러가 발생하였습니다.');
  }
}

function getRequestWithToken(uri) {
  let getUri = (uri === undefined) ? '/api' : uri;

  fetch(getUri, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(response.status);
    }
    return response.text();
  })
  .then(html => {
    document.open();
    document.write(html);
    document.close();
    window.history.pushState({}, '', getUri);
  })
  .catch(error => {
    if (error.message === '401') {
      alert('회원이 아닙니다.');
      localStorage.removeItem('token');
      window.location.href = '/members/register';
    } else {
      alert('확인되지 않은 에러입니다. 관리자에게 연락해주시기 바랍니다.');
    }
  });
}


document.getElementById('registerForm').addEventListener('submit', postRegister);