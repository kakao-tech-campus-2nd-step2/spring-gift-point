document.addEventListener('DOMContentLoaded', function() {
    var tokenDisplayElement = document.getElementById('tokenDisplay');
    var token = tokenDisplayElement.innerText;
    localStorage.setItem('access_token', token);

    fetch(`/kakao/wish`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem("access_token")
            }
        })
        .then(response => {
                if (!response.ok) {
                    return response.text().then(errorText => {
                        throw new Error(`Failed to fetch wishlist: ${errorText}`);
                    });
                }
                return response.text();
            })
        .then(text => {
                document.open();
                document.write(text);
                document.close();
            })
        .catch(error => {
            console.error('Error:', error);
            alert(`An error occurred: ${error.message}`);
        });
});