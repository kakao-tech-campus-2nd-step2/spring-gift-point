// Select/Deselect all checkboxes
document.getElementById('select-all').addEventListener('change', function(e) {
    const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = e.target.checked;
    });
});

document.getElementById('add-wish-btn').addEventListener('click', function(event) {
    const selectedCheckboxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
    selectedCheckboxes.forEach(checkbox => {
        const row = checkbox.closest('tr');
        const temp = row.getAttribute('data-id');
        console.log('Product ID:', temp);
        addWishProduct(temp);
    });
    returnWishProduct();
});

// Function to add a Wish product
function addWishProduct(temp) {
    const data = {optionId: temp};
    console.log(data);
    fetch(`/kakao/wish/addWish`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Token Invalid');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert(`An error occurred: ${error.message}`);
    });
}

function returnWishProduct() {
    fetch(`/kakao/wish`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Token Invalid');
        }
        return response.text();
    })
    .then(text => {
            document.open();
            document.write(text);
            document.close();
    })
    .catch(error => {
        console.error('Error:', error);
        alert(`An error occurred: ${error.message}`);
    });
}

function loadPage(pageNum) {
    fetch(`/kakao/wish/addWish?page=${pageNum}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {
                    throw new Error(`Failed to fetch getWish: ${errorText}`);
                });
            }
            return response.text();
        })
    .then(html => {
            document.open();
            document.write(html);
            document.close();
        })
    .catch(error => {
        console.error('Error:', error);
        alert(`An error occurred: ${error.message}`);
    });
}