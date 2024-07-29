document.getElementById('loginForm').addEventListener('submit', async function(event) {
  event.preventDefault();

  const email = event.target.email.value;
  const password = event.target.password.value;

  const data = { email, password };

  try {
    const response = await fetch('/members/login', {
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
    console.log('Success:', responseData);
    alert('로그인에 성공하였습니다.');

    // 토큰을 로컬 스토리지에 저장
    localStorage.setItem('token', responseData.token);

    window.location.href = '/api/products';

  } catch (error) {
    console.error('Error:', error);
    alert('예상치 못한 에러가 발생하였습니다.');
  }
});
