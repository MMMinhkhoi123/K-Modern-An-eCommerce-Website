$(window).ready((e) => {
    SetUp2();
    convertUrl();
})

function convertUrl() {
    const  array = document.querySelectorAll(".collection__item");
    array.forEach((item) => {
        const  convertUrl = item.getAttribute("data-url").replaceAll(" ", "-");
        const  convertUrlLevel2 = convertUrl.replaceAll("/","-");
        item.setAttribute("href" , '/collections/' +  convertUrlLevel2);
    })
}

convertUrl();

function SetUp2() {
    thumbScroll  =  document.querySelector(".move__collection--step");
    parentThumbScroll = document.querySelector(".move__collection");
    list = document.querySelector(".collection");
    itemList  = document.querySelectorAll(".collection__item");
    itemOne = document.querySelector(".collection__item");
    list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 3.5))";
    const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 2.5));
    thumbScroll.style.width = withActive + 'px';
    itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
    manageMouseUpSwiper = true;
    widthThumbSwiperFact = itineraryThumb / (itemList.length  - 3.5);


    function myFunctionPhone(x) {
        if (x.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 1.5))";
            const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 0.5));
            thumbScroll.style.width = withActive + 'px';
            itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            widthThumbSwiperFact = itineraryThumb / (itemList.length  - 1.5);
        }
    }
    function myFunctionIPad(min, max) {
        if (min.matches && max.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +",calc(100vw / 2.5))";
            const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 0.5));
            thumbScroll.style.width = withActive + 'px';
            itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
            widthThumbSwiperFact = itineraryThumb / (itemList.length  - 2.5);
        }
    }
    var phone = window.matchMedia("(max-width: 500px)")
    var IPadMin = window.matchMedia("(min-width: 500px)")
    var IPadMax = window.matchMedia("(max-width: 1024px)")
    myFunctionPhone(phone);
    myFunctionIPad(IPadMin, IPadMax);
}