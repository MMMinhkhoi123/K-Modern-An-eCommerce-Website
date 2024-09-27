$(window).ready(function() {
    // const  cards = document.querySelectorAll(".card__parent > .card");
    // cards.forEach((e, index) => {
    //     const  color = e.getAttribute("data-color");
    //     const  url = e.getAttribute("href");
    //     var initCapText = color.charAt(0).toUpperCase() + color.slice(1);
    //     const newUrl = url.replaceAll(" ", "-")+ "-" + initCapText;
    //     e.setAttribute("href",newUrl);
    // });
    formatMoney();
    handleRunInit();
});



let active_item = 0;

let default_item;
let next_item;
let listParent= document.querySelectorAll( "[id='1box']");
const runDataBase = (value, id, type, color) => {
    let  listParent = document.querySelectorAll( "#"+ CSS.escape(type + id + color));
   let keyDefault = "default";
    let keyActive = "active";
    let keyNext = "next";

    default_item = active_item - 1;
    next_item = active_item  + 1;
    if(default_item < 0) {
        default_item = (listParent.length - 1);
    }
    if(next_item > (listParent.length - 1) ) {
        next_item = next_item - (listParent.length - 1) -1;
    }
    const active =  document.querySelector("."  + CSS.escape( id +"key" + type + active_item + color) );
    active.classList.add(keyActive);
    active.classList.remove(keyNext)
    active.classList.remove(keyDefault)
    const defaults =  document.querySelector("." +  CSS.escape(id +"key" + type  + default_item + color));
    defaults.classList.add(keyDefault);
    defaults.classList.remove(keyActive);
    defaults.classList.remove(keyNext);
    const next =  document.querySelector("." +  CSS.escape(id +"key" + type  + next_item + color));
    next.classList.add(keyNext);
    next.classList.remove(keyDefault);
    next.classList.remove(keyActive);

    if(value == "next") {
        next.classList.add("move")
        setTimeout( () => {
            next.classList.remove("move")
        }, 200)
    } else {
        defaults.classList.add("move")
        setTimeout( () => {
            defaults.classList.remove("move")
        }, 200)
    }
}


const runDataBase2 = (value, idOption , type) => {
     let listParent =  document.querySelector("#" + CSS.escape("key" + type + idOption))
        .querySelectorAll(".card__img")
    let keyDefault = "default";
    let keyActive = "active";
    let keyNext = "next";

    default_item = active_item - 1;
    next_item = active_item  + 1;
    if(default_item < 0) {
        default_item = listParent.length - 1;
    }
    if(next_item >= listParent.length ) {
        next_item = next_item - listParent.length;
    }


    const active =  document.querySelector("."  + CSS.escape( type + active_item + idOption));
    active.classList.add(keyActive);
    active.classList.remove(keyNext)
    active.classList.remove(keyDefault)
    const defaults =  document.querySelector("." +  CSS.escape(type  + default_item + idOption));
    defaults.classList.add(keyDefault);
    defaults.classList.remove(keyActive);
    defaults.classList.remove(keyNext);
    const next =  document.querySelector("." +  CSS.escape( type  + next_item + idOption));
    next.classList.add(keyNext);
    next.classList.remove(keyDefault);
    next.classList.remove(keyActive);

    if(value == "next") {
        next.classList.add("move")
        setTimeout( () => {
            next.classList.remove("move")
        }, 200)
    } else {
        defaults.classList.add("move")
        setTimeout( () => {
            defaults.classList.remove("move")
        }, 200)
    }
}

function next(event,value, type, color) {
    event.preventDefault();
    listParent = document.querySelectorAll( "#"+ CSS.escape(type + value + color));
    active_item = getIndex (type, value, color);
    checkPreOrNext(active_item, listParent, value, type, color);
    if(active_item < listParent.length - 1) {
        console.log("run next")
        active_item = active_item + 1;
        setIndex(type, value, color, active_item)
        runDataBase("next", value, type, color);
    }
}

function next2(event, type, idOption) {
    event.preventDefault();
    listParent =  document.querySelector("#" + CSS.escape("key" + type + idOption))
        .querySelectorAll(".card__img")
    active_item = getIndex2(type, idOption);
    if(active_item < listParent.length - 1) {
        active_item = active_item + 1;
        setIndex2(type, idOption, active_item)
        runDataBase2("next", idOption, type);
    }
    checkPreOrNext2(active_item, listParent, type, idOption);
}

function pre2(event, type, idOption) {
    event.preventDefault();
    listParent =  document.querySelector("#" + CSS.escape("key" + type + idOption))
        .querySelectorAll(".card__img")
    active_item = getIndex2(type, idOption);
    if(active_item > 0) {
        active_item = active_item - 1;
        setIndex2(type, idOption, active_item)
        runDataBase2("pre", idOption, type);
    }
    checkPreOrNext2(active_item, listParent, type, idOption);
}

function pre(event,value, type, color) {
    event.preventDefault();
    listParent =  document.querySelectorAll( "#"+ CSS.escape(type + value + color));
    active_item = getIndex (type, value, color);
    checkPreOrNext(active_item, listParent, value, type, color);
    if(active_item > 0) {
        active_item = active_item - 1;
        setIndex(type, value, color, active_item)
    }
    runDataBase("pre", value, type, color);
}



function  checkPreOrNext(value, parent, idProduct, type, nameColor ) {
    const  classKey = "block_ability";
    const  pre = document.querySelector("." + CSS.escape('back' + idProduct + type + nameColor));
    const  next = document.querySelector("." + CSS.escape('next' + idProduct + type + nameColor));
    if(value === 0) {
        pre.classList.add(classKey);
    } else  if(value === parent.length - 1)  {
        next.classList.add(classKey);
    } else  {
        if(pre.getAttribute("class").includes(classKey)) {
            pre.classList.remove(classKey);
        }
        if(next.getAttribute("class").includes(classKey)) {
            next.classList.remove(classKey);
        }
    }
}

function  checkPreOrNext2(index, parent, type, idOption ) {
    const  classKey = "block_ability";
    const  pre = document.querySelector("." + CSS.escape('back'  + type + idOption));
    const  next = document.querySelector("." + CSS.escape('next' + type + idOption));

    if(index === 0) {
        pre.classList.add(classKey);
        next.classList.remove(classKey);
    } else  if(index === parent.length - 1)  {
        next.classList.add(classKey);
        pre.classList.remove(classKey);
    } else  {
        if(pre.getAttribute("class").includes(classKey)) {
            pre.classList.remove(classKey);
        }
        if(next.getAttribute("class").includes(classKey)) {
            next.classList.remove(classKey);
        }
    }
}



function  setIndex(type, idProduct, color, newValue) {
    const  item = document.querySelector("." + CSS.escape('card-key' + type + idProduct + color));
    item.setAttribute("data-swiper", newValue);
}

function  setIndex2(type, idOption, newIndex) {
    const  item = document.querySelector("." + CSS.escape( type + idOption));
    item.setAttribute("data-swiper", newIndex);
}
function  getIndex (type, idProduct, color) {
    const  item = document.querySelector("." + CSS.escape('card-key' + type + idProduct + color));
    return Number(item.getAttribute("data-swiper"));
}

function  getIndex2 (type, idOption) {
    const  item = document.querySelector("." + CSS.escape(type + idOption));
    return Number(item.getAttribute("data-swiper"));
}


function  handleRunInit() {
    const  classKey = "block_ability";
    const  arrayKey=  document.querySelectorAll(".card__parent");
    arrayKey.forEach((e) => {
        const type = e.querySelector(".card").getAttribute("data-type");
        const idOption = e.querySelector(".card").getAttribute("data-idOpt");
        const  pre = document.querySelector("." + CSS.escape('back'  + type + idOption));
        const  next = document.querySelector("." + CSS.escape('next' + type + idOption));
        const images = e.querySelectorAll(".card__slide--img");
        let  parent = e.querySelectorAll(".card__slide--img")
        checkPreOrNext2(0, parent, type, idOption )
        if(images.length === 1) {
            pre.classList.add(classKey);
            next.classList.add(classKey);
        }
    })
   const  array=  document.querySelectorAll(".card > .card__slide >.card__img")
        array.forEach((item, index) => {
            if(Number(item.getAttribute("data-index")) === 0) {
                item.classList.add("active");
            } else
            if(Number(item.getAttribute("data-index")) === 1) {
                item.classList.add("next");
            } else
            if(Number(item.getAttribute("data-index")) === 2) {
                item.classList.add("default");
            } else  {
                item.classList.add("default");
            }
        })
}



function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
function formatMoney() {
    const data = document.querySelectorAll(".card__content--price");
    data.forEach((e, index) => {
        e.innerText = assetFormat(Math.floor(e.innerText).toString()) + " VND";// 1,234,567,890
    })
}


// function GoItemMobile(Element) {
//     console.log(window.location);
//     function myFunction(x) {
//         if (x.matches) { // If media query matches
//         }
//     }
//     var x = window.matchMedia("(max-width: 500px)")
//     myFunction(x);
// }