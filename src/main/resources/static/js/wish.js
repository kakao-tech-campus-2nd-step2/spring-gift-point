import { getAuthToken } from './token.js';

let currentPage = 0;
const pageSize = 5;

function loadWishlist(page) {
    const token = getAuthToken();
    fetch(`/api/wishes?page=${page}&size=${pageSize}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(wishes => {
            const wishlistTableBody = document.querySelector("#wishlistTableBody");
            wishlistTableBody.innerHTML = '';
            wishes.content.forEach(wish => {
                const row = document.createElement('tr');
                row.innerHTML = `
                <td>${wish.id}</td>
                <td>${wish.name}</td>
                <td>${wish.price}</td>
                <td>${wish.imageUrl}</td>
                <td>
                    <button class="btn btn-danger" onclick="deleteWish(${wish.id})">삭제</button>
                </td>
            `;
                wishlistTableBody.appendChild(row);
            });

            renderPagination(wishes.page.totalPages, page);
        });
}

function renderPagination(totalPages, currentPage) {
    const paginationNav = document.querySelector("#paginationNav");
    paginationNav.innerHTML = '';

    const ul = document.createElement('ul');
    ul.classList.add('pagination');

    const prevLi = document.createElement('li');
    prevLi.classList.add('page-item');
    const prevBtn = document.createElement('button');
    prevBtn.classList.add('page-link');
    prevBtn.textContent = '이전';
    prevBtn.onclick = () => {
        if (currentPage > 0) {
            loadWishlist(currentPage - 1);
        }
    };
    prevLi.appendChild(prevBtn);
    ul.appendChild(prevLi);

    for (let i = 0; i < totalPages; i++) {
        const li = document.createElement('li');
        li.classList.add('page-item');
        const btn = document.createElement('button');
        btn.classList.add('page-link');
        btn.textContent = `${i + 1}`;
        btn.onclick = () => {
            loadWishlist(i);
        };
        if (i === currentPage) {
            li.classList.add('active');
        }
        li.appendChild(btn);
        ul.appendChild(li);
    }

    const nextLi = document.createElement('li');
    nextLi.classList.add('page-item');
    const nextBtn = document.createElement('button');
    nextBtn.classList.add('page-link');
    nextBtn.textContent = '다음';
    nextBtn.onclick = () => {
        if (currentPage < totalPages - 1) {
            loadWishlist(currentPage + 1);
        }
    };
    nextLi.appendChild(nextBtn);
    ul.appendChild(nextLi);

    paginationNav.appendChild(ul);
}

function createWish() {
    const productId = document.querySelector('#productId').value;
    const token = getAuthToken();

    fetch('/api/wishes', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({productId})
    }).then(response => {
        if (response.status === 201) {
            alert("위시리스트에 추가되었습니다.");
            loadWishlist(currentPage);
        } else {
            alert("위시리스트에 추가되지 않았습니다.");
        }
    });
}

function deleteWish(id) {
    const token = getAuthToken();
    fetch(`/api/wishes`, {
        method: "DELETE",
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({productId: id})
    }).then(response => {
        if (response.status == 204) {
            alert("위시리스트에서 삭제되었습니다.");
            loadWishlist(currentPage);
        } else {
            alert("위시리스트에서 삭제되지 않았습니다.");
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    loadWishlist(currentPage);
});

window.createWish = createWish;