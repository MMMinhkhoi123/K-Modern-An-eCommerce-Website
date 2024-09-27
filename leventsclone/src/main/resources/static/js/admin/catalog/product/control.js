$(window).ready(() => {
    setUpPrice();
})


const  url = window.location.origin + window.location.pathname;


function setUpPrice() {
    const array = document.querySelectorAll(".product__price--field");
    array.forEach((item) => {
        const price = Math.floor(Number(item.getAttribute("data-price")));
        item.innerText = assetFormat(price.toString());
    });
    const  input = document.querySelector(".content__left--price > input");
    if(input != null) {
        let dataTemp = input.value.replaceAll(".","");
        if(Number(input.value) === 0) {
            dataTemp = "";
        }
        input.value = assetFormat(dataTemp);
    }
}

function openFrom() {
    apiGetFromCreate();
    window.history.replaceState(null,null,url  + "?action=add");
}

function openDetail(id) {
    window.history.replaceState(null,null, url  + "?detail="+id);
    apiGetDetailProduct(id);
}

function openFromUpdate(id) {
    window.history.replaceState(null,null, url  + "?update="+id);
    apiGetFromUpdate(id);
}

function setUpEditor(value) {
    const ed = new tinymce.Editor('area', {
        some_setting: 1,
        resize: false,
    }, tinymce.EditorManager);
    ed.render();
    setTimeout(() => {
        if(value != null) {
            tinyMCE.activeEditor.setContent(value);
        }
    }, 1000);
}

function hiddenError(value) {
    const item = document.querySelector("." + CSS.escape("err__" + value));
    item.style.display = "none";
}

function setupData(idProduct,name, price, content, classic, id) {
    price = price.toString().replaceAll(".","");
    price = Number(price);
    return {
        id: idProduct,
        name: name,
        price: price,
        describe: content,
        classic: classic,
        typeProductUse: {id: id}
    };
}

function setUpPriceDetail() {
    const item = document.querySelector(".detail-product__price--show");
    if(item != null) {
        const price = Math.floor(Number(item.getAttribute("data-price")));
        item.innerText = assetFormat(price.toString()) + "VNĐ";
    }
}
setUpPriceDetail();

function controlForm(e, type) {
    e.preventDefault();
    loaderForm("open");
    const  name = document.querySelector(".content__left--name > input");
    const  price = document.querySelector(".content__left--price > input");
    const  idType = document.querySelector(".content__left--type > select");
    const  classic = document.querySelector(".option__basic--check > input");
    // const  permission = document.querySelector(".option__permit--check > input");
    const  id = document.querySelector(".add-product__from").getAttribute("data-id");
    let myContent = tinyMCE.activeEditor.getContent();
    if(type === "add") {
        const  data =  setupData(null,name.value, price.value, myContent, classic.checked, idType.value);
        apiCreate(data);
    } else  {
        const  data =   setupData(id,name.value, price.value, myContent, classic.checked, idType.value);
        apiUpdate(data);
    }
}


function  inputPrice(event) {
    const item = event.target;
    let dataValue = item.value.toString().replaceAll(".", "");
    if(checkInp(dataValue)) {
        item.value = item.value.toString().substring(0, item.value.toString().length - 1);
    }else  {
        const data= item.value.toString().replaceAll(".", "");
        item.value = assetFormat(data);
    }
}

function checkInp(data)
{
    const regex = new RegExp(/[^0-9]/, 'g');
    return data.match(regex);
}

function confirmDelete() {
    loaderForm("open");
    Delete(this.type);
    clearAll();
}
function Delete( type) {
    const  array = document.querySelectorAll(".table__check > input");
    let object = [];
    array.forEach((e) => {
        if(e.checked === true) {
            object.push(e.getAttribute("data-id"));
        }
    })
    apiDeleteProduct(object, type)
}