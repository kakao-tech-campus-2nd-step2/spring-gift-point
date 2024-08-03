document.addEventListener('DOMContentLoaded', function() {
  const accessToken = localStorage.getItem('accessToken');

  if (!accessToken) {
    alert('로그인이 필요합니다.');
    window.location.href = '/admin'; // Redirect to login page
    return;
  }

  let currentPage = 0;
  let totalPages = 1;
  let categories = {};

  function loadCategories() {
    fetch('/api/categories', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      }
    })
    .then(response => response.json())
    .then(data => {
      const categorySelect = document.getElementById('categorySelect');
      const editProductCategorySelect = document.getElementById('editProductCategory');
      const addProductCategorySelect = document.getElementById('addProductCategory');
      data.forEach(category => {
        const option = document.createElement('option');
        option.value = category.id;
        option.textContent = category.name;
        categorySelect.appendChild(option);

        categories[category.id] = category.name

        const editOption = option.cloneNode(true);
        editProductCategorySelect.appendChild(editOption);

        const addOption = option.cloneNode(true);
        addProductCategorySelect.appendChild(addOption);
      });
    })
    .catch(error => {
      console.error('Error loading categories:', error);
      alert('카테고리 로딩에 실패했습니다.');
    });
  }

  function loadProducts() {
    const categoryId = document.getElementById('categorySelect').value;

    fetch(`/api/products?categoryId=${categoryId}&page=${currentPage}&size=10`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      }
    })
    .then(response => response.json())
    .then(data => {
      totalPages = data.totalPages;
      const productsList = document.getElementById('productsList');
      productsList.innerHTML = ''; // Clear the current list

      data.content.forEach(product => {
        const productItem = document.createElement('div');
        productItem.className = 'product-item';
        productItem.innerHTML = `
                    <img src="${product.imageUrl}" alt="${product.name}">
                    <h3>${product.name}</h3>
                    <p>가격: ${product.price} 원</p>
                    <p>카테고리: ${categories[product.categoryId]}</p>
                    <input type="hidden" value="${product.id}" class="productId">
                    <div class="buttons">
                        <button class="editProductButton">수정</button>
                        <button class="deleteProductButton">삭제</button>
                    </div>
                `;
        productsList.appendChild(productItem);

        const editButton = productItem.querySelector('.editProductButton');
        const deleteButton = productItem.querySelector('.deleteProductButton');

        editButton.addEventListener('click', function() {
          openEditProductModal(product);
        });

        deleteButton.addEventListener('click', function() {
          if (confirm('정말로 이 상품을 삭제하시겠습니까?')) {
            deleteProduct(product.id);
          }
        });
      });

      document.getElementById('prevPageButton').disabled = currentPage === 0;
      document.getElementById('nextPageButton').disabled = currentPage === totalPages-1;
    })
    .catch(error => {
      console.error('Error loading products:', error);
      alert('상품 로딩에 실패했습니다.');
    });
  }

  function openEditProductModal(product) {
    document.getElementById('editProductId').value = product.id;
    document.getElementById('editProductName').value = product.name;
    document.getElementById('editProductPrice').value = product.price;
    document.getElementById('editProductImageUrl').value = product.imageUrl;

    const modal = document.getElementById('editProductModal');
    modal.style.display = 'block';
  }

  function closeEditProductModal() {
    const modal = document.getElementById('editProductModal');
    modal.style.display = 'none';
  }

  function openAddProductModal() {
    const modal = document.getElementById('addProductModal');
    modal.style.display = 'block';
  }

  function closeAddProductModal() {
    const modal = document.getElementById('addProductModal');
    modal.style.display = 'none';
  }

  function updateProduct(productData) {
    fetch(`/api/products/${productData.productId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      },
      body: JSON.stringify(productData)
    })
    .then(response => {
      if (response.ok) {
        alert('상품이 성공적으로 수정되었습니다.');
        closeEditProductModal();
        loadProducts();
      } else {
        throw new Error('Failed to update product');
      }
    })
    .catch(error => {
      console.error('Error updating product:', error);
      alert('상품 수정에 실패했습니다.');
    });
  }

  function deleteProduct(productId) {
    fetch(`/api/products/${productId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      }
    })
    .then(response => {
      if (response.ok) {
        alert('상품이 삭제되었습니다.');
        loadProducts(); // Reload products list
      } else {
        throw new Error('Failed to delete product');
      }
    })
    .catch(error => {
      console.error('Error deleting product:', error);
      alert('상품 삭제에 실패했습니다.');
    });
  }

  function createProduct(productData) {
    fetch('/api/products', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      },
      body: JSON.stringify(productData)
    })
    .then(response => {
      if (response.ok) {
        alert('상품이 성공적으로 추가되었습니다.');
        closeAddProductModal();
        loadProducts();
      } else {
        throw new Error('Failed to create product');
      }
    })
    .catch(error => {
      console.error('Error creating product:', error);
      alert('상품 추가에 실패했습니다.');
    });
  }

  document.getElementById('loadProductsButton').addEventListener('click', loadProducts);

  document.getElementById('prevPageButton').addEventListener('click', function() {
    if (currentPage > 0) {
      currentPage--;
      loadProducts();
    }
  });

  document.getElementById('nextPageButton').addEventListener('click', function() {
    if (currentPage < totalPages-1) {
      currentPage++;
      loadProducts();
    }
  });

  document.getElementById('goToMainPageButton').addEventListener('click', function() {
    window.location.href = '/admin'; // Redirect to main page
  });

  document.getElementById('editProductForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const productId = document.getElementById('editProductId').value;
    const name = document.getElementById('editProductName').value;
    const price = parseFloat(document.getElementById('editProductPrice').value);
    const imageUrl = document.getElementById('editProductImageUrl').value;
    const categoryId = parseInt(document.getElementById('editProductCategory').value);

    const productData = {
      productId,
      name,
      price,
      imageUrl,
      categoryId
    };

    updateProduct(productData);
  });

  document.getElementById('addProductForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const productData = {
      name: document.getElementById('addProductName').value,
      price: parseFloat(document.getElementById('addProductPrice').value),
      imageUrl: document.getElementById('addProductImageUrl').value,
      categoryId: parseInt(document.getElementById('addProductCategory').value),
      options: [{'name': document.getElementById('addProductName').value, 'quantity': 1000},],
    };
    createProduct(productData);
  });

  document.getElementById('openAddProductModalButton').addEventListener('click', openAddProductModal);

  const closeButtons = document.querySelectorAll('.close-button');
  closeButtons.forEach(button => {
    button.addEventListener('click', function() {
      closeEditProductModal();
      closeAddProductModal();
    });
  });

  // Load categories on page load
  loadCategories();
});
