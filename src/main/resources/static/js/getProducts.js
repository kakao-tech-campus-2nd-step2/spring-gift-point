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
    document.getElementById('wishList').style.display = 'flex';
    document.getElementById('logoutButton').style.display = 'flex';
  } else {
    document.getElementById('loginLink').style.display = 'flex';
  }
}

function getRequestWithToken(uri) {
  let getUri = (uri === undefined) ? '/api/products/wishes' : uri;

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




function addProductRow(element) {
  const table = document.getElementById('productTable').getElementsByTagName(
      'tbody')[0];

  const newRowForProduct = table.insertRow();
  const newIdCell = newRowForProduct.insertCell(0);
  const productNameCell = newRowForProduct.insertCell(1);
  const productPriceCell = newRowForProduct.insertCell(2);
  const productImageCell = newRowForProduct.insertCell(3);
  const categoryNameCell = newRowForProduct.insertCell(4);
  newRowForProduct.insertCell(5);
  newRowForProduct.insertCell(6);
  newRowForProduct.insertCell(7);
  const saveCell = newRowForProduct.insertCell(8);
  const cancelCell = newRowForProduct.insertCell(9);


  productNameCell.innerHTML = '<input type="text" id="productName" class="productNAme" oninput="validate()"> <span class="nameMessage"></span>';
  productPriceCell.innerHTML = '<input type="text" id="productPrice" class="productPrice" oninput="validate()"> <span class="priceMessage"></span>';
  productImageCell.innerHTML = '<input type="text" id="productImage" class="productImage">';
  categoryNameCell.innerHTML = '<input type="text" id="productCategoryName" class="productCategoryName" oninput="validate()"> <span class="categoryNameMessage"></span>';
  saveCell.innerHTML = '<img src="/image/save.png" alt="save" id="saveButton" class="saveButton" style="width:100px;height: auto" onclick="saveAddProduct(this)">';
  cancelCell.innerHTML = '<img src="/image/cancel.png" alt="cancel" style="width:100px;height: auto" onclick="cancelProductEditing(this)">';

  element.style.pointerEvents = 'none';
  element.style.opacity = '0.3';
}

function validateName(element) {
  const inputName = element.value;
  const inputMessage = element.nextElementSibling;
  const pattern1 = /^[a-zA-Z0-9ㄱ-ㅎ가-힣()\[\]+\-&/_ ]+$/;
  const pattern2 = /^((?!카카오).)*$/;

  if (inputName.length === 0) {
    return false;
  } else if (inputName.length < 1 || inputName.length > 15) {
    inputMessage.textContent = "제품명 길이는 1~15자만 가능합니다.";
    inputMessage.style.color = "red";
    return false;
  } else if (pattern1.test(inputName) && pattern2.test(inputName)) {
    inputMessage.textContent = "올바른 이름입니다.";
    inputMessage.style.color = "green";
    return true
  } else if (pattern1.test(inputName) && !pattern2.test(inputName)) {
    inputMessage.textContent = "카카오가 포함된 문구는 담당 MD와 협의한 후에 사용해주시기 바랍니다.";
    inputMessage.style.color = "red";
    return false
  }
  inputMessage.textContent = "( ), [ ], +, -, &, /, _을 제외한 특수문자는 입력할 수 없습니다.";
  inputMessage.style.color = "red";
  return false
}

function validatePrice(element) {
  const inputPrice = element.value;
  const inputMessage = element.nextElementSibling;

  if (inputPrice.length === 0) {
    return false;
  } else if (isNaN(inputPrice)) {
    inputMessage.textContent = "가격을 숫자로 입력해주세요";
    inputMessage.style.color = "red";
    return false
  } else if (inputPrice.length < 0 || inputPrice > 2147483647) {
    inputMessage.textContent = "가격은 0~2147483647원 까지만 가능합니다.";
    inputMessage.style.color = "red";
    return false
  }
  inputMessage.textContent = "올바른 가격입니다.";
  inputMessage.style.color = "green";
  return true
}

function validate() {
  const productName = document.getElementById('productName');
  const productPrice = document.getElementById('productPrice');
  const saveButton = document.getElementById('saveButton');

  if (validateName(productName) && validatePrice(productPrice)) {
    saveButton.style.pointerEvents = 'auto';
    saveButton.style.opacity = '1';
  } else {
    saveButton.style.pointerEvents = 'none';
    saveButton.style.opacity = '0.3';
  }
}

function saveAddProduct() {
  const productName = document.getElementById('productName').value;
  const productPrice = document.getElementById('productPrice').value;
  const productImage = document.getElementById('productImage').value;
  const categoryName = document.getElementById('productCategoryName').value;

  let optionName = prompt("옵션 이름을 입력해주세요");
  let optionQuantity = prompt("옵션 수량을 입력해주세요.");

  let requestJson = {
    "name": productName,
    "price": productPrice,
    "imageUrl": productImage,
    "categoryName" : categoryName,
    "optionName" : optionName,
    "optionQuantity" : optionQuantity
  };

  console.log(JSON.stringify(requestJson));

  $.ajax({
    type: 'POST',
    url: '/api/products',
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('상품 추가를 성공하였습니다.');
      window.location.href = '/api/products';
    },
    error: function (xhr) {
      if (xhr.responseJSON && xhr.responseJSON.isError
          && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('상품 추가, 삭제, 수정은 로그인을 해야 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('상품 추가를 실패하였습니다. 값을 제대로 입력했는지 확인해주세요');
      }
    }
  });




}

function addWishList(button){
  const row = button.closest('tr');

  const idCell = row.querySelector('.productId');
  const nameCell = row.querySelector('.productName');
  const priceCell = row.querySelector('.productPrice');
  const imageCell = row.querySelector('.productImage');
  const categoryNameCell = row.querySelector('.productCategoryName');

  const currentId = idCell.innerText;
  const currentName = nameCell.innerText;
  const currentPrice = priceCell.innerText;
  const currentImage = imageCell.querySelector('img').src;
  const currentCategoryName = categoryNameCell.innerText;

  let requestJson = {
    "id" : currentId,
    "name": currentName,
    "price": currentPrice,
    "imageUrl": currentImage,
    "categoryName" : currentCategoryName
  };

  $.ajax({
    type: 'POST',
    url: '/api/products/wishes',
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('위시리스트에 제품이 추가되었습니다.');
      window.location.reload();
    },
    error: function (xhr) {
      if (xhr.responseJSON && xhr.responseJSON.isError && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('로그인 후 이용 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('제품 추가를 실패하였습니다.');
      }
    }
  });

}

function cancelProductEditing() {
  const table = document.getElementById('productTable').getElementsByTagName(
      'tbody')[0];
  table.deleteRow(table.rows.length - 3);

  const addButton = document.getElementById('addButton');
  addButton.style.pointerEvents = 'auto';
  addButton.style.opacity = '1';
}

function removeProductRow(button) {
  const row = button.closest('tr');
  const productId = row.getAttribute('data-id');

  $.ajax({
    type: 'DELETE',
    url: `/api/products/${productId}`,
    contentType: 'application/json; charset=utf-8',
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('상품 삭제를 성공하였습니다.');
      window.location.href = '/api/products';
    },
    error: function (xhr) {
      if (xhr.responseJSON && xhr.responseJSON.isError
          && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('상품 추가, 삭제, 수정은 로그인을 해야 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('상품 삭제를 실패하였습니다.');
      }
      window.location.href = '/api/products';
    }
  });
}

function editProductRow(button) {
  const row = button.closest('tr');

  const nameCell = row.querySelector('.productName');
  const priceCell = row.querySelector('.productPrice');
  const imageCell = row.querySelector('.productImage');
  const categoryNameCell = row.querySelector('.productCategoryName');

  const currentName = nameCell.innerText;
  const currentPrice = priceCell.innerText;
  const currentImage = imageCell.querySelector('img').src;
  const currentCategoryName = categoryNameCell.innerText;

  nameCell.innerHTML = `<input type="text" id="productName" class="productName" value="${currentName}" oninput="validate()"> <span class="nameMessage"></span>`;
  priceCell.innerHTML = `<input type="text" id="productPrice" class="productPrice" value="${currentPrice}" oninput="validate()"> <span class="priceMessage"></span>`;
  imageCell.innerHTML = `<input type="text" id="productImage" class="productImage" value="${currentImage}">`;
  categoryNameCell.innerHTML = `<input type="text" id="productCategoryName" value="${currentCategoryName}" class="productCategoryName" oninput="validate()"> <span class="categoryNameMessage"></span>`;


  button.setAttribute('src', '/image/save.png');
  button.setAttribute('alt', 'save');
  button.setAttribute('onclick', 'savePutProductRow(this)');
  button.setAttribute('id', 'saveButton');
}

function savePutProductRow(button) {
  const row = button.closest('tr');
  const productId = row.getAttribute('data-id');
  const productName = row.querySelector('.productName input').value;
  const productPrice = row.querySelector('.productPrice input').value;
  const productImage = row.querySelector('.productImage input').value;
  const categoryName = row.querySelector('.productCategoryName input').value;

  let requestJson = {
    "id": productId,
    "name": productName,
    "price": productPrice,
    "imageUrl": productImage,
    "categoryName" : categoryName
  };

  console.log(JSON.stringify(requestJson));


  $.ajax({
    type: 'PUT',
    url: `/api/products/${productId}`,
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
    },
    success: function () {
      alert('상품 수정을 성공하였습니다.');
      window.location.href = '/api/products';
    },
    error: function (xhr) {
      if (xhr.responseJSON && xhr.responseJSON.isError
          && xhr.responseJSON.message) {
        alert('오류: ' + xhr.responseJSON.message);
      } else if (xhr.status == 401) {
        alert('상품 추가, 삭제, 수정은 로그인을 해야 가능합니다.');
        localStorage.removeItem('token');
        window.location.href = '/members/login';
      } else {
        alert('상품 수정을 실패하였습니다. 값을 제대로 입력했는지 확인해주세요');
      }
    }
  });
}

function autoAddProduct(button){

  for (let i = 1; i < 1001; i++) {

    const requestJson = {
      "name": "커피" + i,
      "price": 10000 + i,
      "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
      "categoryName" : "기타",
      "optionName" : "옵션" + i,
      "optionQuantity" : i
    };

    $.ajax({
      type: 'POST',
      url: '/api/products',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(requestJson),
      beforeSend: function (xhr) {
        xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
      },
      success: function () {

      },
      error: function (xhr) {
        if (xhr.responseJSON && xhr.responseJSON.isError
            && xhr.responseJSON.message) {
          alert('오류: ' + xhr.responseJSON.message);
        } else if (xhr.status == 401) {
          alert('상품 추가, 삭제, 수정은 로그인을 해야 가능합니다.');
          localStorage.removeItem('token');
          window.location.href = '/members/login';
        } else {
          alert('상품 추가를 실패하였습니다. 값을 제대로 입력했는지 확인해주세요');
        }
      }
    });
  }

  alert('상품 추가를 성공하였습니다.');
  window.location.href = '/api/products';
}