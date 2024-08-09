window.onload = function () {
  const token = localStorage.getItem('token');
  if (token) {
    const payload = token.split('.')[1];
    let decodedPayload = atob(payload);
    const bytes = new Uint8Array(decodedPayload.length);
    for (let i = 0; i < decodedPayload.length; i++) {
      bytes[i] = decodedPayload.charCodeAt(i);
    }

    decodedPayload = new TextDecoder().decode(bytes);
    const payloadObject = JSON.parse(decodedPayload);
    const name = payloadObject.name;
    document.getElementById('username').textContent = name + '님';
    document.getElementById('userInfo').style.display = 'flex';

    document.getElementById('logoutButton').onclick = function () {
      localStorage.removeItem('token');
      alert("로그아웃 되었습니다.");
      window.location.reload();
    };
    document.getElementById('logoutButton').style.display = 'flex';
  } else {
    document.getElementById('loginLink').style.display = 'flex';
  }
}

function getMemberInfo(){
  getRequestWithToken('/api/members/info');
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
      window.location.href = '/api/members/register';
    } else {
      alert('확인되지 않은 에러입니다. 관리자에게 연락해주시기 바랍니다.');
    }
  });
}