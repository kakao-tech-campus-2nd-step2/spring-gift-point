// Select/Deselect all checkboxes
document.getElementById('select-all').addEventListener('change', function(e) {
    const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = e.target.checked;
    });
});

document.getElementById('add-wish-option').addEventListener('click', function(event) {
    fetch(`/kakao/wish/addWish`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(errorText => {
                throw new Error(`Failed to fetch addWishProduct: ${errorText}`);
            });
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
});


document.getElementById('delete-selected').addEventListener('click', function(event) {
   const selectedCheckboxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
       selectedCheckboxes.forEach(checkbox => {
           const row = checkbox.closest('tr');
           const temp = row.getAttribute('data-id');
           console.log('Product ID:', temp);
           deleteCheckedProduct(temp);
       });

});

document.querySelectorAll('.send-btn').forEach(button => {
    button.addEventListener('click', function() {
        const optionId = button.dataset.id;
        const optionName = button.dataset.text;

        sendMessage(optionId, optionName);
    });
});

function deleteCheckedProduct(temp) {
    const data = {optionId: temp};
    console.log(data);
    fetch(`/kakao/wish`, {
        method: 'DELETE',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {
                    throw new Error(`Failed to fetch deleteProduct: ${errorText}`);
                });
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

function sendMessage(optionId, optionName) {
    const data = {
            'optionId': optionId,
            'quantity': 1000,
            'message': `Your order success, name: ${optionName}`
    };
    fetch(`/kakao/wish/order`, {
        method: 'POST',
        body: JSON.stringify(data), // JSON 문자열로 변환
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("access_token")
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(errorText => {
                throw new Error(errorText);
            });
        }
        console.log(data);
        alert(data.message);
    })
    .catch(error => {
            console.error('Error:', error);
            alert(`An error occurred: ${error.message}`);
    });
}

function loadPage(pageNum) {
    fetch(`/kakao/wish?page=${pageNum}`, {
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
