// scripts.js

$(document).ready(function () {
  loadProducts();

  function loadProducts() {
    $.ajax({
      url: '/api/products',
      type: 'GET',
      success: function (data) {
        var table = '<table border="1">' +
            '<tr>' +
            '<th>No</th>' +
            '<th>ID</th>' +
            '<th>Name</th>' +
            '<th>Price</th>' +
            '<th>Url</th>' +
            '<th>Edit</th>' +
            '<th>Delete</th>' +
            '</tr>';
        $.each(data, function (index, item) {
          table += '<tr>';
          table += '<td>' + (index + 1) + '</td>';
          table += '<td>' + item.id + '</td>';
          table += '<td>' + item.name + '</td>';
          table += '<td>' + item.price + '</td>';
          table += '<td>' + item.imageUrl + '</td>';
          table += '<td><button class="update-button" data-id="' + item.id
              + '">Update</button></td>';
          table += '<td><button class="delete-button" data-id="' + item.id
              + '">Delete</button></td>';
          table += '</tr>';
        });
        table += '</table>';
        $('#table-container').html(table);

        $('.update-button').click(function () {
          var id = $(this).data('id');
          var row = $(this).closest('tr');
          var name = row.find('td:nth-child(3)').text();
          var price = row.find('td:nth-child(4)').text();
          var imageUrl = row.find('td:nth-child(5)').text();

          // 모달 팝업에 값 채우기
          $('#modal-id').val(id);
          $('#modal-name').val(name);
          $('#modal-price').val(price);
          $('#modal-imageUrl').val(imageUrl);

          // 모달 팝업 열기
          $('#myModal').css('display', 'block');
        });

        $('.delete-button').click(function () {
          var id = $(this).data('id');
          deleteProduct(id);
        });
      },
      error: function (error) {
        console.log('Error:', error);
      }
    });
  }

  // 모달 확인 버튼 클릭 시
  $('#modal-submit').click(function () {
    var id = $('#modal-id').val();
    var name = $('#modal-name').val();
    var price = $('#modal-price').val();
    var imageUrl = $('#modal-imageUrl').val();
    updateProduct(id, name, price, imageUrl);
  });

  // 모달 닫기 버튼 클릭 시
  $('#modal-close').click(function () {
    $('#myModal').css('display', 'none');
  });

  // 추가 버튼 클릭 시
  $('#add-button').click(function () {
    var name = $('#new-name').val();
    var price = $('#new-price').val();
    var imageUrl = $('#new-imageUrl').val();
    addProduct(name, price, imageUrl);
  });

  function updateProduct(id, name, price, imageUrl) {
    $.ajax({
      url: '/api/products/' + id,
      type: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify({name: name, price: price, imageUrl: imageUrl}),
      success: function () {
        loadProducts();
        $('#myModal').css('display', 'none'); // 모달 닫기
      },
      error: function (error) {
        console.log('Error:', error);
      }
    });
  }

  function deleteProduct(id) {
    $.ajax({
      url: '/api/products/' + id,
      type: 'DELETE',
      success: function () {
        loadProducts();
      },
      error: function (error) {
        console.log('Error:', error);
      }
    });
  }

  function addProduct(name, price, imageUrl) {
    $.ajax({
      url: '/api/products',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({name: name, price: price, imageUrl: imageUrl}),
      success: function () {
        loadProducts();
        $('#new-name').val('');
        $('#new-price').val('');
        $('#new-imageUrl').val('');
      },
      error: function (error) {
        console.log('Error:', error);
      }
    });
  }
});
