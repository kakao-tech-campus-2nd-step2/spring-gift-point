document.getElementById('loginForm').addEventListener('submit',
    function (event) {
      event.preventDefault();

      var form = this;
      var formData = new FormData(form);
      var jsonObject = {};

      formData.forEach((value, key) => {
        jsonObject[key] = value;
      });

      fetch(form.action, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonObject)
      })
      .then(response => {
        if (response.status === 200) {
          response.json().then(data => {
            localStorage.setItem("Authorization", data.accessToken)
            window.location.href = "/wishlist"
          })
        }
      })
    });