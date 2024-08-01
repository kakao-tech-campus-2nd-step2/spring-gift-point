window.onload = function(){
  const token = localStorage.getItem('token');
  console.log(token);
  if (token) {
    fetch('/api/products/wishes', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
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
      window.history.pushState({}, '', '/api/products/wishes');
    })
    .catch(error => {
      if (error.message === '400') {
        alert('회원이 아닙니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/register';
      }
    });
  }
}