const classDisable = "disable";
const classActive = "key-active"
const classEventNone = "even_non"

$(window).ready(() => {
    if(type === "update") {
        setUpData();
    }
})


const  url = window.location.origin + window.location.pathname;


function openFromCreate() {
    apiOpenFrom();
    var  url = window.location.href  + "?action=add";
    console.log(url);
    window.history.replaceState(null,null,url);
}

function openFromAddMix(idOutfit) {
    var  url = window.location.href  + "&mix=" + idOutfit;
    window.history.replaceState(null,null,url);
    apiOpenAddMixDetail(idOutfit);
}
function openFromUpdate(id) {
    window.history.replaceState(null,null,url + "?update="+id);
    apiOpenFromUpdate(id);
}
function openFromDetail(id) {
    window.history.replaceState(null,null,url + "?detail="+id);
    apiOpenDetail(id);
}


function addMix(idOutfit) {
    const  array = document.querySelectorAll(".row");
   const  listId = [];
    array.forEach((e) => {
        var input = e.querySelector("input");
        if(input.checked) {
         listId.push(Number(input.getAttribute("data-id")));
        }
    });
    const  data = new FormData();
    data.set("array", listId);
    data.set("idOutfit", idOutfit);
    apiAddMix(data);
    openFromDetail(idOutfit);
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
        if(createEl != null) {
            createEl.classList.add(classOff);
        }
        show_sum.classList.add(classOff);
        show_selected.classList.remove(classOff);
    }else  {
        deleteEl.classList.add(classOff);
        if(createEl != null) {
            createEl.classList.remove(classOff);
        }
        show_sum.classList.remove(classOff);
        show_selected.classList.add(classOff);
    }
}


function ChangeProductMain(event) {
    const  item  = event.target;
    const  dataOriginal = item.value;
    const  idOption = Number(dataOriginal);
    //
    // const productChoice =  product_color_size.filter((e) =>
    //     e.idOpt === idOption);
    //
    // if(getArrayProduct().length === 0) {
    //     createItem(true, item.value, productChoice[0]);
    // }else  {
    //     createItem(false,
    //         item.value, productChoice[0]);
    // }
    //
    // const  option = getOptionByValueChild(idOption);
    // option.forEach(e => {
    //     e.disabled = true;
    // })
    apiGetSize(idOption);
}


function getArrayProduct() {
    const dataResult = [];
    const  array = document.querySelectorAll(".product-selected__list > .item__select");
    array.forEach((e) => {
        if(!e.getAttribute("class").includes(classDisable)) {
            dataResult.push(e);
        }
    })
    return dataResult;
}

function getOptionByValueChild(idOption) {
    let ItemResult = [];
    const array = document.querySelectorAll(".form__product-main > select > option");
    array.forEach((e) => {
        const key = e.getAttribute("value")
        if( Number(idOption) ===  Number(key)) {
            ItemResult.push(e);
        }
    });
    return ItemResult;
}
function hiddenItem(event, key) {
    event.preventDefault();
    event.stopPropagation();
    let  item = null;
    getArrayProduct().forEach((e) => {
        if(e.getAttribute("class").includes("item" + key)) {
            item = e;
        }
    })
    item.classList.add(classDisable);
    if(item.getAttribute("class").includes(classActive)) {
        if(getArrayProduct().length > 0) {
            const  Item = getArrayProduct()[0];
            Item.classList.add(classActive);
        }
    }
    const  original = key.split('-');
    const  idProduct = Number(original[0]);
    const  idColor = Number(original[1]);
    const option = getOptionByValueChild(idProduct, idColor);
    option.forEach((e) => {
        e.disabled = false;
    })
}




function createItem(main, id, object) {
    const parent = document.querySelector(".product-selected__list");
    const  div = document.createElement("label");
    div.classList.add("item__select");
    div.classList.add("item" + id);
    div.setAttribute('data-key', id);

    var img = object.optionImageUses[0].imageUse.linkImage;
    var color = object.colorUse.name;
    var name = object.productUse.name;

    if(main) {
        div.classList.add("key-active");
    }
    div.innerHTML = "  <img src=\""+  img +"\">\n" +
        "                        <div>\n" +
        "                            <strong>"+ color + "</strong>\n" +
        "                        </div>\n" +
        "                        <input disabled onchange=choiceKeyAlong(event,'"+ id+"')  type=\"checkbox\" class='disable'   />\n" +
        "                        <span onclick=\"hiddenItem(event,'"+ id+"')\">\n" +
        "                            <i class=\"fa-solid fa-xmark\"></i>\n" +
        "                        </span>"
    parent.append(div);
}

function choiceKey(event) {
    const Item = event.target;
    const select = document.querySelector(".form__product-main > select");
    if(Item.checked) {
        select.disabled = true;
        getArrayProduct().forEach((e) => {
            const index = e.querySelector("input");
            index.classList.remove(classDisable);
            index.disabled = false;
        });

    } else  {
        select.disabled = false;
        getArrayProduct().forEach((e) => {
            const index = e.querySelector("input");
            index.classList.add(classDisable);
            index.disabled = true;
        });
    }
}

function getProductPrimary() {
   let  Item = null;
    getArrayProduct().forEach((e) => {
       if(e.getAttribute("class").includes(classActive)) {
           Item = e;
       }
   });
    return Item;
}

function getProductMix() {
    let  Item = [];
    getArrayProduct().forEach((e) => {
        if(!e.getAttribute("class").includes(classActive)) {
            Item.push(e);
        }
    });
    return Item;
}


function setData(id, height, weight, idProduct, idColor, idSize) {
    return {
        id: id,
        height: height,
        weight: weight,
        productUse: {
            id: idProduct,
        },
        colorUse: {
            id: idColor,
        },
        sizeUse: {
            id: idSize,
        },
        imagesUses: setImg(),
        outfitUses: setDataOriginal()
    };
}

function setImg() {
   let  dataGet = [];
  const  array = document.querySelectorAll(".drop__selected");
  array.forEach((e) => {
      if(!e.getAttribute('class').includes(classDisable)) {
          const DataImg = e.querySelector("img").getAttribute("data-img");
          const  st = e.querySelector("img").getAttribute("data-state");
          const  dataResult = {
              name: DataImg,
              save: st,
          }
          dataGet.push(dataResult);
      }
  });
  return dataGet;
}


function getImages() {
    let  dataGet = [];
    const  array = document.querySelectorAll(".drop__selected");
    array.forEach((e) => {
        if(!e.getAttribute('class').includes(classDisable)) {
            const DataImg = e.querySelector("img").getAttribute("data-img");
            dataGet.push(DataImg);
        }
    });
    return dataGet;
}


function setDataOriginal() {
    let  dataGet = [];
    getProductMix().forEach((e) => {
        const key = e.getAttribute('data-key').split('-');
        const idProduct = Number(key[0]);
        const idColor = Number(key[1]);
        const idSize = Number(key[2]);
        const dataResult = {
            productUse: {
                id: idProduct,
            },
            colorUse: {
                id: idColor,
            },
            sizeUse: {
                id: idSize,
            }
        };
        dataGet.push(dataResult);
    });
    return dataGet;
}



function controlFor(event, type) {
    event.preventDefault();
    const height = document.querySelector(".form__info--height > input").value;
    const weight = document.querySelector(".form__info--weight > input").value;
    // const primary = getProductPrimary();
    let idOptionSize = document.querySelector(".form__size > select").value;
    // if(primary != null) {
    //     const key = primary.getAttribute('data-key').split('-');
    //      idProduct = Number(key[0]);
    //      idColor = Number(key[1]);
    //      idSize = Number(key[2]);
    // }
    if(type === 'add') {
        openLoader();
        // const Data = setData(null, height.value, weight.value, idProduct, idColor, idSize);
        const  Data = new FormData();
        Data.set("height", height);
        Data.set("weight", weight);
        Data.set("idOptionSize", idOptionSize);
        Data.set("images", getImages());
        apiCreate(Data);
    }else  {
        // const  id = document.querySelector(".add-outfit__form").getAttribute("data-id");
        openLoader();
        // const Data = setData( id, height.value, weight.value, idProduct, idColor, idSize);
        // apiUpdate(Data);

        const  Data = new FormData();
        Data.set("height", height);
        Data.set("weight", weight);
        Data.set("images", getImages());
        const idOutfit = document.querySelector(".add-outfit__form").getAttribute("data-id");
        Data.set("idOutfit", idOutfit);
        apiUpdate(Data);
    }
}



function setUpPrimaryKey(idProduct, idColor, idSize, type) {
    const productChoice =  product_color_size.filter((e) =>
        e.productUse.id === idProduct && e.colorUse.id === idColor
        && e.sizeUse.id === idSize);
    const value =  idProduct + "-" +  idColor + "-" + idSize;
    console.log(productChoice);
    createItem(type, value
        ,productChoice[0].imagesUses[0].name,
        productChoice[0].colorUse.name,
        productChoice[0].sizeUse.name);
    const  option = getOptionByValueChild(idProduct, idColor);
    option.forEach(e => {
        e.disabled = true;
    });
}

function setUpExtraProduct() {
    outfit.outfitUses.forEach((e) => {
        setUpPrimaryKey(e.productUse.id, e.colorUse.id, e.sizeUse.id, false);
    })
}

function setUpImg() {
    outfit.imagesUses.forEach((e, index) => {
        openContentUpload('open');
        hiddenContentUpload('close');
        indexGet = index;
        img.push(e.linkImage);
        // let url = "/admin/products-api/img-upload-outfit/" + e.linkImage;
        let  url = e.linkImage;
        if(e.save === true) {
            url = e.linkImage;
        }
        createImgExtra(index, e.linkImage, url, type, e.save);
    })
}


function setUpData() {
    // if(state == null) {
    //     // if(outfit.productUse.id != null) {
    //     //     setUpPrimaryKey(outfit.productUse.id, outfit.colorUse.id, outfit.sizeUse.id, true);
    //     //     setUpExtraProduct();
    //     // }
    //     setUpImg();
    // }else  {
        if(type === 'update') {
            // if(outfit.productUse.id != null) {
            //     setUpPrimaryKey(outfit.productUse.id, outfit.colorUse.id, outfit.sizeUse.id, true);
            //     setUpExtraProduct();
            // }
            setUpImg();
        }
    // }
}




function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
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


function getArrayProductSubtractValue(value) {
    const dataResult = [];
    const  array = document.querySelectorAll(".product-selected__list > .item__select");
    array.forEach((e) => {
         if(!e.getAttribute("class").includes(classDisable) && !e.getAttribute("class").includes(value)) {
            dataResult.push(e);
        }
    })
    return dataResult;
}


function choiceKeyAlong(event, value) {
    const Item = event.target;
    const submit = document.querySelector(".option__selected");
    if(Item.checked) {
        submit.classList.remove(classDisable);
        getArrayProductSubtractValue(value).forEach((e) => {
            e.classList.add(classEventNone);
        })
    } else  {
        submit.classList.add(classDisable);
        getArrayProductSubtractValue(value).forEach((e) => {
            e.classList.remove(classEventNone);
        })
    }
}


function confirmChangeKey() {
    const submit = document.querySelector(".option__selected");
    getArrayProduct().forEach((e) => {
        const  input = e.querySelector("input");
        if(input.checked) {
            e.classList.add(classActive);
            input.checked = false;
        } else  {
            if(e.getAttribute("class").includes(classActive)) {
                e.classList.remove(classActive);
            }
        }
        e.classList.remove(classEventNone);
    });
    submit.classList.add(classDisable);
}


function confirmDelete() {
    openLoader();
    Delete(this.type);
    clearAll();
}
function Delete(type) {
    const  array = document.querySelectorAll(".table__check > input");
    let obejct = [];
    array.forEach((e) => {
        if(e.checked === true) {
            obejct.push(e.getAttribute("data-id"));
        }
    })
    if(type === "detail") {
        const  idOutfit = document.querySelector(".create").getAttribute('data-id');
        apiDeleteMix(obejct, idOutfit);
        openFromDetail(idOutfit);
    }else  {
        apiDelete(obejct)
    }
}