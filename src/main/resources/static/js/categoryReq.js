const categoryListPage = '/api/categories';

function addCategory() {
    event.preventDefault();

   var formData = {
       'name' : $('#name').val(),
       'color' : $('#color').val(),
       'imageUrl' : $('#imageUrl').val(),
       'description' : $('#description').val()
   };

   $.ajax({
       url: '/api/categories',
       method: 'POST',
       data: JSON.stringify(formData),
       contentType: 'application/json',
       processData: false,

       success: function (response) {
           alert(response);
           location.href = categoryListPage;
       },
       error: function (request, status, error) {
           alert(request.responseText);            
       }
   });
}

function deleteCategory(id) {
  $.ajax({
      method: 'DELETE',
      url: `/api/categories/${id}`,
      success: function(response) {
           alert(response); 
           location.href = categoryListPage;
       },
       error: function (request, status, error) {
           alert(request.responseText);            
       }
  });
}

function editCategory() {
   event.preventDefault();

   var formData = {
       'id' : $('#id').val(),
       'name' : $('#name').val(),
       'color' : $('#color').val(),
       'imageUrl' : $('#imageUrl').val(),
       'description' : $('#description').val()
   };

   $.ajax({
       url: '/api/categories',
       method: 'PUT',
       data: JSON.stringify(formData),
       contentType: 'application/json',
       processData: false,
       success: function (response) {
           alert(response);
           location.href = categoryListPage;
       },
       error: function (request, status, error) {
           alert(request.responseText);            
       }
   });
}