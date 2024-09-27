$(window).ready(() => {
    setUpPrice();
})

const  url = window.location.origin + window.location.pathname;

function openCreate() {
    window.history.replaceState(null,null,url  + "?action=add");
    apiOpenCreate()
}

function openUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiOpenUpdate(id);
}


function handleDelete() {
    const classOff = "disable";
    const createEl = document.querySelector(".create");
    const deleteEl = document.querySelector(".delete");
    const  array = document.querySelectorAll(".table__check > input");
    const show_sum = document.querySelector(".show__sum");
    const  show_selected = document.querySelector(".show__selected");
    const  show__count = document.querySelector(".count__selected");
    let state = true;
    let Item = 0;
    array.forEach((e) => {
        if(e.checked === true) {
            state = false;
            Item = Item + 1;
        }
    });
    show__count.innerText= Item;
    if(!state) {
        deleteEl.classList.remove(classOff);
        createEl.classList.add(classOff);
        show_sum.classList.add(classOff);
        show_selected.classList.remove(classOff);
    }else  {
        deleteEl.classList.add(classOff);
        createEl.classList.remove(classOff);
        show_sum.classList.remove(classOff);
        show_selected.classList.add(classOff);
    }
}

function setUpPrice() {
    const array = document.querySelectorAll(".table__row");
    array.forEach((e) => {
        const grade = e.querySelector(".grade");
        const price = e.querySelector(".price");
        grade.innerText = assetFormat( Math.floor(Number(grade.getAttribute("data-grade"))).toString());
        price.innerText = assetFormat(price.getAttribute("data-price")) + " VNĐ";
    });
    const inputs = document.querySelectorAll(".member__content--right > div > input");
    inputs.forEach((e) => {
        e.value = assetFormat(Number(e.value).toString());
    })
}


function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function inputChange(e) {
    const item = e.target;
    let value = item.value.replaceAll(".", "");
    value = Number(value);
    value = value.toString();
    if(checkInp(value)) {
        item.value = item.value.toString().substring(0, item.value.toString().length - 1);
    }else  {
        const data = document.querySelector(".member__grade > input");
        item.value = assetFormat(value.toString());
        let xSet = (Number(value) / 100).toString().replaceAll(".","");
        xSet = Number(xSet);
        xSet = xSet.toString();
        data.value = assetFormat(xSet.toString());
    }
}

function checkInp(data)
{
    const regex = new RegExp(/[^0-9]/, 'g');
    return data.match(regex);
}


function  fromControl(event,type) {
    loaderForm("open");
    event.preventDefault();
    if(type === "add") {
        apiCreateVoucher(setData());
    }
    else  {
        const  id = document.querySelector(".form-add-member").getAttribute('data-id');
        let  data = setData();
        data.id = Number(id);
        apiUpdateVoucher(data);
    }
}


function setData() {
    const  name = document.querySelector(".member__name > input");
    const  percent = document.querySelector(".member__percent > input");
    const  price = document.querySelector(".member__price > input");
    const  grade = document.querySelector(".member__grade > input");
    return {
        id: null,
        name: name.value,
        price:  Number(price.value.replaceAll(".","")).toString(),
        percent: percent.value,
        grade: Number(grade.value.replaceAll(".","")).toString(),
    };
}



function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function confirmDelete() {
    loaderForm("open");
    Delete()
    clearAll();
}


function Delete() {
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(e.getAttribute("data-id"));
        }
    })
    apiDeleteVoucher(obejct);
}




































