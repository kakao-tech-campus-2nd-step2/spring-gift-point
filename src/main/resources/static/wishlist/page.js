import {addProduct, deleteProduct, pagination} from './API.js';

const modal = document.getElementById('productModal');
const closeButton = document.getElementsByClassName('close')[0];
const mainCheckbox = document.querySelector('table th input[type="checkbox"]');
const checkboxes = document.querySelectorAll('table td input[type="checkbox"]');

export function addProductbtnOnClick() {
  modal.getElementsByTagName('h1')[0].innerText = 'Add a new wishlist';
  modal.getElementsByTagName('button')[0].onclick = addProduct.bind(null,);
  modal.style.display = 'flex';
  document.getElementById('product-name-error-message').style.display =
      'none';
}

window.addProductbtnOnClick = addProductbtnOnClick;

window.editProductbtnOnClick = null;

closeButton.onclick = function () {
  modal.modal.style.display = 'none';
};

window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = 'none';
  }
};

export function mainCheckboxOnClick() {
  checkboxes.forEach((checkbox) => {
    checkbox.checked = mainCheckbox.checked;
  });
}

window.mainCheckboxOnClick = mainCheckboxOnClick;

export function checkboxOnClick() {
  if (
      document.querySelectorAll('table td input[type="checkbox"]:checked')
          .length === checkboxes.length
  ) {
    mainCheckbox.checked = true;
  } else {
    mainCheckbox.checked = false;
  }
}

window.checkboxOnClick = checkboxOnClick;

export function deleteCheckedProductsOnClick() {
  const selectedCheckboxes = document.querySelectorAll(
      'table td input[type="checkbox"]:checked'
  );

  selectedCheckboxes.forEach((checkbox) => {
    const id = checkbox
    .closest('tr')
    .querySelector('td:nth-child(2)').innerText; // ID is in the second column
    deleteProduct(id);
  });
  mainCheckbox.checked = false;
}

window.deleteCheckedProductsOnClick = deleteCheckedProductsOnClick;

export function pageSizeSelected() {
  const pageSizeSelector = document.getElementById("page-size");
  pagination(null, pageSizeSelector.value, null)
}

window.pageSizeSelected = pageSizeSelected

export function idColumnPageSort() {
  const currentOrder = document.getElementById("page-sort-by-id")
  .getAttribute("data-sort-order")

  if (currentOrder === "product.id: ASC" || currentOrder === null) {
    pagination(null, null, "id,desc")
  }

  if (currentOrder === "product.id: DESC") {
    pagination(null, null, "id,asc")
  }
}

window.idColumnPageSort = idColumnPageSort

export function priceColumnPageSort() {
  const currentOrder = document.getElementById("page-sort-by-price")
  .getAttribute("data-sort-order")

  if (currentOrder === "product.price: ASC" || currentOrder === null) {
    pagination(null, null, "price,desc")
  }

  if (currentOrder === "product.price: DESC") {
    pagination(null, null, "price,asc")
  }
}

window.priceColumnPageSort = priceColumnPageSort