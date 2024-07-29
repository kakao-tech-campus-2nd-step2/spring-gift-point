document.getElementById('register-form').addEventListener('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 동작을 막음

    const form = event.target;
    const data = {
        email: form.email.value,
        password: form.password.value
    };

    fetch(`/members/register`, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                alert(errorData.message);
                throw new Error('Registration failed');
            });
        }
        return response.json();
    })
    .then(data => {
        alert(`토큰: ${data.token}`);
    })
    .catch(error => {
        console.error('Error:', error);
    });
});
