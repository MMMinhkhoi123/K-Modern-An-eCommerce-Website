function ChangeType(event) {
    const type = event.target.value;
    const text = document.querySelector(".text__type");
    const input = document.querySelector(".voucher__describe");
    input.value = null;
    if(type === "PERCENT") {
        text.innerText = "% Phần trăm (1 - 100%)";
        input.setAttribute("data-type", "percent");
    } else  {
        text.innerText = "$ Mức tiền";
        input.setAttribute("data-type", "price");
    }
}


const  url = window.location.origin + window.location.pathname;



function chooseCondition(event) {
    const item = event.target;
    const input = document.querySelector(".voucher__restrict > input");
    if(item.checked) {
        input.disabled = false;
    } else  {
        input.value = 0;
        input.disabled = true;
    }
}

function inputChange(e) {
    const item = e.target;
    let value = item.value.replaceAll(".", "");
    value = Number(value);
    value = value.toString();
    if(checkInp(value)) {
        item.value = item.value.toString().substring(0, item.value.toString().length - 1);
    }else  {
        item.value = assetFormat(value.toString());
    }
}

function checkInp(data)
{
    const regex = new RegExp(/[^0-9]/, 'g');
    return data.match(regex);
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


function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
function setUpPrice() {
    const Items = document.querySelectorAll("tbody > tr");
    Items.forEach((e) => {
        const type = e.querySelector(".voucher__type");
        const value = e.querySelector(".voucher__value");
        const condition = e.querySelector(".voucher__condition");
        if(type.getAttribute("data-type") === 'PERCENT') {
            condition.innerText =  assetFormat(Math.floor(Number(condition.getAttribute("data-condition"))).toString());
            value.innerText = Math.floor(Number(value.getAttribute("data-value"))) + "%";
        } else  {
            value.innerText = assetFormat(Math.floor(Number(value.getAttribute("data-value"))).toString()) + " VNĐ";
            condition.innerText =  assetFormat(Math.floor(Number(condition.getAttribute("data-condition"))).toString());
        }

    })
}


$(Window).ready(() => {
    setUpPrice();
})


function openUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiOpenUpdate(id);
}

function openCreate() {
    window.history.replaceState(null,null, url + "?action=add");
    apiOpenCreate();
}

function  fromControl(event,type) {
    loaderForm("open");
    event.preventDefault();
    if(type === "add") {
        apiCreateVoucher(setData());
    }
    else  {
        const  id = document.querySelector(".add__voucher--form").getAttribute('data-id');
        let  data = setData();
        data.id = Number(id);
        apiUpdateVoucher(data);
    }
}


function setData() {
    const  name = document.querySelector(".voucher__describe > input");
    const code = document.querySelector(".voucher__code > input");
    const type = document.querySelector(".voucher__type > select");
    const value = document.querySelector(".voucher__value > input");
    const  condition = document.querySelector(".voucher__restrict > input");

    let dataExtra = {
        id: null,
        code: code.value,
        name : name.value,
        type: type.value,
        priceCondition:  Number(condition.value.replaceAll(".","")),
        price: 0,
        percent: 0,
    };

    if(type.value === "PERCENT") {
        dataExtra.percent = Number(value.value);
    }else  {
        dataExtra.price = Number(value.value.replaceAll(".",""));
    }
    return dataExtra;
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





























