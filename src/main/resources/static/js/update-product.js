document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('product-update-form');
    const pathParts = window.location.pathname.split('/');
    const productId = parseInt(pathParts[pathParts.length - 1]);
    const categorySelect = document.getElementById('product-category');

    loadCategories();
    loadProductDetails(productId);

    form.addEventListener('submit', event => {
        event.preventDefault();
        updateProduct(productId);
    });

    function loadCategories() {
        fetch('/api/categories')
            .then(response => response.json())
            .then(categories => {
                categories.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.name;
                    option.textContent = category.name;
                    categorySelect.appendChild(option);
                });
            })
            .catch(error => console.error('[Error] 카테고리 불러오기 에러:', error));
    }

    function loadProductDetails(productId) {
        fetch(`/api/products/${productId}`)
            .then(response => response.json())
            .then(product => {
                document.getElementById('product-id').value = product.id;
                document.getElementById('product-name').value = product.name;
                document.getElementById('product-price').value = product.price;
                document.getElementById('product-image').value = product.imageUrl;
                document.getElementById('product-category').value = product.category;
            })
            .catch(error => console.error('[Error]상품 정보 조회 에러:', error));
    }

    function updateProduct(productId) {
        const updatedProduct = {
            id: productId,
            name: document.getElementById('product-name').value,
            price: parseFloat(document.getElementById('product-price').value),
            imageUrl: document.getElementById('product-image').value,
            category: document.getElementById('product-category').value
        };

        fetch(`/api/products/${productId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedProduct)
        })
            .then(response => {
                if (response.ok) {
                    alert('상품 정보가 수정 성공!');
                    window.location.href = '/page/manage/products';
                } else {
                    alert('상품 정보 수정 실패.');
                }
            })
            .catch(error => console.error('[Error]상품 정보 수정 에러:', error));
    }
});
