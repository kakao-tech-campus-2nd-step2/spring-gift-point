document.addEventListener('DOMContentLoaded', function () {
      const authToken = localStorage.getItem('Authorization');
      const url = new URL(window.location)
      const param = url.searchParams.toString()

      if (!authToken) {
        window.location.href = '/members/login';
      } else {
        fetch(`/wishlistPage?${param}`, {
          method: 'GET',
          headers: {
            'Authorization': authToken
          }
        })
        .then(response => {
          if (response.ok) {
            response.text().then(
                html => {
                  document.open()
                  document.write(html)
                  document.close()
                })
          }
        })
      }
    }
)