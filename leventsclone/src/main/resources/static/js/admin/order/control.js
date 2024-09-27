const classSelect = "select";
const classDisabled = "disable__menu";
let imgFile = null;
let imgFileRefund = null;

$(window).ready(() => {
    setUpdate();
    setupPrice();
    setupPriceDetail();
    setupPriceDetailSum();
    setColorTransaction();
})

function setUpdate() {
    const items = document.querySelectorAll('.date__item');
    items.forEach((e) => {
        e.innerText = setDate(e.innerText);
    })
}

function openLoadUpload(thisElement, index) {
    thisElement.classList.add("hidden");
    const loader = document.querySelector("#" + CSS.escape('loader'  + index));
    loader.classList.remove("hidden");
}


function uploadImgRefund(thisElement) {
    if(imgFileRefund === null) {
        alert("Chọn hình ảnh !");
    } else  {
        openLoadUpload(thisElement, 2);
        const data = new FormData();
        data.set('file', imgFileRefund);
        const code = document.querySelector(".order__detail").getAttribute("data-code");
        apiUploadRefund(code ,data);
    }
}

function hiddenLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.remove(classActive);
}
function openLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.add(classActive);
}


function uploadImg(thisElement) {
    if(imgFile === null) {
        alert("Chọn hình ảnh !");
    } else  {
        openLoadUpload(thisElement, 1);
        const data = new FormData();
        data.set('file', imgFile);
        const code = document.querySelector(".order__detail").getAttribute("data-code");
        apiUpload(code,data);
    }
}


function renderImgRefund(e) {
    const file = e.target.files[0];
    if(file.type.substring(0, 5) !== 'image') {
        alert("Chọn hình ảnh !");
    }else  {
        imgFileRefund = file;
        const item = document.querySelector(".imgUpload2");
        var reader  = new FileReader();
        reader.onloadend = function () {
            item.src = reader.result;
        }
        if (file) {
            reader.readAsDataURL(file);
        } else {
            item.src = "";
        }
    }
}
function renderImg(e) {
    const file = e.target.files[0];
    if(file.type.substring(0, 5) !== 'image') {
        alert("Chọn hình ảnh !");
    }else  {
        imgFile = file;
        const item = document.querySelector(".imgUpload");
        var reader  = new FileReader();
        reader.onloadend = function () {
            item.src = reader.result;
        }
        if (file) {
            reader.readAsDataURL(file);
        } else {
            item.src = "";
        }
    }
}

function setDate(date) {
    const s = new Date(Number(date));
    return s.getDate() + "/" + Number(s.getMonth() + 1) + "/" + s.getFullYear() + " " + s.getHours() + ":" + s.getMinutes();
}
function setupPriceDetail() {
    const prices = document.querySelectorAll(".info__price > span > strong");
    prices.forEach((e) => {
        e.innerText = assetFormat( Math.floor(Number(e.innerText)).toString()) + " VNĐ"
    });
}

function setupPriceDetailSum() {
    const prices = document.querySelectorAll(".info__price-detail");
    prices.forEach((e) => {
        e.innerText = assetFormat( Math.floor(Number(e.innerText)).toString()) + " VNĐ"
    });
}

function setupPrice() {
    const prices = document.querySelectorAll(".price__item");
    prices.forEach((e) => {
        e.innerText = assetFormat(e.innerText) + " VNĐ"
    })
}

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function resetTab() {
    const array = document.querySelectorAll(".option__item");
    array.forEach((e) => {
        if(e.getAttribute('class').includes(classSelect)) {
            e.classList.remove(classSelect);
        }
    })
}

function filterSearch() {
    const item = document.querySelector(".filter__option--search > input");
    const parent = document.querySelector(".filter__option--selected");
    if(parent != null) {
        const searchKey =  item.value;
        if(!checkItem('search')) {
            parent.innerHTML = parent.innerHTML + createItemSearch(searchKey, searchKey);
        } else  {
            updateSearch(searchKey, searchKey);
        }
        handleUrlSearch(searchKey);

        const dataPost = getDataPost();
        apiFilter(dataPost);
    }
}


function getUrl(search) {
    let url = window.location.origin  + "/admin/order?" ;
    if( window.location.search !== "") {
        url =  window.location.origin  + "/admin/order" +  window.location.search;
    }
    if(search != null && search !== "") {
        url = url + "q=" + search;
    }
    return url;
}


const  url = window.location.origin + window.location.pathname;



function goDetailOrder(key) {
    let urls = url + "?detail=" + key + "&type=products&target=product";
    window.history.pushState(null, null, urls);
    apiGoDetail(key);
}





function changeTab(key) {
    resetTab();
    const Select = document.querySelector("#" + CSS.escape(key));
    Select.classList.add(classSelect);
    setURl(Select.getAttribute('data-key'));
    apiFilterOverView(key.toLocaleUpperCase())
}
function setURl(key) {
    const  urls = url + "?state=" + key;
    window.history.replaceState(null,null,urls);
}


function openMenu(key) {
    resetMenu(key);
    const item = document.querySelector("#" + CSS.escape('menu__' + key));
    if(!item.getAttribute('class').includes(classDisabled)) {
        item.classList.add(classDisabled);
    } else  {
        item.classList.remove(classDisabled);
    }
}



function filterDate() {
    let dataFilter = null;
    let dataKey = null;

    const dataInput = document.querySelector(".date__input");
    if(dataInput.value !== "") {
        dataFilter = dataInput.value;
        dataKey = dataInput.value;
    }else  {
        const datas = document.querySelectorAll(".option--date--menu > li > label > input");
        datas.forEach((e) => {
            if(e.checked) {
                dataFilter = e.value;
                dataKey = e.getAttribute('data-key');
            }
        });
    }
    const parent = document.querySelector(".filter__option--selected");
    if(!checkItem('date')) {
        parent.innerHTML = parent.innerHTML + createItemDate(dataFilter, dataKey);
    } else  {
        updateDate(dataFilter, dataKey);
    }
    handleUrlDate(dataKey);

    const dataPost = getDataPost();
    apiFilter(dataPost);
}



function filterState() {
    const parents = document.querySelectorAll('.option__item');
    parents.forEach((e) => {
        if(e.getAttribute('class').includes('select')) {
            e.classList.remove('select')
        }
    })
    let dataFilter = null;
    let dataKey = null;
    const datas = document.querySelectorAll(".option--state--menu > li > label > input");
    datas.forEach((e) => {
        if(e.checked) {
            dataFilter = e.value;
            dataKey = e.getAttribute('data-key');
        }
    });
    const parent = document.querySelector(".filter__option--selected");
    if(!checkItem('state')) {
        parent.innerHTML = parent.innerHTML + createItemState(dataFilter,dataKey);
    } else  {
        updateState(dataFilter, dataKey);
    }
    handleUrlState(dataKey);


    const dataPost = getDataPost();
    apiFilter(dataPost);

}



function getDataPost() {
    return {
        state : getDataState(),
        q: getDataSearch(),
        pay: getDataPay(),
        date: getDataDate(),
    }
}

function getDataState() {
    const data = document.querySelector('.filter__option--selected > #state');
    if(data === null) {
        return  "";
    }else  {
        const dataResult = data.querySelector('span');
        return dataResult.getAttribute('data-key');
    }
}
function getDataPay() {
    const data = document.querySelector('.filter__option--selected > #pay');
    if(data === null) {
        return  "";
    }else  {
        const dataResult = data.querySelector('span');
        return dataResult.getAttribute('data-key');
    }
}
function getDataDate() {
    const data = document.querySelector('.filter__option--selected > #date');
    if(data === null) {
        return  "";
    }else  {
        const dataResult = data.querySelector('span');
        return dataResult.getAttribute('data-key');
    }
}
function getDataSearch() {
    const data = document.querySelector('.filter__option--selected > #q');
    if(data === null) {
        return  "";
    }else  {
        const dataResult = data.querySelector('span');
        return dataResult.getAttribute('data-key');
    }
}



function filterPay() {
    let dataFilter = null;
    let dataKey = null;
    const datas = document.querySelectorAll(".option--pay--menu > li > label > input");
    datas.forEach((e) => {
        if(e.checked) {
            dataFilter = e.value;
            dataKey = e.getAttribute('data-key');
        }
    });
    const parent = document.querySelector(".filter__option--selected");
    if(!checkItem('pay')) {
        parent.innerHTML = parent.innerHTML + createItemPay(dataFilter, dataKey);
    } else  {
        updatePay(dataFilter, dataKey);
    }
    handleUrlPay(dataKey);

    const dataPost = getDataPost();
    console.log(dataPost);
    apiFilter(dataPost);
}


function handleUrlSearch(key) {
    const search = window.location.search;
    const origin =  window.location.origin;
    const href =  window.location.href;
    if(search === "") {
        const  url = origin + "/admin/order?" + "q=" + key
        window.history.pushState(null, null, url);
    } else  {
        let urlPath = "q=";
        let  indexCut = null;
        let  indexCutEnd= null;

        if(search.includes(urlPath)) {
            if(search.includes("?" + urlPath)) {
                indexCut = search.indexOf("?" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut);
                if(indexCutEnd === -1) {
                    urlFirstSearch(search, indexCut, key, "?");
                }else  {
                    urlBetweenSearch(search, indexCut, indexCutEnd, key, "?")
                }
            } else  {
                indexCut = search.indexOf("&" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut + 1);
                if(indexCutEnd ===-1) {
                    urlFirstSearch(search, indexCut, key, "&");
                }else  {
                    urlBetweenSearch(search, indexCut, indexCutEnd, key, "&")
                }
            }
        } else  {
            const  url = href + "&q=" + key
            window.history.pushState(null, null, url);
        }
    }
}

function handleUrlState(key) {
    const search = window.location.search;
    const origin =  window.location.origin;
    const href =  window.location.href;
    if(search === "") {
        const  url = origin + "/admin/order?" + "state=" + key
        window.history.pushState(null, null, url);
    } else  {
        let urlPath = "state=";
        let  indexCut = null;
        let  indexCutEnd= null;

        if(search.includes(urlPath)) {
            if(search.includes("?" + urlPath)) {
                indexCut = search.indexOf("?" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut);
                if(indexCutEnd === -1) {
                    urlFirstState(search, indexCut, key, "?");
                }else  {
                    urlBetweenState(search, indexCut, indexCutEnd, key, "?")
                }
            } else  {
                indexCut = search.indexOf("&" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut + 1);
                if(indexCutEnd ===-1) {
                    urlFirstState(search, indexCut, key, "&");
                }else  {
                    urlBetweenState(search, indexCut, indexCutEnd, key, "&")
                }
            }
        } else  {
            const  url = href + "&state=" + key
            window.history.pushState(null, null, url);
        }
    }
}


function handleUrlDate(key) {
    const search = window.location.search;
    const origin =  window.location.origin;
    const href =  window.location.href;
    if(search === "") {
        const  url = origin + "/admin/order?" + "date=" + key
        window.history.pushState(null, null, url);
    } else  {
        let urlPath = "date=";
        let  indexCut = null;
        let  indexCutEnd= null;

        if(search.includes(urlPath)) {
            if(search.includes("?" + urlPath)) {
                indexCut = search.indexOf("?" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut);
                if(indexCutEnd ===-1) {
                    urlFirstDate(search, indexCut, key, "?");
                } else  {
                    urlBetweenDate(search, indexCut, indexCutEnd, key, "?")
                }
            } else  {
                indexCut = search.indexOf("&" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut + 1);
                if(indexCutEnd ===-1) {
                    urlFirstDate(search, indexCut, key, "&");
                } else  {
                    urlBetweenDate(search, indexCut, indexCutEnd, key, "&")
                }
            }
        } else  {
            const  url = href + "&date=" + key
            window.history.pushState(null, null, url);
        }
    }
}

function handleUrlPay(key) {
    const search = window.location.search;
    const origin =  window.location.origin;
    const href =  window.location.href;
    if(search === "") {
       const  url = origin + "/admin/order?" + "pay=" + key
        window.history.pushState(null, null, url);
    } else  {
        let urlPath = "pay=";
        let  indexCut = null;
        let  indexCutEnd= null;

        if(search.includes(urlPath)) {
            if(search.includes("?" + urlPath)) {
                indexCut = search.indexOf("?" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut);
                if(indexCutEnd ===-1) {
                    urlFirstPay(search, indexCut, key, "?");
                }
            } else  {
                indexCut = search.indexOf("&" + urlPath);
                indexCutEnd = search.indexOf("&", indexCut + 1);
                if(indexCutEnd ===-1) {
                    urlFirstPay(search, indexCut, key, "&");
                }
            }
        } else  {
            const  url = href + "&pay=" + key
            window.history.pushState(null, null, url);
        }
    }
}
function urlBetweenDate(search, indexCut, indexCutEnd, key, keyChoice) {
    const lastSearch = search.substring(indexCutEnd, search.length);
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"date=" + key + lastSearch;
    window.history.pushState(null, null, url);
}
function urlBetweenSearch(search, indexCut, indexCutEnd, key, keyChoice) {
    const lastSearch = search.substring(indexCutEnd, search.length);
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"q=" + key + lastSearch;
    window.history.pushState(null, null, url);
}
function urlBetweenState(search, indexCut, indexCutEnd, key, keyChoice) {
    const lastSearch = search.substring(indexCutEnd, search.length);
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"state=" + key + lastSearch;
    window.history.pushState(null, null, url);
}
function urlFirstSearch(search, indexCut, key, keyChoice) {
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"q=" + key;
    window.history.pushState(null, null, url);
}
function urlFirstState(search, indexCut, key, keyChoice) {
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"state=" + key;
    window.history.pushState(null, null, url);
}
function urlFirstPay(search, indexCut, key, keyChoice) {
        const hrefResult = search.substring(0, indexCut);
        const  url = origin + "/admin/order" + hrefResult + keyChoice +"pay=" + key;
        window.history.pushState(null, null, url);
}
function urlFirstDate(search, indexCut, key, keyChoice) {
    const hrefResult = search.substring(0, indexCut);
    const  url = origin + "/admin/order" + hrefResult + keyChoice +"date=" + key;
    window.history.pushState(null, null, url);
}

function updateSearch(text, key) {
    const documentPay = document.querySelector(".filter__option--selected > .search > span");
    documentPay.innerText = text;
    documentPay.setAttribute('data-key',key);
}

function updateDate(text, key) {
    const documentPay = document.querySelector(".filter__option--selected > .date > span");
    documentPay.innerText = text;
    documentPay.setAttribute('data-key',key);
}
function updatePay(text, key) {
    const documentPay = document.querySelector(".filter__option--selected > .pay > span");
    documentPay.innerText = text;
    documentPay.setAttribute('data-key',key);
}
function updateState(text, key) {
    const documentPay = document.querySelector(".filter__option--selected > .state > span");
    documentPay.innerText = text;
    documentPay.setAttribute('data-key',key);
}


function checkItem(type) {
    let state = false
    const items = document.querySelectorAll('.selected__item');
    items.forEach((e) => {
        if(e.getAttribute('class').includes(type)) {
            state = true;
        }
    })
    return state;
}

function capitalizeFirstLetter(string) {
    return string[0].toUpperCase() + string.slice(1);
}

function createItemSearch(value, key) {
    return " <div class=\"selected__item search\" id='q'>\n" +
        "            <span data-key='"+ key +"'>" + value + "</span>" +
        " <i class=\"fa-solid fa-xmark\" onclick=\"removeSelection(\'search\', '" + key + "' )\"></i>\n" +
        "        </div>";
}
function createItemDate(value, key) {
    return " <div class=\"selected__item date\" id='date'>\n" +
        "            <span data-key='"+ key +"'>" + value + "</span>" +
        " <i class=\"fa-solid fa-xmark\" onclick=\"removeSelection(\'date\', '" + key + "' )\"></i>\n" +
        "        </div>";
}
function createItemPay(value, key) {
    return " <div class=\"selected__item pay\" id='pay'>\n" +
        "            <span data-key='"+ key +"'>" + value + "</span>" +
        " <i class=\"fa-solid fa-xmark\" onclick=\"removeSelection(\'pay\', '" + key + "' )\"></i>\n" +
        "        </div>";
}
function createItemState(value, key) {
    return " <div class=\"selected__item state\" id='state'>\n" +
        "            <span data-key='"+ key +"'>" + value + "</span>" +
        " <i class=\"fa-solid fa-xmark\" onclick=\"removeSelection(\'state\', '" + key + "' )\"></i>\n" +
        "        </div>";
}

function resetMenu(key) {
    const menus = document.querySelectorAll('.menu__item');
    menus.forEach((e) => {
        if(e.getAttribute('id') !== "menu__"+ key) {
            if(!e.getAttribute('class').includes(classDisabled)) {
                e.classList.add(classDisabled);
            }
        }
    })
}



function changeDate() {
    const dates = document.querySelectorAll('.option--date--menu > li > label > input');
    dates.forEach((e) => {
        e.checked = false;
    })
}

function changeDateSpe() {
    const dataInput = document.querySelector('.date__input');
    dataInput.value = null;
}


function removeSelection(key, value) {
    handleFront(key);
    if(key === 'state') {
        const data = document.querySelector('.option__item');
        data.classList.add('select');
    }
    handleChangeUrl();

    const dataPost = getDataPost();
    apiFilter(dataPost);
}



function handleChangeUrl() {
    let stateFirst = false;
    let url = window.location.origin + "/admin/order?";
    const option__item = document.querySelectorAll('.option__item');
    option__item.forEach((e) => {
        if(e.getAttribute('class').includes('select')) {
            stateFirst = true;
            url = url + 'state=' + e.getAttribute('data-key');
        }
    });


    const items = document.querySelectorAll('.selected__item');
    items.forEach((e) => {
        const key = getKeyUrl(e.getAttribute('id')) + "=";
        const value = e.querySelector('span').getAttribute('data-key');
        if(stateFirst === true) {
            url = url + "&" + key + value
        }else  {
            stateFirst = true;
            url = url + key + value
        }
    });
    if(stateFirst === false) {
        url =  window.location.origin + "/admin/order";
    }
    window.history.pushState(null, null, url);
}





function getKeyUrl(key) {
    let keyData = "";
    switch (key) {
        case "date" : {
            keyData = "date";
            break;
        }
        case "state" : {
            keyData = "state";
            break;
        }
        case "pay" : {
            keyData = "pay";
            break;
        }
        case "search" : {
            keyData = "q";
            break;
        }
    }
    return keyData;
}




function handleFront(key) {
    const parent = document.querySelector(".filter__option--selected");
    const array = document.querySelectorAll('.selected__item');
    let dataSet = "";
    array.forEach((e) => {
        if(!e.getAttribute('class').includes(key)) {
            console.log("hello")
            const classSet = e.getAttribute('class');

            const GetData = e.querySelector('span');
            const key = GetData.getAttribute('data-key');
            const value = GetData.innerText;
            let html = createItemByKey(classSet,key, value);
            dataSet = dataSet + html;
        }
    });
    parent.innerHTML = dataSet;
}
function createItemByKey(classSet,key, value) {
    let html = null;
    if(classSet.includes('date')) {
        html = createItemDate( value, key)
    }else  if (classSet.includes('search')) {
        html = createItemSearch( value,key)
    } else if(classSet.includes('state')) {
        html = createItemState(value, key)
    }else  {
        html = createItemPay(value, key)
    }
    return html;
}




function resetTabDetail() {
    const array = document.querySelectorAll(".tab-left__item");
    array.forEach((e) => {
        if(e.getAttribute('class').includes(classSelect)) {
            e.classList.remove(classSelect);
        }
    })
}
function setURlDetail(code, key) {
    const  url = window.location.origin + "/admin/order?detail=" + code + "&type=products&target=" + key;
    window.history.replaceState(null,null,url);
}

function changeTabDetail(key) {
    resetTabDetail();
    const Select = document.querySelector("#" + CSS.escape('detail__' +  key));
    Select.classList.add(classSelect);
    setURlDetail(Select.getAttribute('data-key'), key);
    apiGoProductsTab(key, Select.getAttribute('data-key'));
}

function closeSum() {
    const input = document.querySelector('.tab-right__item > input');
    input.checked = false;
    const path = document.querySelector('.detail-order__view-parent');
    const side = document.querySelector(".sumPay");
    path.style.paddingRight = 0 + "px";
    side.style.right = -100 + "%";
}
function openSum(e) {
    const  element = e.target;
    const side = document.querySelector(".sumPay");
    if(element.checked === true) {
        side.style.right = 0 + "%";
    }else  {
        side.style.right = -100 + "%";
    }
}


function goTabDetailLarge(code,key) {
    resetTabLarge();
    const item = document.querySelector("." + CSS.escape("order__detail--tab")  + "> #" + CSS.escape(key));
    item.classList.add(classSelect);
    setURlDetailLarge(code, key);
    apiGoProductsTabLarge(code, key);
}


function resetTabLarge() {
    const tab__item = document.querySelectorAll(".tab__item");
    tab__item.forEach((e) => {
        if(e.getAttribute('class').includes(classSelect)) {
            e.classList.remove(classSelect);
        }
    })
}

function setURlDetailLarge(code, key) {
    let  url = window.location.origin + "/admin/order?detail=" + code + "&type=" + key;
    if(key === 'products') {
        url = window.location.origin + "/admin/order?detail=" + code + "&type=" + key + "&target=product";
    }
    window.history.replaceState(null,null,url);
}


function setColorTransaction() {
    const data = document.querySelector(".active__color-process");
    if(data != null) {
        const count = Number(data.getAttribute('data-step'));
        const withSet = document.querySelector('.' + CSS.escape('process' + count)).offsetLeft;
        data.style.width = withSet + "px";
    }
}





