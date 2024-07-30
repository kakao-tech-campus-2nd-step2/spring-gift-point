function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/members/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('token', data.token);
            alert("로그인 성공");
            loadWishlist();
        })
        .catch(error => {
            alert(error);
            window.location.href = "login";
        });
}

function loadWishlist() {
    const token = localStorage.getItem('token');
    if (!token) {
        alert("로그인을 먼저 하시길 바랍니다.");
        window.location.href = "login";
        return;
    }

    fetch('/api/members/wishlist', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': 'text/html'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("container").innerHTML = html;
        })
        .catch(error => {
            alert(error);
            window.location.href = "login";
        });
}

function addProductToWishlist(productId) {
    const token = localStorage.getItem('token');
    fetch(`/api/wishes/${productId}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            alert("위시 리스트에 상품이 추가되었습니다.");
        })
        .catch(error => {
            alert(error);
        });
    loadWishlist();
}

function deleteProductFromWishlist(productId) {
    const token = localStorage.getItem('token');
    fetch(`/api/wishes/${productId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            alert("위시 리스트에 상품이 삭제되었습니다.");
        })
        .catch(error => {
            alert(error);
        });
    loadWishlist();
}

function moveProductListPage(page) {
    const token = localStorage.getItem('token');
    const url = `/products/list?` + new URLSearchParams({ page: page });
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': 'text/html'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("products").innerHTML = html;
        })
        .catch(error => {
            alert(error);
        });
}

function moveWishlistPage(page) {
    const token = localStorage.getItem('token');
    const url = `/wishes/list?` + new URLSearchParams({ page: page });
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Accept': 'text/html'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("wishes").innerHTML = html;
        })
        .catch(error => {
            alert(error);
        });
}

function orderProduct() {
    if (selectedProductId) {
        const optionId = document.getElementById('option' + selectedProductId).value;
        const quantity = document.getElementById('quantity').value;
        const message = document.getElementById('message').value;

        const token = localStorage.getItem('token');

        fetch(`/api/orders`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                optionId: optionId,
                quantity: quantity,
                message: message
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text)
                    });
                }
                alert(`상품이 주문되었습니다.`);
                window.location.href = "order";
            })
            .catch(error => {
                alert(error);
            });
    } else {
        alert("상품을 선택해주세요.");
    }

}