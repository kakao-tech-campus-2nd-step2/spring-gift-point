function updateOptionQuantity(selectElement) {
    const wishId = selectElement.getAttribute('data-wish-id');
    const quantity = selectElement.value;

    fetch(`/api/wishes/update/${wishId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ quantity: quantity })
    })
    .then(response => response.json())
    .then(data => {
        console.log('옵션 수량이 업데이트되었습니다.');
    })
    .catch(error => console.error('오류:', error));
}

function refreshPage() {
    window.location.reload();
}

function deleteWishItem(button) {
    const wishId = button.getAttribute('data-wish-id');
    fetch(`/api/wishes/${wishId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
        document.querySelector(`tr[data-wish-id="${wishId}"]`).style.display = 'none';
    })
    .catch(error => console.error('오류:', error));
}

function orderWishlistItem(button) {
    const wishlistId = button.getAttribute('data-wishlist-id');
    const pointsInput = document.querySelector(`input.use-points[data-wishlist-id='${wishlistId}']`);
    const pointsToUse = pointsInput ? pointsInput.value : "0";

    fetch(`/api/orders/order/${wishId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ pointsToUse: pointsToUse })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('네트워크 응답이 올바르지 않습니다');
        }
        return response.json();
    })
    .then(data => {
        if (data.error) {
            alert(data.error);
        } else {
            alert('주문이 성공적으로 완료되었습니다');
            // 세션의 포인트를 업데이트
            const pointsElement = document.getElementById('points');
            if (pointsElement && data.newPoints !== undefined) { // null 체크 및 newPoints 존재 여부 확인
                pointsElement.innerText = data.newPoints;
            }
            window.location.reload();
        }
    })
    .catch(error => {
        console.error('오류:', error);
        alert('주문하는 중 오류가 발생했습니다');
    });
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.delete-wish').forEach(function(button) {
        button.addEventListener('click', function() {
            deleteWishItem(this);
        });
    });
    document.querySelectorAll('.update-quantity').forEach(function(button) {
        button.addEventListener('click', function() {
            refreshPage();
        });
    });
    document.querySelectorAll('.option-quantity').forEach(select => {
        select.addEventListener('change', function() {
            updateOptionQuantity(this);
        });
    });
    document.querySelectorAll('.order-wish').forEach(function(button) {
        button.addEventListener('click', function() {
            orderWishItem(this);
        });
    });
});
