
$(window).ready(function() {

    var urlParamsUpdated = new URLSearchParams(window.location.search);
    var paramValueColor = urlParamsUpdated.get('color');
    var paramValuePrice = urlParamsUpdated.get('price');
    var paramValueSize = urlParamsUpdated.get('size');

    const colors = document.querySelectorAll(".color__dropdown--menu > .dropdown__item");
    if(paramValueColor != null) {
        const dataArrayColor =  paramValueColor.split(",");
        colors.forEach((item) => {
            if(dataArrayColor.includes(item.getAttribute("data-color"))) {
                item.classList.add("active-color");
            }
        })
    }

    const sizes = document.querySelectorAll(".size__dropdown--menu > .dropdown__item > label > input");
    if(paramValueSize != null) {
        const dataArraySize =  paramValueSize.split(",");
        sizes.forEach((item) => {
            let str = item.getAttribute("data-size");
            if(dataArraySize.includes(str)) {
                item.checked = true;
            }
        })
    }

    const prices = document.querySelectorAll(".price__dropdown--menu > .dropdown__item > label > input");
    if(paramValuePrice != null) {
        const dataArrayPrice =  paramValuePrice.split(",");
        prices.forEach((item) => {
            if(dataArrayPrice.includes(item.getAttribute("data-price"))) {
                item.checked = true;
            }
        })
    }

});

function handleGetFilters(color, size, price, page, sort) {
    const url = document.getElementById("urlPage").getAttribute("data-url");
    const dataReset = document.querySelector(".collections-list__below");
    dataReset.innerHTML = "";
    const  top = document.querySelector(".header__top");
    top.scrollIntoView();
    const  nagivation = document.querySelector(".pagination-parent");
    nagivation.innerHTML = "";
    const parentLoad = document.querySelector(".collections__loading");
    parentLoad.innerHTML = loadingCollection();
    $.get("/fragments/" + url.toString(), { color: color, size: size, price: price, page: page, sort: sort }, function(data) {
         $("#collection-view").html(data);
        formatMoneyV2();
        FormatMoney();
        setResponsive();
    });
    $.get("/fragments/filter-list", { color: color, size: size, price: price }, function(data) {
        $("#collection__select--parent").html(data);
    });
}

function  setResponsive() {
    function myFunction(x) {
        if (x.matches) { // If media query matches
            const  data = document.querySelector(".collections-list__above--left > span");
            const dataSet = document.querySelector(".collection__result > span:nth-child(2)");
            dataSet.innerText = data.innerText;
        }
    }
    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);
}


function loadingCollection() {

    const  ret = "" +
        "<div class=\"collections__mobile-loading\">\n" +
        "            <div class=\"item__loading\">\n" +
        "                <div class=\"item__loading-img skeleton\"></div>\n" +
        "                <div class=\"item__loading-content1 skeleton\"></div>\n" +
        "                <div class=\"item__loading-content2 skeleton\"></div>\n" +
        "            </div>\n" +
        "            <div class=\"item__loading\">\n" +
        "                <div class=\"item__loading-img skeleton\"></div>\n" +
        "                <div class=\"item__loading-content1 skeleton\"></div>\n" +
        "                <div class=\"item__loading-content2 skeleton\"></div>\n" +
        "            </div>\n" +
        "            <div class=\"item__loading\">\n" +
        "                <div class=\"item__loading-img skeleton\"></div>\n" +
        "                <div class=\"item__loading-content1 skeleton\"></div>\n" +
        "                <div class=\"item__loading-content2 skeleton\"></div>\n" +
        "            </div>\n" +
        "            <div class=\"item__loading\">\n" +
        "                <div class=\"item__loading-img skeleton\"></div>\n" +
        "                <div class=\"item__loading-content1 skeleton\"></div>\n" +
        "                <div class=\"item__loading-content2 skeleton\"></div>\n" +
        "            </div>\n" +
        "        </div>";
    return ret;
}



function handlePreUrlParam(type,valueUSe) {

    let urlUSe = "";

    let urlStandard = window.location.href.split(type);
    let urlFake = urlStandard[1].split("&");

    if(urlFake.length > 1) {
        const m = handlePreUrlMain(urlFake[0], valueUSe, type);
        let url = urlStandard[0] +  m + "&" + urlFake[1];
        if(m.trim() === type) {
            url = url.split(type + "&")[0] + url.split(type + "&")[1];
        }
        urlUSe = url;
        console.log(urlUSe);
    }  else  {
        let urlChild = urlFake[0].split(",");
        if(urlChild.length > 1) {
            let url = urlStandard[0] + handlePreUrlMain(urlFake[0], valueUSe, type);
            urlUSe = url;
            console.log(url);
        } else  {
            let urlStandards = window.location.href.split("&" + type);
            let urlRepair = urlStandard[0].split("?");
            if(urlRepair.length > 1) {
                let urlRepairs = urlRepair[0] +"?" + urlRepair[1];

                if(urlRepairs.split("?")[1] !== "") {
                    let x = urlRepairs.split("?")[1].charAt(urlRepairs.split("?")[1].length -1);
                    if(x.trim() === "&") {
                        urlUSe = urlRepairs.slice(0, urlRepairs.length - 1);
                    } else  {
                        urlUSe = urlRepairs;
                    }
                    console.log(urlUSe);
                } else  {
                    urlUSe = urlRepair[0] + urlRepair[1];

                }
            } else {
                let urlRepair = urlStandard[0].split("?")[0].trim();
                urlUSe = urlRepair;
                console.log(urlRepair);
            }
        }
    }
    window.history.pushState(null, null, urlUSe);
    var urlParamsUpdated = new URLSearchParams(window.location.search);
    var paramValueColor = urlParamsUpdated.get('color');
    var paramValuePrice = urlParamsUpdated.get('price');
    var paramValueSize = urlParamsUpdated.get('size');
    var paramValuePage = urlParamsUpdated.get('page');
    var paramValueSort = urlParamsUpdated.get('sort');
    handleGetFilters(paramValueColor, paramValueSize, paramValuePrice, paramValuePage,paramValueSort);
}



function  handlePreUrlMain(urlFake, valueUSe, type) {
    const  indexKey = urlFake.indexOf(valueUSe);
    if(indexKey === 1) {
        if(urlFake.split(",").length > 1) {
            let url = urlFake.split(valueUSe+",")
            return type + url[0] + url[1];
        } else  {
            let url = urlFake.split("="+ valueUSe);
            return type + url[0] + url[1]
        }
    } else  {
        let url = urlFake.split(","+ valueUSe)
        return  type + url[0] + url[1]
    } // Kết quả: "Heo Word"
}


function handleUrlParam(type, value) {
    let url = "";
    let urlParams = new URLSearchParams(window.location.search);
    let paramValue = urlParams.get(type);
    let urlStandard = window.location.href;

    if(paramValue == null) {
        if(urlStandard.split("?").length > 1) {
            url = urlStandard +  "&"+type+"="+ value;
        } else {
            url = urlStandard +  "?"+type+"="+ value;
        }
    } else {
        const urlNew = urlStandard.split(type);
        const urlHandle = urlNew[1].split("&");

        if(type==='page' || type==='sort') {
            if(urlHandle.length > 1) {
                url = urlNew[0] + type +'=' + value  + UrlEnd(urlHandle);
            }else  {
                url = urlNew[0] + type +'=' + value;
            }
        } else {
            if(urlHandle.length > 1) {
                url = urlNew[0] + type+ urlHandle[0].toString() + "," + value  + UrlEnd(urlHandle);
            }else  {
                url = urlNew[0] + type + urlHandle[0].toString() + "," + value;
            }
        }
    }
    window.history.pushState(null, null, url);
    var urlParamsUpdated = new URLSearchParams(window.location.search);
    var paramValueColor = urlParamsUpdated.get('color');
    var paramValuePrice = urlParamsUpdated.get('price');
    var paramValueSize = urlParamsUpdated.get('size');
    var paramValuePage = urlParamsUpdated.get('page');
    var paramValueSort = urlParamsUpdated.get('sort');
    handleGetFilters(paramValueColor, paramValueSize, paramValuePrice, paramValuePage,paramValueSort);
}

function UrlEnd(array) {
    var url = "";
    for (let i = 1 ; i < array.length; i++) {
        url = url  + "&" +  array[i] ;
    }
    return url;
}

function choiceSort(value) {
    const  mySelect = document.getElementById("mySelect");
    handleUrlParam("sort", mySelect.value);
}


function choiceColor(value) {
    const colorItem = document.querySelector("." + CSS.escape('dropdown__item' + value));
    if(colorItem == null) {
        handlePreUrlParam("color",value);
    }else
    if(colorItem.getAttribute("class").includes("active-color")) {
        colorItem.classList.remove("active-color")
        handlePreUrlParam("color",value);
    } else  {
        colorItem.classList.add("active-color");
        handleUrlParam("color", value);
    }
}

function clean() {
    handleGetFilters(null, null, null, null, null)
    const  urlUse = location.origin + location.pathname;
}


function choicePrice(e, value) {
    e.preventDefault();
    const input = document.querySelector("." + CSS.escape('priceInput' + value));
    if(input == null) {
        handlePreUrlParam("price",value);
    }else
    if(!input.checked) {
        input.checked = true;
        handleUrlParam("price", value);
    } else  {
        input.checked = false;
        handlePreUrlParam("price",value);
    }
}

function choiceSize(e, value) {
    let  nameSize = value.replace(" ", "+");
    e.preventDefault();
    const input = document.querySelector("." + CSS.escape('sizeInput' + value.replace(' ', '-')));
    if(input == null) {
        handlePreUrlParam("size",nameSize);
    }else
    if(!input.checked) {
        input.checked = true;
        handleUrlParam("size", nameSize);
    } else  {
        input.checked = false;
        handlePreUrlParam("size",nameSize);
    }
}

function  choicePage(thisPage, value) {
    handleUrlParam("page", value);
    var urlParamsUpdated = new URLSearchParams(window.location.search);
    var paramValueColor = urlParamsUpdated.get('color');
    var paramValuePrice = urlParamsUpdated.get('price');
    var paramValueSize = urlParamsUpdated.get('size');
    var paramValuePage = urlParamsUpdated.get('page');
    var paramValueSort = urlParamsUpdated.get('sort');
    handleGetFilters(paramValueColor, paramValueSize, paramValuePrice, paramValuePage, paramValueSort);
}
function handleNameProduct(value) {
    return  value.replace(" ", "-");
}

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
function formatMoneyV2() {
    const data = document.querySelectorAll(".card__content--price");
    data.forEach((e) => {
        e.innerText = assetFormat(Math.floor(e.innerText).toString()) + " VND";// 1,234,567,890
    })
}


function FormatMoney() {
    const  cards = document.querySelectorAll(".card__parent > .card");
    cards.forEach((e) => {
        const  color = ConvertUrl(e.getAttribute("data-color"));
        const  name = ConvertUrl(e.getAttribute("data-name"));
        var url  =  name + "-" + color;
        e.setAttribute("href", "/products/"+ url);
    });
}
function ConvertUrl(name) {
    const nameLV1= name.replaceAll(" ","-");
    return nameLV1.replaceAll("/","-");
}


function setFilter(type) {
    const data = document.querySelector(".collections__main--filter");
    if(type === 'open') {
        data.style.display = "flex";
    }else  {
        data.style.display = "none";
    }
}