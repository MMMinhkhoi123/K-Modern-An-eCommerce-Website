$(window).ready((e) => {
    SetUp3Init();
})
function SetUp3Init() {
    const list = document.querySelector(".feedback__below--swiper");
    const itemList  = document.querySelectorAll(".below__item-feedback");
    list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 3.5))";
    function myFunctionPhone(phone) {
        if (phone.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 1.5))";
        }
    }
    function myFunctionIpad(min, max) {
        if (min.matches && max.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+ itemList.length +",calc(100vw / 2.5))";
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIpad(iPadMin, iPadMax);
}

function SetUp4() {
    thumbScroll  =  document.querySelector(".move__feedback");
    parentThumbScroll = document.querySelector(".feedback__below--move");
    list = document.querySelector(".feedback__below--swiper");
    itemList  = document.querySelectorAll(".below__item-feedback");
    itemOne = document.querySelector(".below__item-feedback");

    list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 3.5))";
    if(parentThumbScroll != null) {
        itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
    }

    const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 2.5));
    thumbScroll.style.width = withActive + 'px';
    manageMouseUpSwiper = true;
    widthThumbSwiperFact = itineraryThumb / (itemList.length - 3.5);

    function myFunctionPhone(phone) {
        if (phone.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 1.5))";
            if(parentThumbScroll != null) {
                itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            }
            const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 0.5));
            thumbScroll.style.width = withActive + 'px';
            manageMouseUpSwiper = true;
            widthThumbSwiperFact = itineraryThumb / (itemList.length - 1.5);
        }
    }
    function myFunctionIpad(min, max) {
        if (min.matches && max.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 2.5))";
            if(parentThumbScroll != null) {
                itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            }
            const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 1.5));
            thumbScroll.style.width = withActive + 'px';
            manageMouseUpSwiper = true;
            widthThumbSwiperFact = itineraryThumb / (itemList.length - 2.5);
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIpad(iPadMin, iPadMax);
}
