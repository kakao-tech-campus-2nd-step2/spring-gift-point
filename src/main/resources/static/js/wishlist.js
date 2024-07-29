function updateOptionQuantity(selectElement) {
    const wishlistId = selectElement.getAttribute('data-wishlist-id');
    const quantity = selectElement.value;

    fetch(`/web/wishlist/update/${wishlistId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ quantity: quantity })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Option quantity updated.');
    })
    .catch(error => console.error('Error:', error));
}

function refreshPage() {
    window.location.reload();
}

function deleteWishlistItem(button) {
    const wishlistId = button.getAttribute('data-wishlist-id');
    fetch(`/web/wishlist/delete/${wishlistId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
        window.location.reload();
    })
    .catch(error => console.error('Error:', error));
}

function orderWishlistItem(button) {
    const wishlistId = button.getAttribute('data-wishlist-id');

    fetch(`/api/orders/order/${wishlistId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        alert('Order placed successfully');
        window.location.reload();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while placing the order');
    });
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.delete-wishlist').forEach(function(button) {
        button.addEventListener('click', function() {
            deleteWishlistItem(this);
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
    document.querySelectorAll('.order-wishlist').forEach(function(button) {
        button.addEventListener('click', function() {
            orderWishlistItem(this);
        });
    });
});
