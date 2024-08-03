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

function putPointAdd() {

  const token = localStorage.getItem('token');



  if (token) {
    let addPoint = prompt("충전할 포인트를 입력해주세요");

    let requestJson = {
      "point": addPoint
    };

    $.ajax({
      type: 'PUT',
      url: `/api/members/point`,
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('포인트 추가에 성공하였습니다.');
      getRequestWithToken('/api/members/info');
    },
    error: function (xhr) {
      console.log(xhr.responseJSON);
      if (xhr.responseJSON && xhr.responseJSON.isError && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('로그인 후 이용 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/api/members/login';
      } else {
      }
    }
  });
}
}