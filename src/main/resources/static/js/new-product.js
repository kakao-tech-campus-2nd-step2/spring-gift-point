const addProduct = async () => {
  const name = document.getElementById('name').value;
  const price = document.getElementById('price').value;
  const imageUrl = document.getElementById('imageUrl').value;
  return fetch('/api/products', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({name, price, imageUrl})
  });
}

document.getElementById('add').addEventListener('click', async (event) => {
  event.preventDefault();

  const response = await addProduct();

  if (response.status !== 201) {
    const result = await response.json();
    alert(result.detail);
    return;
  }

  alert('상품 등록 성공');

  // 홈 화면으로 이동
  window.location.href = '/admin/products';
});