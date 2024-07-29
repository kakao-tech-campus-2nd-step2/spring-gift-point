function deleteProduct(element) {
  if (confirm('이 제품을 삭제하시겠습니까?')) {
    const productId = element.getAttribute('data-id');
    fetch(`/admin/${productId}`, {
      method: 'DELETE'
    })
    .then(response => response.text())
    .then(result => {
      if (result === 'success') {
        window.location.reload();
      } else {
        alert('제품을 삭제하는데 실패하였습니다.');
      }
    });
  }
}
