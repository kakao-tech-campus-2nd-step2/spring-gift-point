const productListPage = '/api/products';

function addOption(productId) {
     event.preventDefault();

    var formData = {
        'name' : $('#name').val(),
        'quantity' : $('#quantity').val(),
        'productId' : productId
    };

    $.ajax({
        method: 'POST',
        url: productListPage + `/product/${productId}/options`,
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,

        success: function (response) {
            alert(response);
            location.href = productListPage + `/product/${productId}/options`
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}

function deleteOption(productId, optionId) {
   $.ajax({
       method: 'DELETE',
       url: productListPage + `/product/${productId}/options/${optionId}`,
       success: function(response) {
            alert(response); 
            location.href = productListPage + `/product/${productId}/options`
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
   });
}

function editOption(productId) {
    event.preventDefault();

    var formData = {
        'id' : $('#id').val(),
        'name' : $('#name').val(),
        'quantity' : $('#quantity').val(),
        'productId' : productId
    };

    $.ajax({
        method: 'PUT',
       url: productListPage + `/product/${productId}/options`,
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,
        success: function (response) {
            alert(response);
            location.href = productListPage + `/product/${productId}/options`
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}
