document.querySelector('.add-btn').addEventListener('click', function() {
    const errorMessageElement = document.getElementById('addOptionErrorMessage');
    errorMessageElement.style.display = 'none';
});

function postOption() {
    const form = document.getElementById('addOptionForm');
    const data = {
        name: form.name.value,
        quantity: form.quantity.value,
    };

    fetch(`/api/product/options/${productId}`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    .then(response => {
            if (response.status == 400) {
                return response.json().then(errorData => {
                    throw new Error(errorData.message);
                });
            }
    })

    .then(result => {
        $('#addOption').modal('hide');
    })

     .catch(error => {
            const errorMessageElement = document.getElementById('addOptionErrorMessage');
            errorMessageElement.innerText = error.message;
            errorMessageElement.style.display = 'block';
     });

}