let idImg = 0;
let imgArray = [];
let imgExsit = [];


function openLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.add(classActive);
}


function handleUploadSubmit() {
    const array = document.querySelectorAll(".imgUpload");
    const data = [];
    array.forEach((item) => {
        if(!item.getAttribute("class").includes("disable")) {
            data.push(item.getAttribute("data-img"));
        }
    });
    if(data.length > 0) {
        openLoader();
        apiUploadSubmit(data);
    }
}

function hiddenLoader() {
    const  classActive = "active-load";
    const  loader = document.querySelector(".loader-background");
    loader.classList.remove(classActive);
}
function  apiUploadSubmit(data) {
    axios.post("/api/upload/save",data, {}).then((e) => {
        $(".upload__collection--content-frame").html(e.data);
        hiddenLoader();
        // reset();
    }) ;
}

function apiDeleteCommon(name) {
    axios.delete("/admin/products-api/delete-common/"+ name, {
    });
}


function handleOverride() {
    imgExsit.forEach((e) => {
        const  Item = getItemName(e.name);
        const index = Item.getAttribute("data-index");
        Item.setAttribute("data-img", e.name);
        renderImgLoad(index, e);
        const  dataFrom = new FormData();
        dataFrom.set("file", e);
        apiUpload(dataFrom, index);
    });
    openAlert("close");
}

function reset() {
    const  classDisable = "disable";
    const array = document.querySelectorAll(".imgUpload");
    array.forEach((e) => {
        if(!e.getAttribute("class").includes("disable")) {
            e.classList.add(classDisable);
            imgArray = [];
            imgExsit = [];
            idImg = 0;
        }
    });
    handleOpenEmpty();
}

function hiddeAlert() {
    const alert = document.querySelector(".alert__success");
    alert.classList.add("disable");
}
function getItemName(name) {
    const array = document.querySelectorAll(".imgUpload");
    let Item = null;
    array.forEach((e) => {
        if(e.getAttribute("data-img") === name) {
            Item = e;
        }
    });
    return Item;
}



function upLoadImg(event) {
    const listImg = event.target.files;

    const listNew = [];
    for (let  x = 0 ; x < listImg.length ; x++) {
        listNew.push(listImg[x]);
    }
    const  listExit = getImgExist(imgArray, listNew);

    if(listExit.length > 0) {
        imgExsit = listExit;
        openAlert("open");
        console.log(imgExsit);
    }else  {
        for (let  x = 0 ; x < listImg.length ; x++) {
            const from = new FormData();
            from.set("file", listImg[x]);
            apiUpload(from, idImg);
            createImg(idImg, listImg[x].name);
            imgArray.push(listImg[x]);
            renderImgLoad(idImg, listImg[x]);
            idImg = idImg + 1;
        }
        handleEmpty("close");
    }
}

function setValueInput() {
    const  Item = document.querySelector(".collection__option--upload > input");
    Item.value = null;
}
function openAlert(type) {
    const  classDisable = "disable";
    const alert = document.querySelector(".collection__content--background");
    if(type === "open") {
        alert.classList.remove(classDisable);
    }else  {
        alert.classList.add(classDisable);
    }
}

function getImgExist(ListOld, ListNew) {
    const ListExist = [];
    ListNew.forEach((e) => {
        console.log(checkNameExist(e.name, ListOld));
        if(checkNameExist(e.name, ListOld)) {
            ListExist.push(e);
        }
    });
    return ListExist;
}


function checkNameExist(name , listItem) {
    let check = false;
    listItem.forEach((e) => {
        if(e.name === name) {
            check = true;
        }
    })
    return check;
}



function hiddenImg(event,index) {
    event.preventDefault();
    const  item = document.querySelector("." + CSS.escape("img" + index));
    item.classList.add("disable");
    handleOpenEmpty();
    apiDeleteCommon(item.getAttribute("data-img"));
}


function handleOpenEmpty() {
    const classDisable = "disable";
    const array = document.querySelectorAll(".imgUpload");
    let  check = false;
    array.forEach((e) => {
        if(!e.getAttribute("class").includes(classDisable)) {
            check = true;
        }
    })
    if(!check) {
        handleEmpty("open");
    }
}

function createImg(index, name) {
   const  parent = document.querySelector(".collection__option--list");
  const div = document.createElement("div");
  div.classList.add("imgUpload");
  div.classList.add("img" + index);
  div.classList.add("upload__loading");
  div.setAttribute("data-img", name);
  div.setAttribute("data-index", index);
  div.innerHTML = "  <img src=\"https://th.bing.com/th/id/R.2dd6c3d3897a323db95d52e64db217f1?rik=cfCpsO55vcAWmg&pid=ImgRaw&r=0\">\n" +
      "                    <i onclick='hiddenImg(event,"+ index +")' class=\"fa-solid fa-xmark\"></i>\n" +
      "                    <div class=\"process\">\n" +
      "                        <div class=\"process__bar\"></div>\n" +
      "                    </div>";
    parent.append(div);
}

function apiUpload(data, index) {
    axios.post("/admin/products-api/upload-common",data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            animateProcess(index, process);
            if(process === 100) {
                removeLoading(index);
                setValueInput();
            }
        }
    }).then((e) => {
    })
}


function handleEmpty(type) {
    const classDisable = "disable";
    const empty = document.querySelector(".option__upload--empty");
    if(type === "open") {
        empty.classList.remove(classDisable);
    }else  {
        empty.classList.add(classDisable);
    }
}


function renderImgLoad(index, file) {
    const  item = document.querySelector("." + CSS.escape("img" + index));
    let image = item.querySelector("img");
    let reader  = new FileReader();
    reader.onload = function(e)  {
        image.src = e.target.result;
    }
    reader.readAsDataURL(file);
}




function removeLoading(index) {
    const classLoading = "upload__loading";
    const  item = document.querySelector("." + CSS.escape("img" + index));
    item.classList.remove(classLoading);
    const process = item.querySelector(".process");
    process.classList.add("disable");
}
function animateProcess(index, value) {
    const  item = document.querySelector("." + CSS.escape("img" + index));
    const  process = item.querySelector(".process__bar");
    process.style.right = 100 - value + "%";
}

function handleChangeSelect(event) {
    const  item = event.target;
    if(item.checked === true) {
        handleITem("open");
        handleButtonRemove("open");
        handleShowCount("close");
        handleShowCountSelected("open");
    }else  {
        handleITem("close");
        handleButtonRemove("close");
        handleShowCount("add");
        handleShowCountSelected("close");
    }
}
function handleShowCountSelected(type) {
    const classDisable = "disable";
    const data = document.querySelector(".show__init-select");
    if(type === "open") {
        data.classList.remove(classDisable);
    } else  {
        data.classList.add(classDisable);
    }
}


function handleChangeAlone(event) {
    const  item = event.target;
    if (item.checked === true) {
        countHandle("increase")
    } else  {
        countHandle("decrease")
    }
}


function countHandle(type) {
    const  item = document.querySelector(".show__init-select > span");
    let  countCurrent = Number(item.innerText);
    console.log(countCurrent);
    if(type === "increase") {
        countCurrent = (countCurrent + 1);
    }else  {
        countCurrent = (countCurrent - 1);
    }
    item.innerText = countCurrent;
}


function handleShowCount(type) {
    const classDisable = "disable";
    const data = document.querySelector(".show__init-original");
    if(type === "open") {
        data.classList.remove(classDisable);
    } else  {
        data.classList.add(classDisable);
    }
}

function handleButtonRemove(type) {
    const  disable = "disable";
    const button = document.querySelector(".selected__remove");
    if(type === "open") {
        button.classList.remove(disable);
    }else  {
        button.classList.add(disable);
    }
}

function handleITem(type) {
    const classActive = "activeSelect";
    const disable = "disable";
    const array = document.querySelectorAll(".collection__content--item");
    if(type === "open") {
        array.forEach((e) => {
            e.classList.add("activeSelect");
            const  inputEl = e.querySelector("input");
            inputEl.disabled = false;
            const  name = e.getAttribute("data-img")
            const cope = document.querySelector("#" + CSS.escape("show" + name));
            cope.classList.add(disable);
        })
    } else  {
        array.forEach((e) => {
            e.classList.remove("activeSelect");
            const  inputEl = e.querySelector("input");
            inputEl.disabled = true;
            const  name = e.getAttribute("data-img")
            const cope = document.querySelector("#" + CSS.escape("show" + name));
            cope.classList.remove(disable);
        })
    }
}


function deleteSubmit() {
    const  listImg = [];
    const array = document.querySelectorAll(".collection__content--item");
    array.forEach((e) => {
        const  check = e.querySelector("input");
        if(check.checked === true) {
            listImg.push(e.getAttribute("data-img"));
        }
    });
    if(array.length > 0) {
        apiDeleteSubmit(listImg);
    }
}


function OpenUpload(type) {
    const classDisable = "disableUpload";
    const data = document.querySelector(".upload__collection");
    if(type === "open") {
        data.classList.add(classDisable);
    }else  {
        data.classList.remove(classDisable);
    }
}

function apiDeleteSubmit(data) {
    openLoader();
    axios.delete("/api/upload/delete", {
        data: data,
    }).then((e) => {
        $(".upload__collection--content-frame").html(e.data);
        hiddenLoader();
    });
}

function getItemByName(value) {
    let Item = null;
    const  x = document.querySelectorAll(".collection__content--item");
    x.forEach((e) => {
        if(e.getAttribute("data-img") === value) {
            Item = e;
        }
    });
    return Item;
}

function copy(e, name) {
    e.preventDefault();
    const disable = "disable";
    const Item = getItemByName(name);
    const copy1 = Item.querySelector(".copy > i:nth-child(1)");
    const copy = Item.querySelector(".copy > i:nth-child(2)");
    copy.classList.remove(disable);
    copy1.classList.add(disable);
    navigator.clipboard.writeText(name);
}

function mouseOut(e, name) {
    e.preventDefault();
    const disable = "disable";
    const Item = getItemByName(name);
    const copy1 = Item.querySelector(".copy > i:nth-child(1)");
    const copy = Item.querySelector(".copy > i:nth-child(2)");
    copy.classList.add(disable);
    copy1.classList.remove(disable);
}


