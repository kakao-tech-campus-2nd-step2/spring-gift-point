const authSetup = {

    setAuthorizationHeader: function (){
        $.ajaxSetup({
            beforeSend: function(xhr) {
                let token = localStorage.getItem('token');
                if (token) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                }
            }
        });
    },
    storeToken: function (token){
        localStorage.setItem('token', token);
    }
}

// 페이지 로드 시 setAuthorizationHeader 호출
$(document).ready(function() {
    authSetup.setAuthorizationHeader();
});