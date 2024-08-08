document.addEventListener('DOMContentLoaded', function () {
  const addForm = document.getElementById('addProductForm');
  addForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);
    const productData = {
      name: formData.get('name'),
      price: parseFloat(formData.get('price')),
      imageUrl: formData.get('imageUrl')
    };

    fetch('/api/products', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(productData)
    }).then(response => {
      if (response.status === 201) {
        window.location.reload();
      } else {
        response.text().then(text => alert(text));
      }
    }).catch(error => alert('Error: ' + error));
  });

  document.querySelectorAll('.save-btn').forEach(button => {
    button.addEventListener('click', function () {
      const id = this.getAttribute('data-id');
      const row = this.closest('tr');
      const productData = {
        name: row.querySelector('.name-input').value,
        price: parseFloat(row.querySelector('.price-input').value),
        imageUrl: row.querySelector('.image-input').value
      };

      fetch(`/api/products/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
      }).then(response => {
        if (!response.ok) {
          response.text().then(text => alert(text));
        }
      }).catch(error => alert('Error: ' + error));
    });
  });

  document.querySelectorAll('.delete-btn').forEach(button => {
    button.addEventListener('click', function () {
      const id = this.getAttribute('data-id');
      if (confirm('Are you sure you want to delete this product?')) {
        fetch(`/api/products/${id}`, {
          method: 'DELETE'
        }).then(response => {
          if (response.ok) {
            window.location.reload();
          } else {
            response.text().then(text => alert(text));
          }
        }).catch(error => alert('Error: ' + error));
      }
    });
  });
});
