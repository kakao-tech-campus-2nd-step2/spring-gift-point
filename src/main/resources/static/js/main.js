document.addEventListener('DOMContentLoaded', () => {
  window.deleteProduct = function(button) {
    const productId = button.getAttribute('data-product-id');
    if (confirm(`Do you want to delete product ID ${productId}?`)) {
      deleteProductById(productId);
    }
  }

  function toggleCheckboxes() {
    const checkboxes = document.querySelectorAll('.product-checkbox');
    const selectAllCheckbox = document.querySelector('#selectAll');
    checkboxes.forEach(checkbox => {
      checkbox.checked = selectAllCheckbox.checked;
    });
  }

  function deleteSelectedProducts() {
    const selectedProducts = [];
    const checkboxes = document.querySelectorAll('.product-checkbox');
    checkboxes.forEach(checkbox => {
      if (checkbox.checked) {
        selectedProducts.push(checkbox.value);
      }
    });

    if (selectedProducts.length === 0) {
      alert('Please select at least one product.');
      return;
    }

    if (confirm('Do you want to delete the selected products?')) {
      deleteProducts(selectedProducts);
    }
  }

  const deleteProducts = productIds => {
    productIds.forEach(productId => deleteProductById(productId));
  }

  const deleteProductById = productId => {
    fetch('/products/' + productId, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
    }).then(response => {
      if (response.ok) {
        alert('Deleted successfully.');
        location.reload();
      } else {
        alert('Failed to delete.');
      }
    }).catch(error => {
      console.error('Error deleting product:', error);
      alert('Failed to delete product. Please try again.');
    });
  }

  const alertMessages = document.querySelectorAll('.alert-message');
  alertMessages.forEach(alertMessage => {
    if (alertMessage.textContent.trim()) {
      alert(alertMessage.textContent.trim());
    }
  });

  window.toggleCheckboxes = toggleCheckboxes;
  window.deleteSelectedProducts = deleteSelectedProducts;
});
