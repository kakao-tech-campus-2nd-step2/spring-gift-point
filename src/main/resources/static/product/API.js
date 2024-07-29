let productAPIUrl = window.location.origin + '/api/products';

export function addProduct() {
  const product = {
    name: document.getElementById('productName').value,
    price: document.getElementById('productPrice').value,
    imageUrl: document.getElementById('productImageUrl').value,
    category: {
      name: document.getElementById("category").value
    }
  };

  fetch(`${productAPIUrl}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(product),
  }).then(response => responseResult(response));
}

export function deleteProduct(id) {
  fetch(`${productAPIUrl}/${id}`, {
    method: 'DELETE',
  }).then((response) => {
    if (response.status === 200) {
      location.reload();
    }
  });
}

window.deleteProduct = deleteProduct;

export function editProduct(id) {
  const product = {
    name: document.getElementById('productName').value,
    price: document.getElementById('productPrice').value,
    imageUrl: document.getElementById('productImageUrl').value,
    category: {
      name: document.getElementById("category").value
    }
  };

  fetch(`${productAPIUrl}/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(product),
  }).then(response => responseResult(response));
}

function responseResult(response) {
  if (response.status === 200) {
    location.reload()
  }

  if (response.status === 400) {
    response.text().then(
        (text) => {
          const productNameErrorDiv = document.getElementById(
              'product-name-error-message'
          )
          productNameErrorDiv.innerText = text
          productNameErrorDiv.style.display = 'block'
        }
    )
  }
}

export function pagination(page, size, sort) {
  const url = new URL(window.location)
  const param = url.searchParams
  if (page != null) {
    param.set("page", page)
  }
  if (size != null) {
    param.set("size", size)
  }
  if (sort != null) {
    param.set("sort", sort)
  }
  window.location.href = url
}

window.pagination = pagination;
