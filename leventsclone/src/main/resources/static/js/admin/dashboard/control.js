
function  selectFilter(e){
    const value = e.target.value;
    apiFilter(value);

}

function  setUpMoney() {
    const  prices = document.querySelectorAll(".price");
    prices.forEach((e) => {
        e.innerText = '+ ' + assetFormat(e.innerText) + 'VND';
    })
}
setUpMoney();
function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}





















