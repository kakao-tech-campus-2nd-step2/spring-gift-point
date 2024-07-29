export function setAuthToken(token) {
    return localStorage.setItem('authToken', token);
}

export function getAuthToken() {
    return localStorage.getItem('authToken');
}