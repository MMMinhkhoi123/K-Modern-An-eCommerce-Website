

const  url = window.location.origin + window.location.pathname;

function openFrom() {
    window.history.replaceState(null,null,url + "?action=add");
    apiOpenFrom();
}
function openFromUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiGetFromUpdate(id);
}


function controlFrom(e, type) {
    e.preventDefault();
    loaderForm("open");
    const name = document.querySelector(".add-directory__name > input").value;
    const key = document.querySelector(".add-directory__key > input").value;
    if(type === 'update') {
        const  id = document.querySelector(".add-directory__from").getAttribute("data-id");
        const  data = setData(id, name, key)
        apiUpdate(data);
    } else  {
        const  data = setData(null, name, key)
        apiCreate(data);
    }
}

function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

let type = null;
function openDelete(type) {
    const active =  "active__warning__delete";
    const  waring = document.querySelector(".warning__delete");
    const  content = document.querySelector(".delete__matter");
    waring.classList.add(active);
    this.type = type;
    const  array = document.querySelectorAll(".table__check > input");
    let count = 0;
    array.forEach((e) => {
        if(e.checked === true) {
            count = count + 1;
        }
    })
    content.innerText= "Bạn có chắn chắn muốn xóa "+ count + " danh mục này không ?"
}


function outWaring() {
    const active =  "active__warning__delete";
    const  waring = document.querySelector(".warning__delete");
    waring.classList.remove(active);
}

function confirmDelete () {
    loaderForm("open")
    deleteDirectory(this.type);
    clearAll();
}


function deleteDirectory( type){
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(e.getAttribute("data-id"));
        }
    })
    apiDelete(obejct,type);
}

function preCount(count) {
    const show_sum = document.querySelector(".directory__show--sum > span");
    show_sum.innerText = (Number(show_sum.innerText) - count);
}

function clearAll() {
    const  array = document.querySelectorAll(".table__check > input");
    array.forEach((e) => {
        if(e.checked === true) {
           e.checked = false;
        }
    });
    handleDelete();
}


function handleDelete() {
    const classOff = "disable";
    const createEl = document.querySelector(".directory__create > a");
    const deleteEl = document.querySelector(".directory__create > button");
    const  array = document.querySelectorAll(".table__check > input");
    const show_sum = document.querySelector(".directory__show--sum");
    const  show_selected = document.querySelector(".directory__show--selected");
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

function setData(id, name, key) {
    return  {
        id: id,
        name: name,
        key: key
    }
}

function  checked(value) {
    const input = document.querySelector("#" + CSS.escape('input' + value));
    if(input.getAttribute("disabled") == null) {
        input.checked = !input.checked;
        handleDelete()
    }
}

