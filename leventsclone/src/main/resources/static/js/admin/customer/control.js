const classSelect = "select";

$(Window).ready(() => {
    setupPrice();
})


function getSearch() {
    const input = document.querySelector(".filter__customer--search-main > input");
    apiSearch(input.value, getKeyStateSelect());
}


function resetTab() {
    const array = document.querySelectorAll(".option__item");
    array.forEach((e) => {
        if(e.getAttribute('class').includes(classSelect)) {
            e.classList.remove(classSelect);
        }
    })
}



function getKeyStateSelect() {
    let keySelect = "";
    const array = document.querySelectorAll(".option__item");
    array.forEach((e) => {
        if(e.getAttribute('class').includes(classSelect)) {
            keySelect = e.getAttribute('data-key');
        }
    });
    return keySelect;
}




function changeTab(key) {
    resetTab();
    const Select = document.querySelector("#" + CSS.escape(key));
    Select.classList.add(classSelect);
    setURl(Select.getAttribute('data-key'));
    apiGetState(Select.getAttribute('data-key'));
}



function setURl(key) {
    const  url = window.location.origin + "/admin/customer?state=" + key;
    window.history.replaceState(null,null,url);
}


function setupPrice() {
    const prices = document.querySelectorAll(".price__order");
    prices.forEach((e) => {
        e.innerText = assetFormat(e.innerText) + " VNĐ"
    })
}
function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}