function updateTotalPrice() {
    let totalPrice = 0;
    document.querySelectorAll('.option-row').forEach((row, index) => {
        const price = parseFloat(row.querySelector('.option-price').innerText);
        const quantity = parseInt(row.querySelector('.option-quantity').innerText);
        const isChecked = row.querySelector('input[name="optionSelect"]').checked;
        if (isChecked) {
            totalPrice += price * quantity;
        }
    });
    document.getElementById('total-price').innerText = Math.round(totalPrice);
}

function addToWishlist() {
    const productId = document.querySelector('.add-to-wishlist').getAttribute('data-product-id');
    const options = Array.from(document.querySelectorAll('.option-row'))
        .filter(row => row.querySelector('input[name="optionSelect"]').checked)
        .map(row => {
            return {
                id: row.getAttribute('data-option-id'),
                quantity: parseInt(row.querySelector('.option-quantity').innerText) || 1
            };
        });

    fetch('/api/wishes/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ productId, options })
    })
    .then(response => response.text())
    .then(data => {
        alert('success');
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
        document.querySelector(`.wishlist-item[data-wishlist-id="${wishlistId}"]`).style.display = 'none';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while placing the order');
    });
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelector('.add-to-wishlist').addEventListener('click', addToWishlist);
    document.querySelectorAll('.option-quantity').forEach(select => {
        select.addEventListener('change', updateTotalPrice);
    });
    document.querySelectorAll('input[name="optionSelect"]').forEach(checkbox => {
        checkbox.addEventListener('change', updateTotalPrice);
    });
    document.querySelectorAll('.order-wishlist').forEach(button => {
        button.addEventListener('click', function() {
            orderWishlistItem(this);
        });
    });
    updateTotalPrice();
});
