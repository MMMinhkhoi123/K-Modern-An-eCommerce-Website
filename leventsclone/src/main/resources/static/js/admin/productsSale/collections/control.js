
$(window).ready(() => {
    handleSelectedUpdate();
    if(dataHIx.describe !== null) {
        setArea(dataHIx.describe);
    }else  {
        setArea("");
    }
})


function setArea(value) {
    const ed = new tinymce.Editor('collection_describe', {
    some_setting: 1,
        toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
        plugins: 'image',
        setup: ((editor) => {
            editor.on('init', function (e) {
                    editor.setContent(value);
            });
        })
    }, tinymce.EditorManager);
    ed.render();
}

function handleDelete() {
    const classOff = "disable";
    const createEl = document.querySelector(".color-section__option--setting > button:nth-child(2)");
    const deleteEl = document.querySelector(".color-section__option--setting > button:nth-child(1)");
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


function handleSelectedUpdate() {
    const  array = document.querySelectorAll(".item__list");
    array.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            const  x = e.querySelector("i");
            const  name = x.getAttribute("data-name");
            const  id = x.getAttribute("data-id");
            const  dataUse = id + "-" + name;
            selectedItem(dataUse);
        }
    })
}


function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}


function renderImg(event) {
    let image = document.querySelector(".portion__left--avatar > img");
    let file = event.target.files[0];
    let reader  = new FileReader();
    reader.onload = function(e)  {
        image.src = e.target.result;
    }
    reader.readAsDataURL(file);
}

function openForm() {
    setUpUrl("add");
    apiOpenForm()
}


function handleChangeProduct(event) {
    const  initData = event.target.value.split("-");
    const  id = initData[0];
    const  name = initData[1];
    const  item = event.target;
    selectedItem(item.value);
    createItem(id, name);
    emptyList("close");
}


function emptyList(type) {
    const classDisabled = "disable";
    const  empty = document.querySelector(".product-choice__list--empty");
    if(type === "open") {
        empty.classList.remove(classDisabled);
    }else  {
        empty.classList.add(classDisabled);
    }
}

function  createItem(id, name) {
    const  Item = document.querySelector(".product-choice__list");
    const  div = document.createElement("div");
    div.classList.add("item__list");
    div.id  = "item" + id;
    div.setAttribute("data-id", id);
    div.innerHTML = "  <i class=\"fa-solid fa-xmark\" onclick=\"hiddenItem(" + id + ",'" + name +"')\"></i>\n" +
        "          <span>"+ name +"</span>";
    Item.append(div);
}
function hiddenItem(id, name) {
    const data = id + "-" + name;
    const classDisabled = "disable";
    const  item = document.querySelector("#" + CSS.escape("item" + id));
    item.classList.add(classDisabled);
    checkInclude();
    reselectedItem(data)
}

function checkInclude() {
    const classDisabled = "disable";
    const  listEmpty = document.querySelector(".product-choice__list--empty");
    const  array = document.querySelectorAll(".product-choice__list > div:not(.product-choice__list--empty)");
    let check = true;
    array.forEach((e) => {
        if(!e.getAttribute("class").includes(classDisabled)) {
            check = false;
        }
    })
    if(check) {
        listEmpty.classList.remove(classDisabled);
    }
}

function selectedItem(value) {
    const  array = document.querySelectorAll(".portion__right--product > select > option");
    array.forEach((e) => {
        if(e.getAttribute("value") === value) {
            e.disabled = true;
        }
    })
}

const  url = window.location.origin + window.location.pathname;
function reselectedItem(value) {
    const  array = document.querySelectorAll(".portion__right--product > select > option");
    array.forEach((e) => {
        if(e.getAttribute("value") === value) {
            e.disabled = false;
        }
    })
}



function setUpUrl(value){
    window.history.replaceState(null,null,url  + "?action="+value);
}
function setUpUrlUpdate(value){
    window.history.replaceState(null,null,url + "?update="+value);
}


function goFormUpdate(id) {
    setUpUrlUpdate(id);
    apiOpenUpdate(id);
}



function setData() {
    return {
        name: null,
        file: null,
        product: null,
        describe: null,
    }
}

function getItem() {
 const  item = document.querySelectorAll(".item__list")
    let  listId = [];
    item.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            const  id = Number(e.getAttribute("data-id"));
            listId.push(id);
        }
    });
    return listId;
}


function setUpFrom(name, dark, products, describe, file) {
    const  from = new FormData();
    from.set("name", name);
    from.set("file", file);
    from.set("dark", dark);
    from.set("product", products);
    from.set("describe",describe);
    return from;

}

function controlForm(e,type) {
    e.preventDefault();
    const  name = document.querySelector(".portion__right--name > input").value;
    const  item =  getItem();
    const  dark = document.querySelector(".avatar__type-input > input").checked;
    const  img  = document.querySelector(".portion__left--avatar > input").files[0];
    let myContent = tinyMCE.activeEditor.getContent();
    const  data = setUpFrom(name, dark, item, myContent, img);
    if(type === "add") {
        openLoader();
        apiCreate(data);
    }
    else {
        const  id = document.querySelector(".collection__form").getAttribute("data-id");
        openLoader();
        data.set("id", id);
        apiUpdate(data);
    }
}

function confirmDelete() {
    const  array = document.querySelectorAll(".table__check > input");
    let object = [];
    array.forEach((e) => {
        if(e.checked === true) {
            object.push(e.getAttribute("data-id"));
        }
    })
    openLoader();
    apiDelete(object);
    clearAll();
}
