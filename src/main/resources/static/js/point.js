import { getAuthToken } from './token.js';

function charge() {
    const point = document.querySelector('#point').value;
    console.log(point)
    const token = getAuthToken();

    fetch('/api/points', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({point})
    }).then(response => {
        if (response.status === 201) {
            alert("포인트 충전이 되었습니다.");
            updatePointDisplay();
        } else {
            alert("포인트가 충전되지 않았습니다.");
        }
    });
}

function updatePointDisplay() {
    const token = getAuthToken();

    fetch('/api/points', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(response => response.json())
        .then(data => {
            document.querySelector('#getPoint').textContent = `현재 포인트 값: ${data.point}`;
        }).catch(error => {
        console.error('포인트를 가져오는 중 오류 발생:', error);
        document.querySelector('#getPoint').textContent = '포인트 값을 가져오는 중 오류 발생.';
    });
}


document.addEventListener('DOMContentLoaded', updatePointDisplay);

window.charge = charge;
window.updatePointDisplay = updatePointDisplay;