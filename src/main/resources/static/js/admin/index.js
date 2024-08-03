document.addEventListener('DOMContentLoaded', function () {
  const menuContainer = document.getElementById('menuContainer');
  const accessToken = localStorage.getItem('accessToken');

  if (accessToken) {
    // Get current points
    fetch('/api/point', {
      method: 'GET',
      headers: {
        'Authorization': accessToken
      }
    })
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        alert('로그인 정보가 올바르지 않습니다.');
        localStorage.removeItem('accessToken');
        location.reload();
      }
    })
    .then(data => {
      const point = data.point; // Adjust based on your API response structure
      menuContainer.innerHTML = `
                <div id="point">현재 포인트: ${point}</div>
                <ul>
                    <li><a href="#" id="chargeButton">포인트 충전</a></li>
                    <li><a href="#">카테고리</a></li>
                    <li><a href="#">상품</a></li>
                    <li><a href="#">옵션</a></li>
                    <li><a href="#" id="logoutButton">로그아웃</a></li>
                </ul>
            `;

      document.getElementById('chargeButton').addEventListener('click',
          function () {
            const point = prompt('충전할 포인트를 입력하세요:');

            if (point && !isNaN(point) && point > 0) {
              const chargeData = {point: parseInt(point, 10)};

              fetch('/api/point', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': accessToken
                },
                body: JSON.stringify(chargeData)
              })
              .then(response => {
                if (response.ok) {
                  return response.json()
                } else {
                  alert('포인트 충전에 실패했습니다.\n포인트는 2,147,483,647까지만 충전 가능합니다.');
                }
              })
              .then(data => {
                document.getElementById('point').innerHTML = '현재 포인트: '
                    + data.point
              });
            } else {
              alert('유효한 포인트 금액을 입력하세요.\n포인트는 2,147,483,647까지만 충전 가능합니다.');
            }
          });

      document.getElementById('logoutButton').addEventListener('click',
          function () {
            localStorage.removeItem('accessToken');
            location.reload();
          });
    });

  } else {
    menuContainer.innerHTML = `
            <ul>
                <li><a href="/admin/login">로그인</a></li>
                <li><a href="/admin/register">회원가입</a></li>
            </ul>
        `;
  }
});
