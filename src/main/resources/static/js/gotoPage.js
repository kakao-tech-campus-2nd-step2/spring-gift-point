const homeAdminUrl = "/admin";


function gotoHome() {
    window.location.href = `${homeAdminUrl}`;
}

function gotoAdd() {
    window.location.href = `${homeAdminUrl}/add`;
}

const homeButton = document.querySelector("#homeButton");
homeButton.addEventListener("click", gotoHome);

const addButton = document.querySelector("#addButton");
addButton.addEventListener("click", gotoAdd);