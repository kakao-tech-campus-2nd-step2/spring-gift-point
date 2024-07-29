let wishlistAPIUrl = window.location.origin + '/wishes';
const authToken = localStorage.getItem("Authorization")

export function addProduct() {
  fetch(`${wishlistAPIUrl}/` + document.getElementById("select-product").value,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": authToken
        },
      }).then(response => responseResult(response));
}

export function deleteProduct(id) {
  fetch(`${wishlistAPIUrl}/${id}`, {
    method: 'DELETE',
    headers: {
      "Authorization": authToken
    }
  }).then((response) => {
    if (response.status === 200) {
      location.reload();
    }
  });
}

window.deleteProduct = deleteProduct;

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
