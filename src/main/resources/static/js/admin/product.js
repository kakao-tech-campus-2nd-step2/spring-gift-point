const newProductModal = document.querySelector('.new_product');
const newProductModalOpen = document.querySelector('.new_product_modal_btn');
const newProductModalClose = document.querySelector('.new_product .close_btn');

//열기 버튼을 눌렀을 때 모달팝업이 열림
newProductModalOpen.addEventListener('click', function () {
  //'on' class 추가
  newProductModal.classList.add('on');
});
//닫기 버튼을 눌렀을 때 모달팝업이 닫힘
newProductModalClose.addEventListener('click', function () {
  //'on' class 제거
  newProductModal.classList.remove('on');
});
document.getElementById('new_product_form').addEventListener('submit',
    function (event) {
      event.preventDefault();

      const formData = new FormData(event.target);
      const data = {};

      formData.forEach((value, key) => {
        data[key] = value;
      });

      // JSON 형식으로 변환
      const jsonData = JSON.stringify(data);

      // fetch를 이용하여 서버로 전송
      fetch('/api/products', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: jsonData
      })
      .then(response => response.json())
      .then(data => {
        location.reload()
      })
    });

const updateProductModal = document.querySelector('.update_product');
const updateProductButtons = document.querySelectorAll(
    '.update_product_button');
const updateProductModalClose = document.querySelector(
    '.update_product .close_btn');

//열기 버튼을 눌렀을 때 모달팝업이 열림
updateProductButtons.forEach(button => {
  button.addEventListener('click', function (event) {
    // 모달의 폼에 데이터 작성
    const row = event.target.parentElement.parentElement;
    const id = row.cells[1].innerText;
    const name = row.cells[2].innerText;
    const price = row.cells[3].innerText;
    const imageUrl = row.cells[4].querySelector('img').src;

    document.getElementById('id').value = id;
    document.getElementById('new_name').value = name;
    document.getElementById('new_price').value = price;
    document.getElementById('new_imageUrl').value = imageUrl;

    //'on' class 추가
    updateProductModal.classList.add('on');
  })
})
//닫기 버튼을 눌렀을 때 모달팝업이 닫힘
updateProductModalClose.addEventListener('click', function () {
  //'on' class 제거
  updateProductModal.classList.remove('on');
});
document.getElementById('update_product_form').addEventListener('submit',
    function (event) {
      event.preventDefault();

      const formData = new FormData(event.target);
      const data = {};

      formData.forEach((value, key) => {
        data[key] = value;
      });

      // id값 추출
      const id = data['id']
      delete data['id']

      // JSON 형식으로 변환
      const jsonData = JSON.stringify(data);

      // fetch를 이용하여 서버로 전송
      fetch('/api/products/' + id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: jsonData
      })
      .then(response => response.json())
      .then(data => {
        location.reload()
      })
    });

const deleteProductButtons = document.querySelectorAll(
    '.delete_product_button');

deleteProductButtons.forEach(button => {
  button.addEventListener('click', function (event) {
    // 상품 id와 name 가져옴
    const row = event.target.parentElement.parentElement;
    const id = row.cells[1].innerText;
    const name = row.cells[2].innerText;

    // 확인 경고창 띄우고
    const isConfirmed = confirm("상품명: " + name + "\n정말로 삭제하시겠습니까?");
    // 확인 누르면 삭제
    if (isConfirmed) {
      fetch('/api/products/' + id, {
        method: 'DELETE'
      })
      .then(data => {
        location.reload()
      })
    }
  })
})

const masterCheckbox = document.querySelector('.master_checkbox');
const checkboxes = document.querySelectorAll('.product_checkbox');

masterCheckbox.addEventListener('change', function () {
  checkboxes.forEach(checkbox => {
    checkbox.checked = masterCheckbox.checked;
  });
});

checkboxes.forEach(checkbox => {
  checkbox.addEventListener('change', function () {
    // 모든 체크박스가 체크되었는지 확인
    const allChecked = Array.from(checkboxes).every(cb => cb.checked);
    // 모든 체크박스가 체크되지 않았는지 확인
    const noneChecked = Array.from(checkboxes).every(cb => !cb.checked);

    if (allChecked) {
      masterCheckbox.checked = true;
      masterCheckbox.indeterminate = false;
    } else if (noneChecked) {
      masterCheckbox.checked = false;
      masterCheckbox.indeterminate = false;
    } else {
      masterCheckbox.checked = false;
      masterCheckbox.indeterminate = true;
    }
  });
});

const deleteSelectedProductsButton = document.querySelector(
    '.delete_selected_products_bnt');
deleteSelectedProductsButton.addEventListener('click', function () {
  // 선택된 상품 id 가져옴
  const selectedProductsIds = [];

  checkboxes.forEach(checkbox => {
    if (checkbox.checked) {
      selectedProductsIds.push(checkbox.id)
    }
  })

  // 확인 경고창 띄우고
  const isConfirmed = confirm(
      selectedProductsIds.length + "개의 상품을 정말로 삭제하시겠습니까?");
  // 확인 누르면 선택된 상품 전체 삭제
  if (isConfirmed) {
    selectedProductsIds.forEach(id => {
      fetch('/api/products/' + id, {
        method: 'DELETE'
      })
    })
    location.reload();
  }
})

