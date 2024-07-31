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
        console.log('Option quantity updated.');
    })
    .catch(error => console.error('Error:', error));
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
    .catch(error => console.error('Error:', error));
}

function orderWishItem(button) {
    const wishId = button.getAttribute('data-wish-id');

    fetch(`/api/orders/order/${wishId}`, {
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
        document.querySelector(`tr[data-wish-id="${wishId}"]`).style.display = 'none';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while placing the order');
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
