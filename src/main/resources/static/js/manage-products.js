document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 0;
    const productList = document.getElementById('product-list');
    const prevPageButton = document.getElementById('prevPage');
    const nextPageButton = document.getElementById('nextPage');

    loadProducts();

    prevPageButton.addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            loadProducts();
        }
    });

    nextPageButton.addEventListener('click', () => {
        currentPage++;
        loadProducts();
    });

    function loadProducts() {
        fetch(`/api/products?page=${currentPage}`)
            .then(response => {
                if(!response.ok) { throw new Error(response.statusText); }
                return response.json();
            })
            .then(data => {
                const products = data.content;
                productList.innerHTML = '';
                products.forEach(product => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                        <td><img src="${product.imageUrl}" alt="Image" width="50"/></td>
                        <td>${product.category}</td>
                        <td>
                            <a href="/page/manage/products/update/${product.id}">Edit</a>
                            <button onclick="deleteProduct(${product.id})">Delete</button>
                        </td>
                    `;
                    productList.appendChild(row);
                });
                prevPageButton.disabled = products.first;
                nextPageButton.disabled = products.last;
            })
            .catch(error => {
                console.error('[Error]상품 불러오기 에러:', error)
            });
    }

    window.deleteProduct = function(productId) {
        if (confirm('상품을 삭제합니다.')) {
            fetch(`/api/products/${productId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.ok) {
                        alert('상품 삭제 성공!');
                        loadProducts();
                    } else {
                        alert('상품 삭제 실패.');
                    }
                })
                .catch(error => console.error('[Error]상품 삭제 에러:', error));
        }
    }
});
