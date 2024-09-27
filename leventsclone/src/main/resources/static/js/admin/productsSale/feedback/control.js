$(window).ready(() => {
    setUpUpdate();
})

const  url = window.location.origin + window.location.pathname;
function setUpUpdate() {
    if(type === "update") {
        const  idProduct = feedback.productUse.id;
        const  idColor = feedback.colorUse.id;
        choiceSelected(idProduct, idColor)
        const dataUSe = getItemChoice(idProduct, idColor);
        if(feedback.img != null) {
            let parent = document.querySelector(".uploadImg");
            parent.classList.remove("disable");
            const item = parent.querySelector("img");
            item.setAttribute("src", feedback.img );
        }
        createItem(dataUSe);
    }
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

function choiceSelected(idProduct, idColor) {
    const  data = idProduct + "-" + idColor;
    const array = document.querySelectorAll(".add_feedback__product-form > select > option");
    array.forEach((e) => {
        if(e.getAttribute("value") === data) {
            e.selected = true;
        }
    })
}

function renderImgLoad(file) {
    let parent = document.querySelector(".uploadImg");
    parent.classList.remove("disable");
    const item = parent.querySelector("img");
    let reader = new FileReader();
    reader.onload = function(e)  {
        item.src = e.target.result;
    }
    reader.readAsDataURL(file);
}

function choiceImg(event) {
    const file = event.target.files[0];
    renderImgLoad(file);
}


function handleChange(event) {
    const original = event.target.value
    const idOption = original
    const dataUSe = getItemChoice(idOption);
    createItem(dataUSe);
}


function getItemChoice(idOption) {
    let Item = null;
    product_color_size.forEach(e => {
        console.log();
        if(e.idOpt === Number(idOption)) {
            Item = e;
        }
    });
  return Item;
}

let idOpt = null;


function createItem(Data) {
    idOpt = Data.idOpt;
    const  parent = document.querySelector(".add_feedback__product-selected--frame");
    parent.innerHTML = null;
    parent.innerHTML = "<div class=\"item__selected\">\n" +
        "                   <div class=\"item__selected--content\">\n" +
        "                       <strong>"+ Data.productUse.name +"</strong>\n" +
        "                       <span>" + Data.colorUse.name  + "</span>\n" +
        "                   </div>\n" +
        "                   <div class=\"item__selected--img\">\n" +
        "                       <img src=\""+ Data.optionImageUses[0].imageUse.linkImage  +"\" />\n" +
        "                   </div>\n" +
        "               </div>"
}
function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function controlFrom(e, type) {
    e.preventDefault();
    const file = document.querySelector(".file").files[0];
    const data = new FormData();
    data.set("file", file);
    data.set("optionId", idOpt);
            if(!file) {
                openError();
            } else  {
                openLoader();
                upload(data);
            }
}


function openError() {
    const errer = document.querySelector(".err__img");
    errer.classList.remove("disable");
}

function openFromUpdate(value) {
    window.history.replaceState(null,null, url + "?update="+value);
    apiOpenUpdate(value);
}

function openFormCreate() {
    window.history.replaceState(null,null,url + "?action=add");
    apiOpenCreate();
}

function openLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.add(classActive);
}

function hiddenLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.remove(classActive);
}



function hiddenErrors() {
    const errer = document.querySelector(".err__img");
    errer.classList.add("disable")
}

function confirmDelete() {
    openLoader()
    Delete();
    clearAll();
}

function Delete() {
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(Number(e.getAttribute("data-id")));
        }
    });
    apiDelete(obejct);
}