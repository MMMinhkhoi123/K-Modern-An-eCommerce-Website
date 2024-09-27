function submitFilter(e) {
    e.preventDefault();
    const  option = document.querySelector(".filter-chart-connect > select").value;
    const  year = document.querySelector(".filter-chart-connect > input").value;
    const data = new FormData();
    data.set("year", year);
    data.set('option', option);
    apiGetFilter(data);
}


function  setUpMoney() {
    const  prices = document.querySelectorAll(".setup");
    prices.forEach((e) => {
        e.innerText = assetFormat(e.innerText) + 'VND';
    })
}