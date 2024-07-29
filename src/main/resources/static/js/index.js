const getProducts = async () => {
  const response = await fetch('/api/products');
  return await response.json();
}

window.onload = async () => {
  const products = await getProducts();
  console.log(products);

  const table = document.getElementById('products-table');
  products.forEach(product => {
    const row = table.insertRow(-1);
    const idCell = row.insertCell(0);
    const nameCell = row.insertCell(1);
    const priceCell = row.insertCell(2);
    const imageUrlCell = row.insertCell(3);
    const actionsCell = row.insertCell(4);
    idCell.innerHTML = product.id;
    nameCell.innerHTML = product.name;
    priceCell.innerHTML = product.price;
    imageUrlCell.innerHTML = product.imageUrl;
    actionsCell.innerHTML = `
      <a href="/admin/edit-product?product-id=${product.id}" class="btn btn-secondary">수정</a>
      <button class="delete btn btn-danger" onclick="deleteProduct(${product.id})">삭제</button>
    `;
  });
}

const deleteProduct = async (id) => {
  const response = await fetch(`/api/products/${id}`, {
    method: 'DELETE'
  });

  if (response.status !== 200) {
    const result = await response.json();
    alert(result.detail);
    return;
  }

  const message = await response.text();
  alert(message);

  // 화면 새로고침
  window.location.reload();
}
