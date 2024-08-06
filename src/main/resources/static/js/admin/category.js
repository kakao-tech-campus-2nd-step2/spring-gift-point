document.addEventListener('DOMContentLoaded', function () {
  const accessToken = localStorage.getItem('accessToken');

  if (!accessToken) {
    alert('로그인이 필요합니다.');
    window.location.href = '/admin'; // Redirect to login page
    return;
  }

  function loadCategories() {
    fetch('/api/categories', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      }
    })
    .then(response => response.json())
    .then(data => {
      const categoriesList = document.getElementById('categoriesList');
      categoriesList.innerHTML = ''; // Clear the current list
      data.forEach(category => {
        const categoryItem = document.createElement('div');
        categoryItem.className = 'category-item';
        categoryItem.innerHTML = `
                    <input type="text" class="nameInput" value="${category.name}" placeholder="카테고리 이름">
                    <input type="color" class="colorInput" value="${category.color}" placeholder="색상">
                    <img src="${category.imageUrl}" alt="${category.name}" class="categoryImage">
                    <input type="text" class="imageInput" value="${category.imageUrl}" placeholder="URL">
                    <input type="text" class="descriptionInput" value="${category.description
        || ''}" placeholder="설명">
                    <input type="hidden" class="idInput" value="${category.id}" class="categoryId">
                    <button class="updateCategoryButton">수정</button>
                    <button class="deleteCategoryButton">삭제</button>
                `;
        categoriesList.appendChild(categoryItem);

        const updateButton = categoryItem.querySelector(
            '.updateCategoryButton');
        const deleteButton = categoryItem.querySelector(
            '.deleteCategoryButton');

        updateButton.addEventListener('click', function () {
          const newName = categoryItem.querySelector('.nameInput').value;
          const newColor = categoryItem.querySelector('.colorInput').value;
          const newImageUrl = categoryItem.querySelector('.imageInput').value;
          const newDescription = categoryItem.querySelector(
              '.descriptionInput').value;
          const categoryId = categoryItem.querySelector('.idInput').value;

          if (newName && newColor && newImageUrl) {
            if (confirm('정말로 이 카테고리를 수정하시겠습니까?')) {
              updateCategory(categoryId, newName, newColor, newDescription,
                  newImageUrl);
            }
          } else {
            alert('카테고리 이름, 색상, 그리고 이미지 URL은 필수 입력값입니다.');
          }
        });

        deleteButton.addEventListener('click', function () {
          const categoryId = categoryItem.querySelector('.categoryId').value;

          if (confirm('정말로 이 카테고리를 삭제하시겠습니까?')) {
            deleteCategory(categoryId);
          }
        });
      });
    })
    .catch(error => {
      console.error('Error loading categories:', error);
      alert('카테고리를 불러오는데 실패했습니다.');
    });
  }

  function addCategory(name, color, description, imageUrl) {
    fetch('/api/categories', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      },
      body: JSON.stringify({name, color, description, imageUrl})
    })
    .then(response => {
      if (response.ok) {
        alert('카테고리가 등록되었습니다.');
        loadCategories();
      } else {
        throw new Error('Failed to add category');
      }
    })
    .catch(error => {
      console.error('Error adding category:', error);
      alert('카테고리 등록에 실패했습니다.');
    });
  }

  function updateCategory(id, name, color, description, imageUrl) {
    fetch(`/api/categories/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      },
      body: JSON.stringify({name, color, description, imageUrl})
    })
    .then(response => {
      if (response.ok) {
        alert('카테고리가 수정되었습니다.');
        loadCategories();
      } else {
        throw new Error('Failed to update category');
      }
    })
    .catch(error => {
      console.error('Error updating category:', error);
      alert('카테고리 수정에 실패했습니다.');
    });
  }

  function deleteCategory(id) {
    fetch(`/api/categories/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': accessToken
      }
    })
    .then(response => {
      if (response.ok) {
        alert('카테고리가 삭제되었습니다.');
        loadCategories();
      } else {
        throw new Error('Failed to delete category');
      }
    })
    .catch(error => {
      console.error('Error deleting category:', error);
      alert('카테고리 삭제에 실패했습니다.');
    });
  }

  document.getElementById('loadCategoriesButton').addEventListener('click',
      loadCategories);

  document.getElementById('addCategoryButton').addEventListener('click',
      function () {
        const newName = document.getElementById('newCategoryName').value;
        const newColor = document.getElementById('newCategoryColor').value;
        const newDescription = document.getElementById(
            'newCategoryDescription').value;
        const newImageUrl = document.getElementById(
            'newCategoryImageUrl').value;
        if (newName && newColor && newImageUrl) {
          addCategory(newName, newColor, newDescription, newImageUrl);
        } else {
          alert('카테고리 이름, 색상, 그리고 이미지 URL은 필수 입력값입니다.');
        }
      });

  document.getElementById('goToMainPageButton').addEventListener('click',
      function () {
        window.location.href = '/admin'; // Redirect to main page
      });

  // Load categories on page load
  loadCategories();
});
