function updateTotalPrice() {
    let totalPrice = 0;
    document.querySelectorAll('.option-row').forEach((row, index) => {
        const price = parseFloat(row.querySelector('.option-price').innerText);
        const quantity = parseInt(row.querySelector('.option-quantity').value);
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
                quantity: parseInt(row.querySelector('.option-quantity').value) || 1
            };
        });

    fetch('/web/wishlist/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({productId, options})
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
    })
    .catch(error => console.error('Error:', error));
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelector('.add-to-wishlist').addEventListener('click', addToWishlist);
    document.querySelectorAll('.option-quantity').forEach(select => {
        select.addEventListener('change', updateTotalPrice);
    });
    document.querySelectorAll('input[name="optionSelect"]').forEach(checkbox => {
        checkbox.addEventListener('change', updateTotalPrice);
    });
    updateTotalPrice();
});
