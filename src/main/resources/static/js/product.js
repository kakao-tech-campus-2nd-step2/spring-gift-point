$(document).ready(function () {
  let currentPage = 0;
  const pageSize = 10;

  async function loadProducts(page) {
    try {
      const data = await $.get(
          `/products?page=${page}&size=${pageSize}`);
      $('#productTableBody').empty();
      data.forEach(function (product) {
        $('#productTableBody').append(`
          <tr>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.imageUrl}</td>
            <td>
              <button class="btn btn-warning btn-edit" data-id="${product.id}">Edit</button>
              <button class="btn btn-danger btn-delete" data-id="${product.id}">Delete</button>
            </td>
          </tr>
        `);
      });
    } catch (error) {
      console.error('Error loading products:', error);
    }
  }

  async function saveProduct() {
    const product = {
      name: $('#name').val(),
      price: $('#price').val(),
      imageUrl: $('#imageUrl').val()
    };
    const id = $('#productId').val();
    try {
      let response;
      if (id) {
        response = await $.ajax({
          url: `/products/${id}`,
          type: 'PUT',
          contentType: 'application/json',
          data: JSON.stringify(product)
        });
      } else {
        response = await $.ajax({
          url: '/products',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify(product)
        });
      }
      $('#productModal').modal('hide');
      await loadProducts(currentPage);
    } catch (error) {
      console.error('Error saving product:', error);
    }
  }

  async function editProduct(id) {
    try {
      const data = await $.get(`/products/${id}`);
      $('#productId').val(data.id);
      $('#name').val(data.name);
      $('#price').val(data.price);
      $('#imageUrl').val(data.imageUrl);
      $('#productModal').modal('show');
    } catch (error) {
      console.error('Error editing product:', error);
    }
  }

  async function deleteProduct(id) {
    try {
      await $.ajax({
        url: `/products/${id}`,
        type: 'DELETE'
      });
      await loadProducts(currentPage);
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  }

  async function updatePaginationLinks(page) {
    $('#pagination').empty();

    if (page > 0) {
      $('#pagination').append(`
        <li class="page-item"><a class="page-link" href="#" id="previousBlock">Previous</a></li>
      `);
    } else {
      $('#pagination').append(`
        <li class="page-item disabled"><span class="page-link">Previous</span></li>
      `);
    }

    try {
      const totalElements = await $.get('/products/count'); // Fetch total product count from server
      const totalPages = Math.ceil(totalElements / pageSize);
      const startPage = Math.floor(page / 5) * 5;
      const endPage = Math.min(startPage + 5, totalPages);

      for (let i = startPage; i < endPage; i++) {
        if (i === page) {
          $('#pagination').append(`
          <li class="page-item active"><span class="page-link">${i + 1}</span></li>
        `);
        } else {
          $('#pagination').append(`
          <li class="page-item"><a class="page-link" href="#" data-page="${i}">${i
          + 1}</a></li>
        `);
        }
      }

      if (endPage < totalPages) {
        $('#pagination').append(`
        <li class="page-item"><a class="page-link" href="#" id="nextBlock">Next</a></li>
      `);
      } else {
        $('#pagination').append(`
        <li class="page-item disabled"><span class="page-link">Next</span></li>
      `);
      }
    } catch (error) {
      console.error('Error fetching total product count:', error);
    }
  }

  $('#addNewProduct').click(function () {
    $('#productForm')[0].reset();
    $('#productId').val('');
    $('#productModal').modal('show');
  });

  $('#saveProduct').click(async function () {
    await saveProduct();
  });

  $(document).on('click', '.btn-edit', function () {
    const id = $(this).data('id');
    editProduct(id);
  });

  $(document).on('click', '.btn-delete', function () {
    const id = $(this).data('id');
    deleteProduct(id);
  });

  $(document).on('click', '.page-link', function (e) {
    e.preventDefault();
    const page = parseInt($(this).data('page'));
    if (!isNaN(page)) {
      currentPage = page;
      loadProducts(currentPage);
      updatePaginationLinks(currentPage);
    }
  });

  $(document).on('click', '#previousBlock', function () {
    if (currentPage > 0) {
      currentPage -= 5;
      currentPage = parseInt(currentPage / 5) * 5;
      loadProducts(currentPage);
      updatePaginationLinks(currentPage);
    }
  });

  $(document).on('click', '#nextBlock', function () {
    currentPage += 5;
    currentPage = parseInt(currentPage / 5) * 5;
    loadProducts(currentPage);
    updatePaginationLinks(currentPage);
  });

  loadProducts(currentPage);
  updatePaginationLinks(currentPage);
});
