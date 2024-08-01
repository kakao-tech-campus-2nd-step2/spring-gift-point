function register() {

    event.preventDefault();

    var formData = {
        'email' : $('#email').val(),
        'password' : $('#password').val()
    };

    $.ajax({
        url: '/api/members/register',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,
        success: function (response) {
            alert(response);
            location.href = '/';
        },
        error: function (request, status, error) {
            alert(request.responseText);            
        }
    });
}


$(document).ready(function() {
    var token = localStorage.getItem("token");
    if (token) {
        updateUI(true);
    } else {
        updateUI(false);
    }
//    getWishlist();
});

function login() {

    event.preventDefault();

    var formData = {
        'email' : $('#email').val(),
        'password' : $('#password').val()
    };

    $.ajax({
        url: '/api/members/login',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        processData: false,
        success: function (data, status, jqXHR) {
            const token = jqXHR.getResponseHeader('Token');
            localStorage.setItem("token", token);
            console.log(token);
            alert(data);
            location.href = '/';
            updateUI(true);
        },
        error: function (request, status, error) {
            alert(request.responseText);
        }
    });
}

function logout() {
    localStorage.removeItem('token');
    updateUI(false);
}

function updateUI(isLoggedIn) {
    if (isLoggedIn) {
        $('#login-button').hide(); 
        $('#logout-button').show();
    } else {
        $('#login-button').show();
        $('#logout-button').hide(); 
    }
}



function getWishlist(pageNumber = 1) {
    pageNumber--;
    $.ajax({
        url: `/api/members/wishlist?page=${pageNumber}`,
        type: "GET",
        headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(data) {
            var wishlist = $('#wish-list');
            wishlist.empty();
            wishlist.html(data);
        },
        error: function (request, status, error) {
            alert(request.responseText);
        }
    });
}

$(document).on('click', '.delete-button', function() {
    var productId = $(this).data('product-id');
    deleteWishlist(productId);
});

function deleteWishlist(productId) {
    $.ajax({
        url: `/api/members/wishlist/${productId}`,
        type: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(response) {
            alert(response);
        },
        error: function (request, status, error) {
            alert(request.responseText);
        }
    });
}