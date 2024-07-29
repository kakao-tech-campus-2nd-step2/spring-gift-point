import { setAuthToken } from './token.js';

function register() {
    const email = document.querySelector('#username').value;
    const password = document.querySelector('#password').value;
    fetch('/members/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    }).then(response => {
        if (response.status === 200) {
            alert("회원가입이 완료되었습니다.");
        } else {
            alert("회원가입이 실패했습니다.");
        }
    });
}

function login() {
    const email = document.querySelector('#username').value;
    const password = document.querySelector('#password').value;

    fetch('/members/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    }).then(response => response.json())
        .then(data => {
            if (data.token) {
                setAuthToken(data.token)
                alert("로그인에 성공했습니다.");
                window.location.href = '/';
            } else {
                alert("로그인에 실패했습니다.");
            }
        });
}

window.register = register;
window.login = login;