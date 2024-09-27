const  url = window.location.origin + window.location.pathname;

$(window).ready(() => {
    setUp();
    setUpDate();
})

function setUp() {
    const data = document.querySelectorAll(".date-row");
    data.forEach((e) => {
        e.innerText = formatDate(convertDate2(Number(e.innerText)));
    })
}
function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function formatDate(dateString) {
    var parts = dateString.split('-'); // Split the date string into parts
    var day = parts[2];
    var month = parts[1];
    var year = parts[0];

    // Pad day and month with leading zeros if needed
    if (day.length === 1) {
        day = '0' + day;
    }
    if (month.length === 1) {
        month = '0' + month;
    }

    // Concatenate the parts into the desired format
    return year + '-' + month + '-' + day;
}
function convertDate(value) {
    const date = new Date(value);
    return date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
}
function convertDate2(value) {
    const date = new Date(value);
    return date.getFullYear() + "-0" + (date.getMonth() + 1) + "-" + date.getDate();
}


function goCreate() {
    setUpUrl('add');
    apiGoAdd();
}

function goUpdate(value) {
    setUpUrlUpdate(value);
    apiGoUpdate(value);
}

function goProductAdd(value) {
    setUpUrlProductAdd(value);
    apiGoDetail(value);
}

function setUpUrl(value){
    window.history.replaceState(null,null,url + "?action="+value);
}
function setUpUrlProductAdd(value){
    window.history.replaceState(null,null,url + "?detail="+value);
}

function setUpUrlUpdate(value){
    window.history.replaceState(null,null,url + "?update="+value);
}

function setUpUrlProductUse(value){
    window.history.replaceState(null,null,url + "?detail="+value + "&state=use");
}




function formControl(e, type) {
    e.preventDefault();
    loaderForm("open");
    const dateErr = document.querySelector(".err__date");
    const dateStart = document.querySelector(".item__start > input").value;
    const dateEnd = document.querySelector(".item__end > input").value;
    if(setDate(dateStart) >= setDate(dateEnd) || setDate(dateStart) < new Date().getTime()) {
        dateErr.style.display = "flex";
        loaderForm("close");
    } else  {
        if(type === 'add') {
            const data = getData();
            apiAddEvent(data);
        } else  {
            const  id = document.querySelector(".addevent").getAttribute('data-id');
            const data = getData();
            data.id = id;
            apiUpdateEvent(data);
        }
    }
}

function setDate(value) {
    return new Date(value).getTime();
}


function getData() {
    const name = document.querySelector(".item__name > input").value;
    let path = document.querySelector(".item__path > input").value;
    const dateStart = document.querySelector(".item__start > input").value;
    const dateEnd = document.querySelector(".item__end > input").value;
    path = path.replaceAll(" ", "-");
    path = path.replaceAll("/", "-");
    return {
        id: null,
        name: name,
        path : path,
        dateStart: setDate(dateStart),
        dateEnd: setDate(dateEnd),
    }
}

function setUpDate() {
    const dates  = document.querySelectorAll(".addevent__form--start-end > div");
    dates.forEach((e) => {
        const value = e.getAttribute("data-date");
        if(value != null && value !== "") {
            const dateSet = convertDate2(Number(value));
            const input = e.querySelector("input");
            input.value = formatDate(dateSet);
        }
    })
}


function menuOption(thisElement) {
    const classActive = "active__menu";
    if(!thisElement.getAttribute('class').includes(classActive)) {
        thisElement.classList.add(classActive);
    } else  {
        thisElement.classList.remove(classActive);
    }
}

function chooseAllItem(item) {
    const activeSubmit = "active__submit";
    const items = document.querySelectorAll(".input__item");
    const data = document.querySelector(".productAdd__submit");
    if(item.checked === true) {
        items.forEach((e) => {
            e.checked = true;
        });
        updateCount(items.length);
        openAll(true)
        data.classList.add(activeSubmit);
    } else  {
        items.forEach((e) => {
            e.checked = false;
        });
        openAll(false)
        updateCount(0);
        data.classList.remove(activeSubmit);
    }
}

function openAll(type) {
    const array = document.querySelectorAll(".input_get_value");
    if(type === true) {
        array.forEach((e) => {
            const input = e.querySelector("input");
            input.disabled = false;
        })
    }else  {
        array.forEach((e) => {
            const input = e.querySelector("input");
            input.disabled = true;
        })
    }
}


function chooseAlone(e) {

    const activeSubmit = "active__submit";
    const input = e;

    const id = input.getAttribute("data-id");
    const inputPercent = document.querySelector("." + CSS.escape("input" + id));
    const data = document.querySelector(".productAdd__submit");
    const itemCount = document.querySelector(".count_submit");

    if(input.checked === false) {
        // input.checked = false;
        updateCount(Number(itemCount.innerText) - 1);
        if(Number(itemCount.innerText) === 0) {
            data.classList.remove(activeSubmit);
        }
        inputPercent.disabled = true;
    } else  {
        // input.checked = true;
        updateCount(Number(itemCount.innerText) + 1);
        data.classList.add(activeSubmit);
        inputPercent.disabled = false;
    }


}


function confirmDelete() {
    loaderForm("open")
    deleteC(this.type);
    // clearAll();
}

function deleteC(type) {
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(e.getAttribute("data-id"));
        }
    })
    apiDelete(obejct, type);
}



function updateCount(value) {
    const item = document.querySelectorAll(".count_submit");
    item.forEach((e) => {
        e.innerText = value;
    })
}





function clearChoiceAll() {
    const data = document.querySelector(".productAdd__submit");
    const item = document.querySelector(".input__choice_all");
    const items = document.querySelectorAll(".input__item");
    const activeSubmit = "active__submit";
    items.forEach((e) => {
        e.checked = false;
    });
    data.classList.remove(activeSubmit);
    item.checked = false;
    setDisabledInputAll();
    updateCount(0);
}

function setDisabledInputAll() {
    const array = document.querySelectorAll(".input_get_value");
    array.forEach((e) => {
        const input = e.querySelector("input");
        input.value = 0;
        input.disabled = true;
    })
}

function clearChoice() {
    const classChoice = "active__option2";
    const data = document.querySelectorAll(".menu--list__item");
    data.forEach((e) => {
        if(e.getAttribute("class").includes(classChoice)) {
            e.classList.remove(classChoice);
        }
    });
    const input = document.querySelector(".productAdd__option--menu > input");
    input.value = "";
}

function setValue(thisElement) {
    const button = document.querySelector(".productAdd__option--menu > button");
    const classDisButton = "disable-button";
    if(thisElement.value == null || thisElement.value === "" ) {
        button.classList.add(classDisButton);
    }else
    if(thisElement.value < 0) {
        thisElement.value = 0;
    }else {
       if(thisElement.value > 100) {
           thisElement.value = 100;
       } else  {
           button.classList.remove(classDisButton);
       }
    }
}

function selectedMenu(e) {
    const classChoice = "active__option2";
    const classDisButton = "disable-button";
    e.stopPropagation();
    clearChoice();
    const button = document.querySelector(".productAdd__option--menu > button");
    e.target.classList.add(classChoice);
    if(!e.target.getAttribute('class').includes("input__item-menu")) {
        button.classList.remove(classDisButton);
    }else  {
        if(e.target.value === "" || e.target.value === null  ) {
            button.classList.add(classDisButton);
        }
    }
}


function submitSelect(e) {
    const classChoice = "active__option2";
    e.stopPropagation();
    let data = null;
    const input = document.querySelectorAll(".menu--list__itemSet");
    input.forEach((e) => {
        if(e.getAttribute('data-key') === "item") {
            if(e.getAttribute('class').includes(classChoice)) {
                data = e.getAttribute("data-value");
            }
        }else  {
            if(e.getAttribute('class').includes(classChoice)) {
                data = e.value;
            }
        }
    });
    setDataInputSelect(data);
    const dateItem = document.querySelector(".productAdd__option");
    menuOption(dateItem);
}



function setDataInputSelect(value) {
    const array = document.querySelectorAll(".input_get_value");
    array.forEach((e) => {
        const input = e.querySelector("input");
        input.value = value;
    });
}


function setValue2(thisElement) {
    if(thisElement.value < 0) {
        thisElement.value = 0;
    }else {
        if(thisElement.value > 100) {
            thisElement.value = 100;
        }
    }
}


function submitAddData(e) {
    e.defaultPrevented;
    const  id = e.target.getAttribute("data-id");
    const dataSet = [];
    const items = document.querySelectorAll(".input__item");
    items.forEach((e) => {
        if(e.checked === true) {
            const  id = e.getAttribute("data-id");
            const percent = document.querySelector("." + CSS.escape("input" + id));
            const data = {
                idProduct: id,
                percent: percent.value,
            }
            dataSet.push(data);
        }
    });
     apiAddProductNew(dataSet, id);
}



function changeTab(item) {
    const classActive = "active__option";
    const id = document.querySelector(".productAdd__submit > button").getAttribute("data-id");
    const  type = item.getAttribute("data-type");
    const array = document.querySelectorAll(".productAdd__select > div");
    array.forEach((e) => {
        e.classList.remove(classActive);
    });
    item.classList.add(classActive);
    if(type === "use") {
        setUpUrlProductUse(id);
        apiGoDetailUse(id);
    }else  {
        setUpUrlProductAdd(id);
        apiGoDetailNotUse(id);
    }
}


function removeProductEvent(e) {
    e.defaultPrevented;
    const  id = e.target.getAttribute("data-id");
    const dataSet = [];
    const items = document.querySelectorAll(".input__item");
    items.forEach((e) => {
        if(e.checked === true) {
            const  id = e.getAttribute("data-id");
            const percent = document.querySelector("." + CSS.escape("input" + id));
            const data = {
                idProduct: id,
                percent: percent.value,
            }
            dataSet.push(data);
        }
    });
    apiDeleteProductNew(dataSet, id);
}


function updateProductEvent(e) {
    e.defaultPrevented;
    const  id = e.target.getAttribute("data-id");
    const dataSet = [];
    const items = document.querySelectorAll(".input__item");
    items.forEach((e) => {
        if(e.checked === true) {
            const  id = e.getAttribute("data-id");
            const percent = document.querySelector("." + CSS.escape("input" + id));
            const data = {
                idProduct: id,
                percent: percent.value,
            }
            dataSet.push(data);
        }
    });
    apiUpdateProductEvent(dataSet, id);
}






































