const editProduct = async (id) => {
  const name = document.getElementById('name').value;
  const price = document.getElementById('price').value;
  const imageUrl = document.getElementById('imageUrl').value;
  return fetch(`/api/products/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({name, price, imageUrl})
  });
}

document.getElementById('edit').addEventListener('click', async (event) => {
  event.preventDefault();
  const response = await editProduct(productId);

  if (response.status !== 200) {
    const result = await response.json();
    alert(result.detail);
    return;
  }

  const message = await response.text();
  alert(message);

  // 홈 화면으로 이동
  window.location.href = '/admin/products';
});

window.onload = async () => {
  const response = await fetch(`/api/products/${productId}`);
  // 상품 정보를 input에 넣어준다.
  const product = await response.json();
  document.getElementById('name').value = product.name;
  document.getElementById('price').value = product.price;
  document.getElementById('imageUrl').value = product.imageUrl;
}