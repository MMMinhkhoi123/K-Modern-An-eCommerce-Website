$(window).ready(() => {
    // setUpSwiper3();
    // const itemCollection  = document.querySelector(".below__item");
    // widthItemSwiper3 = itemCollection.offsetWidth;
    SetUp3Init();
    setUpSwiper3();
})
function SetUp3() {
    thumbScroll  =  document.querySelector(".below__move");
    parentThumbScroll = document.querySelector(".styling__below--move");
    list = document.querySelector(".styling__below--swiper");
    itemList  = document.querySelectorAll(".below__item-outfit");
    itemOne = document.querySelector(".below__item-outfit");
    list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 4))";
    if(parentThumbScroll != null) {
        itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
    }
    manageMouseUpSwiper = true;
    widthThumbSwiperFact = itineraryThumb / (itemList.length - 4);

    function myFunctionPhone(phone) {
        if (phone.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 1.5))";
            if(parentThumbScroll != null) {
                itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            }
            manageMouseUpSwiper = true;
            widthThumbSwiperFact = itineraryThumb / (itemList.length - 1.5);
        }
    }
    function myFunctionIPad(min, max) {
        if (min.matches && max.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 2.5))";
            if(parentThumbScroll != null) {
                itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            }
            manageMouseUpSwiper = true;
            widthThumbSwiperFact = itineraryThumb / (itemList.length - 2.5);
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIPad(iPadMin, iPadMax);
}

function SetUp3Init() {
    const list = document.querySelector(".styling__below--swiper");
    const itemList  = document.querySelectorAll(".below__item");
    list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 4))";

    function myFunctionPhone(x) {
        if (x.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 1.5))";
        }
    }
    function myFunctionIPad(min, max) {
        if (min.matches && max.matches) {
            // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 2.5))";
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIPad(iPadMin, iPadMax);
    console.log("hello 2")
}


// let  manageMouseOutSwiper3 = true;
// let  manageMouseUpSwiper3 = true;f
function  setUpSwiper3() {
    const arrayItem = document.querySelectorAll(".styling__below--swiper > .below__item-outfit");
    const parent = document.querySelector(".styling__below--swiper");
    parent.style.gridTemplateColumns = 'repeat('+ arrayItem.length +', calc(100vw / 4))'
    function myFunctionPhone(x) {
        if (x.matches) { // If media query matches
            parent.style.gridTemplateColumns = 'repeat('+ arrayItem.length +', calc(100vw / 1.5))'
        }
    }

    function myFunctionIPad(min, max) {
        if (min.matches && max.matches) { // If media query matches
            console.log("hello")
            parent.style.gridTemplateColumns = 'repeat('+ arrayItem.length +', calc(100vw / 2.5))'
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIPad(iPadMin, iPadMax);


    //set url
    arrayItem.forEach((item) => {
        const id = ConvertUrl(item.getAttribute("data-id"));
        item.setAttribute("href", "/blogs/outfit/" + id);
        // item.querySelector(".content__name > p").innerText = item.getAttribute("data-name") + "/" +item.getAttribute("data-color");
    })
}
//
//
function ConvertUrl(name) {
    const nameLV1= name.replaceAll(" ","-");
    return nameLV1.replaceAll("/","-");
}


