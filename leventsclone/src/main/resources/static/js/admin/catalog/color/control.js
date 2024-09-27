function openFrom() {
    apiOpenFromCreate();
    setUpUrl("add");
}

function openFromUpdate(id) {
    apiOpenFromUpdate(id);
    setUpUrlUpdate(id);
}


function controlForm(e, type) {
    loaderForm("open");
    e.preventDefault();
    const name = document.querySelector(".from-add-color__name > input").value;
    const code = document.querySelector(".from-add-color__code--left > input").value;
    if(type === "add") {
        const  data = setData(null, name, code);
        apiCreate(data);
    }else  {
        const  id = document.querySelector(".from-add-color").getAttribute("data-id");
        const  data = setData(id, name, code);
        apiUpdate(data);
    }
}


function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function setData(id, name, code) {
    return {
        id: id,
        name: name,
        code: code
    }
}
const  url = window.location.origin + window.location.pathname;

function changeColor(item) {
    const  model = document.querySelector(".color__model");
    model.style.background = item.value;
}



function setUpUrl(value){

    window.history.replaceState(null,null,url + "?action="+value);
}

function setUpUrlUpdate(value){
    window.history.replaceState(null,null,url + "?update="+value);
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

function  checked(value) {
    const input = document.querySelector("#" + CSS.escape('input' + value));
    if(input.getAttribute("disabled") == null) {
        input.checked = !input.checked;
        handleDelete()
    }
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

let  type = null;

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
    content.innerText= "Bạn có chắn chắn muốn xóa "+ count + " màu này không ?"
}


function outWaring() {
    const active =  "active__warning__delete";
    const  waring = document.querySelector(".warning__delete");
    waring.classList.remove(active);
}

function confirmDelete() {
    loaderForm("open")
    deleteC(this.type);
    clearAll();
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


function preCount(count) {
    const show_sum = document.querySelector(".show__sum > span");
    show_sum.innerText = (Number(show_sum.innerText) - count);
}


