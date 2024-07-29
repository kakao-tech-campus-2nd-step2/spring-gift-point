window.onload = function() {
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

    document.getElementById('logoutButton').onclick = function() {
      localStorage.removeItem('token');
      alert("로그아웃 되었습니다.");
      window.location.reload();
    };

    document.getElementById('logoutButton').style.display = 'flex';
  } else {
    document.getElementById('loginLink').style.display = 'flex';
  }
}

function editQuantity(event) {
  const row = event.closest('tr');
  const quantityCell = row.querySelector('.productQuantity');
  const editCell = row.querySelector('.productEdit');

  editCell.src = '/image/save.png';
  editCell.alt = 'save';
  editCell.onclick = function() {
    saveQuantity(this);
  }
  quantityCell.innerHTML = '<input type="text" id="productQuantityInput">';

}

function saveQuantity(event) {
  const row = event.closest('tr');

  const quantityInput = row.querySelector('.productQuantity input');
  let quantity = quantityInput ? quantityInput.value : '';
  if (quantity === '') {
    alert('상품 개수를 입력해주세요');
    getRequestWithToken('/api/products/wishes');
    return;
  }
  quantity = quantity.trim();
  const id = row.querySelector('.productId').innerText;
  const name = row.querySelector('.productName').innerText;
  const price = row.querySelector('.productPrice').innerText;
  const image = row.querySelector('.productImage').querySelector('img').src;
  const categoryName = row.querySelector('.productCategoryName').innerText;

  requestJson = {
    "id" : id,
    "name": name,
    "price": price,
    "imageUrl": image,
    "quantity" : quantity,
    "categoryName" : categoryName
  };

  $.ajax({
    type: 'PUT',
    url: `/api/products/wishes/${quantity}`,
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('위시리스트 제품 개수가 수정되었습니다.');
      getRequestWithToken('/api/products/wishes');
    },
    error: function (xhr) {
      console.log(xhr.responseJSON);
      if (xhr.responseJSON && xhr.responseJSON.isError && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('로그인 후 이용 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('상품의 개수는 0보다 커야합니다.');
      }
    }
  });
}

function deleteWish(button) {
  const row = button.closest('tr');
  const productId = row.querySelector('.productId').innerText;

  $.ajax({
    type: 'DELETE',
    url: `/api/products/wishes/${productId}`,
    contentType: 'application/json; charset=utf-8',
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('위시리스트에서 제품이 삭제되었습니다.');
      getRequestWithToken('/api/products/wishes');
    },
    error: function (xhr) {
      if (xhr.responseJSON && xhr.responseJSON.isError && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('로그인 후 이용 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('제품 삭제를 실패하였습니다.');
      }
    }
  });
}