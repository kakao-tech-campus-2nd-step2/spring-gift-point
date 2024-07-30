$(document).ready(function() {
    function loadCategories(callback) {
        $.ajax({
            url: '/api/categories',
            type: 'GET',
            success: function(response) {
                if (response.success && response.data.length > 0) {
                    callback(response.data);
                } else {
                    alert("No categories found.");
                }
            },
            error: function() {
                alert("Error fetching categories.");
            }
        });
    }

    $("#addProductBtn").click(function() {
        loadCategories(function(categories) {
            let categorySelect = $('#category');
            categorySelect.empty();
            categories.forEach(function(category) {
                categorySelect.append(new Option(category.name, category.id));
            });
        });

        $("#addProductModal").dialog({
            modal: true,
            width: 400,
            buttons: {
                "Add": function() {
                    let name = $("#name").val();
                    let price = $("#price").val();
                    let imageUrl = $("#imageUrl").val();
                    let categoryId = $("#category").val();

                    if (name && price && imageUrl && categoryId) {
                        let newProduct = {
                            name: name,
                            price: price,
                            imageUrl: imageUrl,
                            category: { id: categoryId }
                        };

                        $.ajax({
                            url: '/api/products',
                            type: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify(newProduct),
                            success: function() {
                                window.location.href = '/admin/products';
                            },
                            error: function() {
                                alert("Error adding product.");
                            }
                        });
                    } else {
                        alert("All fields are required!");
                    }
                },
                "Cancel": function() {
                    $(this).dialog("close");
                }
            }
        });
    });

    $(".edit-btn").click(function() {
        let row = $(this).closest("tr");
        row.find(".display-text").hide();
        row.find(".edit-input").show();
        $(this).hide();
        row.find(".save-btn").show();

        let categorySelect = row.find(".edit-input").eq(3);
        let currentCategoryId = row.find(".display-text").eq(3).data("category-id");

        loadCategories(function(categories) {
            categorySelect.empty();
            categories.forEach(function(category) {
                let option = new Option(category.name, category.id);
                if (category.id == currentCategoryId) {
                    option.selected = true;
                }
                categorySelect.append(option);
            });
        });
    });

    $(document).on("click", ".save-btn", function() {
        let row = $(this).closest("tr");
        let id = row.data("id");
        let name = row.find(".edit-input").eq(0).val();
        let price = row.find(".edit-input").eq(1).val();
        let imageUrl = row.find(".edit-input").eq(2).val();
        let categoryId = row.find(".edit-input").eq(3).val();

        let updatedProduct = {
            id: id,
            name: name,
            price: price,
            imageUrl: imageUrl,
            category: { id: categoryId }
        };

        $.ajax({
            url: '/api/products/' + id,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedProduct),
            success: function() {
                window.location.href = '/admin/products';
            },
            error: function() {
                alert("Error updating product.");
            }
        });
    });

    $(document).on("click", ".delete-btn", function() {
        let row = $(this).closest("tr");
        let id = row.data("id");

        $.ajax({
            url: '/api/products/' + id,
            type: 'DELETE',
            success: function() {
                window.location.href = '/admin/products';
            },
            error: function() {
                alert("Error deleting product.");
            }
        });
    });
});
