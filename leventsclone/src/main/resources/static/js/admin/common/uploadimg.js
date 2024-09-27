let img = [];
let indexGet = 0;

let fileExist = null;





function changeImage(e) {
    const  listFile  = e.target.files;
    if(listFile.length > 0) {
        hiddenContentUpload('close');
        openContentUpload('open');
        let formData = new FormData();
        for(let x = 0 ; x < listFile.length ; x++) {
            formData = new FormData();
            const name = listFile[x].name;
            if(checkInclude(name) === true) {
                formData.append("file", listFile[x]);
                indexGet ++;
                if(createImg(indexGet, listFile[x], false) != null) {
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


function OpenNameExist() {
    const  from = document.querySelector(".add-products__form--imagesExist");
    from.classList.remove("disable");
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

function openContentUpload(type) {
    const  classHidden = "disable";
    const  item = document.querySelector(".form__drop--selected");
    if(type === 'open') {
        item.classList.remove(classHidden);
    }else  {
        item.classList.add(classHidden);
    }
}

function  checkInclude(name) {
    if(img.includes(name)) {
        return false;
    }else  {
        return  true;
    }
}


function createImg(index, file, state) {
    const img = document.querySelector(".form__drop--selected");
    const div = document.createElement("div");
    div.classList.add("drop__selected");
    div.id = "imageCard" + index;
    div.innerHTML = "<i onclick=\"deleteImage(event,' "+file.name+"', "+index+")\" class=\"fa-solid fa-xmark\"></i>\n" +
        "                        <img data-state="+state+" data-index="+ index +" data-img='"+file.name+"'" +
        "                   class=image"+index+" src=\"/image/star.jpg\" alt=\"ss\">\n" +
        "                        <div id=barparrent"+index+" class=\"drop__selected--process\">\n" +
        "                            <div class=\"process__bar\"  id=bar"+index+"></div>\n" +
        "                        </div>"
    img.append(div);
    const  item = document.querySelector("." + CSS.escape("image" + index));
    renderImg(item, file);
    return  index;
}

function  createImgExtra(index, name, url, type, state) {
    const img = document.querySelector(".form__drop--selected");
    const div = document.createElement("div");
    div.classList.add("drop__selected");
    div.classList.add("show");
    div.id = "imageCard" + index;

    div.innerHTML = "<i onclick=\"deleteImage(event,'"+ name + "', " + index + ", '"+ type +"')\" class=\"fa-solid fa-xmark\"></i>\n" +
        "                        <img data-state="+state+" data-index="+ index +" data-img='"+ name +"' class=image" + index + " src='"+url+"'  alt=\"ss\">\n" +
        "                        <div id=barparrent" + index + " class=\"drop__selected--process\">\n" +
        "                            <div class=\"process__bar\"  id=bar" + index + "></div>\n" +
        "                        </div>"
    img.append(div);
}



function deleteImage(e, value, indexs, type) {
    e.stopPropagation();
    e.preventDefault();
    console.log(indexs);
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
