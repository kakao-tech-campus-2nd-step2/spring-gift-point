const productListPage = '/api/products';

function getOptionsArray() {
    var optionsArray = [];
    $('#options-container .option-row').each(function() {
        var optionName = $(this).find('input[name="optionName"]').val();
        var optionQuantity = $(this).find('input[name="optionQuantity"]').val();

        if (optionName && optionQuantity) {
            var optionObject = {
                name: optionName,
                quantity: optionQuantity,
                productId: null
            };
            optionsArray.push(optionObject);
        }
    });
    return optionsArray;
}

function addOne() {
     event.preventDefault();

    var options = getOptionsArray();

    var formData = {
        'name' : $('#name').val(),
        'price' : $('#price').val(),
        'imageUrl' : $('#imageUrl').val(),
        'categoryId' : $('#category > option:selected').val(),
        'categoryName' : $('#category > option:selected').text(),
        'options' : options
    };

    $.ajax({
        url: '/api/products/product',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,

        success: function (response) {
            alert(response);
            location.href = productListPage
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}

function deleteOne(id) {
   $.ajax({
       method: 'DELETE',
       url: `/api/products/product/${id}`,
       success: function(response) {
            alert(response); 
            location.href = productListPage;
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
   });
}

function editOne() {
    event.preventDefault();

    var formData = {
        'id' : $('#id').val(),
        'name' : $('#name').val(),
        'price' : $('#price').val(),
        'imageUrl' : $('#imageUrl').val(),
        'categoryId' : $('#category > option:selected').val(),
        'categoryName' : $('#category > option:selected').text()
    };

    $.ajax({
        url: '/api/products/product',
        method: 'PUT',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,
        success: function (response) {
            alert(response);
            location.href = productListPage;
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}

function addWishlist(productId) {
    event.preventDefault();

    $.ajax({
        url: `/api/members/wishlist/${productId}`,
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function (response) {
            alert(response);
            location.href = productListPage;
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}

$(document).ready(function() {
    $('#addOptionButton').click(function() {
        var optionName = $('#optionName').val();
        var optionQuantity = $('#optionQuantity').val();

        if (optionName && optionQuantity) {
            var newOptionRow = $('<div class="option-row"></div>');
            newOptionRow.append('<input type="text" name="optionName" class="form-control" value="' + optionName + '" readonly style="display:inline-block; width: 45%;">');
            newOptionRow.append('<input type="number" name="optionQuantity" class="form-control" value="' + optionQuantity + '" readonly style="display:inline-block; width: 45%;">');
            newOptionRow.append('<button type="button" class="btn btn-danger removeOptionButton">삭제</button>');
            $('#options-container').append(newOptionRow);

            // 입력 필드 초기화
            $('#optionName').val('');
            $('#optionQuantity').val('');
        } else {
            alert("옵션 이름과 수량을 입력하세요.");
        }
    });

    $('#options-container').on('click', '.removeOptionButton', function() {
        $(this).parent().remove();
    });
});