$(window).ready(() => {
    const  array = document.querySelectorAll(".collect__item");
    array.forEach((item) => {
        const  name = item.getAttribute("data-name");
        const  color = item.getAttribute("data-color");
        const  price = item.getAttribute("data-price");
        const  nameFull = name + "/" + color;
       const  data= item.querySelector(".collect__item--right > h4");
        data.innerText = nameFull;
       const  priceEl =  item.querySelector(".collect__item--right > .item__price");
       priceEl.innerText = assetFormat(Math.floor(price).toString()) + " VND";
    })

})

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
function formatMoney() {
    const data = document.querySelectorAll(".collect__item > .collect__item--right >  ");
    data.forEach((e) => {
        e.innerText = assetFormat(Math.floor(e.innerText).toString()) + " VND";// 1,234,567,890
    })
}