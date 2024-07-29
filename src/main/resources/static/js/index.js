function openAddModal() {
    var modal = document.getElementById("addModal");
    modal.style.display = "block";
}

function closeAddModal() {
    var modal = document.getElementById("addModal");
    modal.style.display = "none";
}

function openUpdateModal(id) {
    console.log(id);
    var modal = document.getElementById("updateModal");
    document.getElementById("updateId").value = parseInt(id);
    modal.style.display = "block";
}

function closeUpdateModal() {
    var modal = document.getElementById("updateModal");
    modal.style.display = "none";
}

window.onclick = function (event) {
    var modal = document.getElementById("addModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
    var updateModal = document.getElementById("updateModal");
    if (event.target === updateModal) {
        updateModal.style.display = "none";
    }
}

function submitAddForm() {
    var form = document.getElementById('addProductForm');
    var formData = new FormData(form);
    var jsonObject = {};
    formData.forEach((value, key) => {
        jsonObject[key] = value;
    });

    console.log('Sending request with data:', jsonObject);

    fetch('/api/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonObject)
    })
    .then(response => {
        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);

        if (response.ok) {
            return response.json().then(data => {
                console.log('Success:', data);
                closeAddModal();
                window.location.reload();
            });
        } else {
            return response.text().then(text => {
                console.log('Error response text:', text);
                throw new Error(
                    response.status + ' ' + response.statusText + ': ' + text);
            });
        }
    })
    .catch(error => {
        console.error('Caught error:', error);
        if (error.message.includes('404')) {
            alert('입력한 카테고리가 존재하지 않습니다. 올바른 카테고리 이름을 입력해주세요.');
        } else {
            alert('오류가 발생했습니다: ' + error.message);
        }
    });
}

function submitUpdateForm() {
    var form = document.getElementById('updateProductForm');
    var formData = new FormData(form);
    var jsonObject = {};
    formData.forEach((value, key) => {
        jsonObject[key] = value;
    });

    console.log('Sending request with data:', jsonObject);

    fetch('/api/products', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonObject)
    })
    .then(response => {
        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);

        if (response.ok) {
            return response.json().then(data => {
                console.log('Success:', data);
                closeAddModal();
                window.location.reload();
            });
        } else {
            return response.text().then(text => {
                console.log('Error response text:', text);
                throw new Error(
                    response.status + ' ' + response.statusText + ': ' + text);
            });
        }
    })
    .catch(error => {
        console.error('Caught error:', error);
        if (error.message.includes('404')) {
            alert('입력한 카테고리가 존재하지 않습니다. 올바른 카테고리 이름을 입력해주세요.');
        } else {
            alert('오류가 발생했습니다: ' + error.message);
        }
    });
}

function deleteProduct(id) {
    if (confirm('정말로 이 제품을 삭제하시겠습니까?')) {
        fetch('/api/products/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error:', response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}