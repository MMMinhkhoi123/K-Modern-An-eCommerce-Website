const  url = window.location.origin + window.location.pathname;

function openForm() {
    window.history.replaceState(null,null,url + "?action=add");
    apiOpenForm()
}

function openFormSize(id) {
    window.history.replaceState(null,null,url + "&actionSize=" + id);
    apiOpenFormSize(id)
}
function openFormUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiOpenFormUpdate(id);
}
function OpenDetail(idOption) {
    const  data = idOption;
    window.history.replaceState(null,null,url + "?detail="+ data);
    apiOpenDetail(data);
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


let indexGet = 0;
let img = [];
let fileExist = null;

function destroyImage() {
    const  from = document.querySelector(".add-products__form--imagesExist");
    from.classList.add("disable");
}

function overrideImage() {
    const dataItem = document.querySelectorAll(".drop__selected > img");
    dataItem.forEach((e, index) => {
        if(e.getAttribute("data-img") === fileExist.name) {
            const indexUse = e.getAttribute("data-index");
            renderImg(e, fileExist);
            const form = new FormData();
            form.append("file", fileExist);
            apiUpload(form, indexUse);
            destroyImage();
        }
    });
}


function submitAddSize(e) {
    e.preventDefault();
    const  idSize = document.querySelector(".addSize__size > select").value;
    const  quantity = document.querySelector(".addSize__quantity > input").value;
    const  idOpt = e.target.getAttribute("data-option")
    const data = new FormData()
    data.set("quantity", quantity);
    data.set("idSize", idSize);
    data.set("idOpt", idOpt);
    apiAddSize(data);

}

function changeImage(e) {
    const  listFile  = e.target.files;
    if(listFile.length > 0) {
        hiddenContentUpload('close');
        openContentUpload('open');
        let formData = new FormData();
        for(let x = 0 ; x < listFile.length ; x++) {
            formData = new FormData();
            const name = listFile[x].name;
            if( checkInclude(name) === true) {
                formData.append("file", listFile[x]);
                indexGet ++;
                if(createImg(indexGet, listFile[x]) != null) {
                    apiUpload(formData, indexGet);
                }
                img.push(name);
            } else  {
                fileExist = listFile[x];
                OpenNameExist();
            }
        }
        e.target.value = null;
    }
}


function OpenNameExist() {
    const  from = document.querySelector(".add-products__form--imagesExist");
    from.classList.remove("disable");
}


function  checkInclude(name) {
    if(img.includes(name)) {
        return false;
    }else  {
        return  true;
    }
}


function deleteImage(e, value, indexs, type) {
    e.stopPropagation();
    e.preventDefault();
    const  card = document.querySelector("#" + CSS.escape("imageCard" + indexs));
    card.classList.add("disable");
    img = img.filter((e, index) => e.trim() !== value.trim());
    if(img.length === 0) {
        hiddenContentUpload('open');
        openContentUpload('close');
    }
    if(type !== "update") {
        apiDeleteImg(value);
    }
}



function handleAnimate(index, value) {
    const bar = document.querySelector("#" + CSS.escape("bar" + index));
    const barParent = document.querySelector("#" + CSS.escape("barparrent" + index));
    const img = document.querySelector("." + CSS.escape("image" + index));
    let data = 100 - value;
    bar.style.right = data + "%";
    if(value === 100) {
        img.style.opacity = 1;
        barParent.style.opacity = 0;
    }
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
function openContentUpload(type) {
    const  classHidden = "disable";
    const  item = document.querySelector(".form__drop--selected");
    if(type === 'open') {
        item.classList.remove(classHidden);
    }else  {
        item.classList.add(classHidden);
    }
}

function hiddenContentUpload(type) {
   const  classHidden = "disable";
    const  item = document.querySelector(".form__drop--content");
   if(type === "close") {
       item.classList.add(classHidden);
   }else  {
       item.classList.remove(classHidden);
   }
}

function  createImgExtra(index, name, success, type) {
    let url = '/admin/products-api/img-upload/'+name;
    if(success != null) {
        url = name;
    }
    const img = document.querySelector(".form__drop--selected");
    const div = document.createElement("div");
    div.classList.add("drop__selected");
    div.classList.add("show");
    div.id = "imageCard" + index;
    div.innerHTML = "<i onclick=\"deleteImage(event,' " + name + "', " + index + ", '"+ type +"')\" class=\"fa-solid fa-xmark\"></i>\n" +
        "                        <img data-index="+ index +" data-img=" + name + " class=image" + index + " src="+url+"  alt=\"ss\">\n" +
        "                        <div id=barparrent" + index + " class=\"drop__selected--process\">\n" +
        "                            <div class=\"process__bar\"  id=bar" + index + "></div>\n" +
        "                        </div>"
    img.append(div);
}




function createImg(index, file) {
    const img = document.querySelector(".form__drop--selected");
    const div = document.createElement("div");
    div.classList.add("drop__selected");
    div.id = "imageCard" + index;
    div.innerHTML = "<i onclick=\"deleteImage(event,' "+file.name+"', "+index+")\" class=\"fa-solid fa-xmark\"></i>\n" +
        "                        <img data-index="+ index +" data-img="+file.name+" class=image"+index+" src=\"/image/star.jpg\" alt=\"ss\">\n" +
        "                        <div id=barparrent"+index+" class=\"drop__selected--process\">\n" +
        "                            <div class=\"process__bar\"  id=bar"+index+"></div>\n" +
        "                        </div>"
    img.append(div);
    const  item = document.querySelector("." + CSS.escape("image" + index));
    renderImg(item, file);
    return  index;
}



function renderImg(item, file) {
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


function controlFromProducts(e, type) {
    e.preventDefault();
    const idProduct = document.querySelector(".form--product > select").value;
    const idColor = document.querySelector(".form--color > select").value;
    const price = document.querySelector(".form-price > input").value;
    // const data = setData(idProduct,idColor);
    openLoader();
    if(type === "add") {
        const formData = new FormData();
        if(img != null) {
           formData.set("images",img);
        }
        formData.set('idProduct', idProduct);
        formData.set("idColor", idColor)
        formData.set('price', price);

        apiCreateProducts(formData);
        img = [];
    } else  {
        const formData = new FormData();
        if(img != null) {
            formData.set("images",img);
        }
        formData.set('idProduct', idProduct);
        formData.set("idColor", idColor)
        formData.set('price', price);
       apiUpdateProducts(formData);
        img = [];
    }
}

function getColorActive(e) {
    apiGetColors(e.target.value);
}

function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function setData(idProduct, idColor) {
    return {
        productUse: {
            id: idProduct,
        },
        colorUse: {
            id: idColor
        },
        optionImageUses: getImages(),
    }
}


function getSizes() {
   let listSizeUses = [];
   const  array = document.querySelectorAll(".size__list > div:not(.size__list--empty) ");
   array.forEach((e) => {
       if(!e.getAttribute("class").includes("disable")) {
           const itemChild = e.querySelector(".size_child");
           const sizeUse = {
               id: Number(itemChild.getAttribute("data-id")),
               name: itemChild.getAttribute("data-name"),
               quantity: e.getAttribute("data-quantity")
           }
           listSizeUses.push(sizeUse);
       }
   })
    return listSizeUses;
}

let idSizeSet = null;
let nameSizeSet = null;
let quantitySet = null;

function handleChangSize(event) {
    const select = event.target.value.split("-");
    const  idSize = select[0];
    const  nameSize = select[1];

    idSizeSet = idSize;
    nameSizeSet = nameSize;
    quantitySet = 1;

    resetSelected(event.target.value);
    selectedEmpty("close");
    const  quantity =  InputQuantity('open');
    quantity.setAttribute("data-id", idSizeSet);
    quantity.value = 1;
    quantity.focus();
    addElement(idSize, nameSize, quantity.value);
}


function handleChangeQuantity(event) {
    if(event.target.value <= 0) {
        event.target.value = 1;
    }
   const  id = event.target.getAttribute("data-id");
   const  array = document.querySelectorAll("." + CSS.escape("size" + id));
   array.forEach((e) => {
       if(!e.getAttribute("class").includes("disable")) {
           const quantity = e.querySelector("#" + CSS.escape("quantity" + id));
           quantity.innerText = event.target.value;
           e.setAttribute("data-quantity",event.target.value );
       }
   })
   event.target.disabled = true;
   event.target.value = 0;
    resetDataSelected();
}


function resetDataSelected() {
    const select = document.querySelectorAll(".size > select > option");
    select.forEach((e) => {
        if(e.selected === true) {
            e.selected = false;
        }
    })
}


function InputQuantity(type) {
    const quantity = document.querySelector(".form--quality > input");
    if(type === 'close') {
        quantity.disabled = true;
    }else  {
        quantity.disabled = false;
    }
    return quantity;
}

function selectedEmpty(type) {
    const classDisabled = "disable";
    const item = document.querySelector(".size__list--empty");
    if(type === 'close') {
        item.classList.add(classDisabled);
    }else  {
        item.classList.remove(classDisabled);
    }
}

function unSelected(data) {
    const select_parent = document.querySelector(".size > select ");
    const select = document.querySelectorAll(".size > select > option");
    let  check = true;
    select.forEach((e) => {
        if(e.getAttribute("value") === data) {
            e.disabled = false;
        }
        if(e.disabled === true) {
            check = false;
        }
    });
    if(check) {
        select.forEach((e) => {
            if(e.selected === true) {
              e.selected = false;
            }
        });
        selectedEmpty("open");
    }

}
function resetSelected(data) {
    const select = document.querySelectorAll(".size > select > option");
    select.forEach((e) => {
        if(e.getAttribute("value") === data) {
            e.disabled = true;
        }
    });
}

function addElement(id, name, quantity) {
    const  divParent = document.querySelector(".size__list");
   const  div = document.createElement("div");
   const  originalData = id + "-" + name;
   div.classList.add("size" + id);
   div.setAttribute("data-quantity",quantity);
    div.innerHTML = "<div class='size_child' data-id='"+ id +"' data-name='"+ name +"' onclick=\"updateQuantity("+ id +", '" + name +"')\"  > " +
"                        <i onclick=\"deleteSizeSelected(event,"+ id +", '"+ name+"')\" class=\"fa-solid fa-xmark\"></i>\n" +
"                        <span>\n" +
                    "        <span class=\"size__name\"> "+ name +" </span>\n" +
                    "        (<span class=\"size__quantity\" id='quantity"+ id+"'> " + quantity +"</span>\n )" +
                    "    </span>" +
       "                    </div>";
    divParent.append(div);
}


function updateQuantity(id, name) {
    const  data = id + "-" + name;
    const select = document.querySelectorAll(".size > select > option");
    select.forEach((e) => {
        if(e.getAttribute("value") === data ) {
            e.selected = true;
        }
    });
    let quantity = 0;
    const  array = document.querySelectorAll("." + CSS.escape("size" + id));
    array.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            quantity = e.getAttribute("data-quantity");
        }
    })
    const input = document.querySelector(".form--quality > input");
    input.setAttribute("data-id", id);
    input.disabled = false;
    input.value = quantity;
}


function deleteSizeSelected(event,id, name) {
    event.stopPropagation();
    const  array = document.querySelectorAll("." + CSS.escape("size" + id));
    array.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            e.classList.add("disable");
            unSelected(id + "-" + name);
        }
    })
}


function getImages() {
    let imgUse = [];
    img.forEach((e) => {
        const  data = {
            name: e,
        }
        imgUse.push(data);
    })
    return imgUse;
}


function confirmDelete() {
    outWaring();
    openLoader();
    deleteProducts(this.type);
    clearAll();
}

function  deleteProducts(type) {
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(e.getAttribute("data-id"));
        }
    });


    if(type === "detail") {
        const  id = document.querySelector(".products__sale").getAttribute("data-option");
        apiDeleteSize(obejct, id)
    }else  {
apiDelete(obejct);
    }
}

function preCount(count) {
    const show_sum = document.querySelector(".show__sum > span");
    show_sum.innerText = (Number(show_sum.innerText) - count);
}



function handleSelectedUpdate(){
    const array = document.querySelectorAll(".size__list > div:not(.size__list--empty)");
    array.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            const  child = e.querySelector(".size_child");
            const  value = child.getAttribute("data-id") + "-" +  child.getAttribute("data-name");
            disableSelected(value);
        }
    })
}

$(window).ready(() => {
    handleSelectedUpdate();
})

function disableSelected(value) {
    const x = document.querySelectorAll(".size > select > option");
    x.forEach((e) => {
        if(e.value === value) {
            e.disabled = true;
        }
    })
}