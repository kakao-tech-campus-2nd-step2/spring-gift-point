document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('product-addition-form');
    const categorySelect = document.getElementById('product-category');

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

    form.addEventListener('submit', event => {
        event.preventDefault();
        addProduct();
    });

    function addProduct() {
        const newProduct = {
            name: document.getElementById('product-name').value,
            price: parseFloat(document.getElementById('product-price').value),
            imageUrl: document.getElementById('product-image').value,
            category: categorySelect.value
        };

        fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newProduct)
        })
            .then(response => {
                if (response.ok) {
                    alert('상품 추가 성공!');
                    window.location.href = '/page/manage/products';
                } else {
                    console.log(newProduct)
                    alert('상품 추가 실패.');
                }
            })
            .catch(error => console.error('[Error]상품 추가 에러:', error));
    }
});