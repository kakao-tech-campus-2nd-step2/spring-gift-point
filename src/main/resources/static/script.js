document.addEventListener('DOMContentLoaded', function () {
    const productFormModal = document.getElementById('product-form-modal');
    const productForm = document.getElementById('product-form');
    const productList = document.getElementById('product-list');
    const addProductBtn = document.getElementById('add-product-btn');
    const closeModalBtn = document.querySelector('.close');

    const apiEndpoint = 'http://localhost:8080/api/products';

    // 상품 목록 조회
    function fetchProducts() {
        axios.get(apiEndpoint)
            .then(response => {
                const products = response.data;
                productList.innerHTML = '';
                products.forEach(product => {
                    const productItem = document.createElement('div');
                    productItem.className = 'product-item';
                    productItem.innerHTML = `
                        <img src="${product.imageUrl}" alt="${product.name}">
                        <span>${product.id}</span>
                        <span>${product.name}</span>
                        <span>${product.price}원</span>
                        <button onclick="editProduct(${product.id})">수정</button>
                        <button onclick="deleteProduct(${product.id})">삭제</button>
                    `;
                    productList.appendChild(productItem);
                });
            })
            .catch(error => console.error('상품 목록 조회 에러: ', error));
    }

    // 상품 추가 or 수정 폼 열기
    function openFormModal(product = null) {
        if (product) {
            document.getElementById('form-title').textContent = '상품 수정';
            document.getElementById('product-id').value = product.id;
            document.getElementById('name').value = product.name;
            document.getElementById('price').value = product.price;
            document.getElementById('imageUrl').value = product.imageUrl;
        } else {
            document.getElementById('form-title').textContent = '상품 추가';
            productForm.reset();
            document.getElementById('product-id').value = '';  // id 필드 초기화
        }
        productFormModal.style.display = 'block';
    }

    // 상품 폼 닫기
    function closeFormModal() {
        productFormModal.style.display = 'none';
    }

    // 상품 추가 및 수정 처리
    productForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const id = document.getElementById('product-id').value;
        const name = document.getElementById('name').value;
        const price = document.getElementById('price').value;
        const imageUrl = document.getElementById('imageUrl').value;

        const product = {name, price, imageUrl};

        if (id) {
            // 상품 수정
            axios.put(`${apiEndpoint}/${id}`, product)
                .then(response => {
                    fetchProducts();
                    closeFormModal();
                })
                .catch(error => console.error('상품 수정 에러: ', error));
        } else {
            // 상품 추가
            axios.post(apiEndpoint, product)
                .then(response => {
                    fetchProducts();
                    closeFormModal();
                })
                .catch(error => console.error('상품 추가 에러: ', error));
        }
    });

    // 상품 수정 함수
    window.editProduct = function (id) {
        axios.get(`${apiEndpoint}/${id}`)
            .then(response => {
                openFormModal(response.data);
            })
            .catch(error => console.error('상품 단건 조회 에러: ', error));
    };

    // 상품 삭제 함수
    window.deleteProduct = function (id) {
        axios.delete(`${apiEndpoint}/${id}`)
            .then(response => {
                fetchProducts();
            })
            .catch(error => console.error('상품 삭제 에러: ', error));
    };

    addProductBtn.addEventListener('click', () => openFormModal());
    closeModalBtn.addEventListener('click', closeFormModal);
    window.addEventListener('click', function (event) {
        if (event.target === productFormModal) {
            closeFormModal();
        }
    });

    // 초기 상품 목록 조회
    fetchProducts();
});
