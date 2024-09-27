const choiceArrival = document.querySelector(".our-collection__above--option > .new");
const choiceSeller = document.querySelector(".our-collection__above--option > .seller");
const viewArrival = document.querySelector(".swiper > .product-arrive");
const viewSeller = document.querySelector(".swiper > .product-seller");

choiceArrival.addEventListener("click", () => {
    resetSwiper();
   if(!choiceArrival.getAttribute("class").includes("active")) {
       choiceArrival.classList.add("active");
       viewArrival.classList.add("active--choice-type-product");
       if(choiceSeller.getAttribute("class").includes("active")) {
           choiceSeller.classList.remove("active");
           viewSeller.classList.remove("active--choice-type-product");
       }
       const link = document.querySelector(".our-collection__above--view > a");
       link.setAttribute("href", "/collections/new-arrival");
   }
});


choiceSeller.addEventListener("click", () => {
    resetSwiper();
    if(!choiceSeller.getAttribute("class").includes("active")) {
        choiceSeller.classList.add("active");
        viewSeller.classList.add("active--choice-type-product");
        if(choiceArrival.getAttribute("class").includes("active")) {
            choiceArrival.classList.remove("active");
            viewArrival.classList.remove("active--choice-type-product");
        }
        const link = document.querySelector(".our-collection__above--view > a");
        link.setAttribute("href", "/collections/best-seller");
    }
})

function SetUp(classThumb, classParentThumb, classList, classItem) {
    thumbScroll  =  document.querySelector(classThumb);
    parentThumbScroll = document.querySelector(classParentThumb);
    list = document.querySelector(classList);
    itemList  = document.querySelectorAll(classItem);
    itemOne = document.querySelector(classItem);
    list.style.gridTemplateColumns = "repeat("+itemList.length +", calc(100vw/ 4))";
    // const withActive = parentThumbScroll.offsetWidth / ((itemList.length - 3));
    // thumbScroll.style.width = withActive + 'px';
    itineraryThumb  = parentThumbScroll.offsetWidth - thumbScroll.offsetWidth;
    manageMouseUpSwiper = true
    widthThumbSwiperFact = itineraryThumb / (itemList.length  - 4) ;

    function myFunctionPhone(x) {
        if (x.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +", calc(100vw/ 2))";
            widthThumbSwiperFact = itineraryThumb / (itemList.length  - 2) ;
        }
    }
    function myFunctionIpad(min,max) {
        if (min.matches && max.matches) { // If media query matches
            list.style.gridTemplateColumns = "repeat("+itemList.length +", calc(100vw/ 3))";
            widthThumbSwiperFact = itineraryThumb / (itemList.length  - 3) ;
        }
    }

// Create a MediaQueryList object
    var phone = window.matchMedia("(max-width: 500px)")
    var iPadMin = window.matchMedia("(min-width: 500px)")
    var iPadMax = window.matchMedia("(max-width: 1024px)")
// Call listener function at run time
    myFunctionPhone(phone);
    myFunctionIpad(iPadMin,iPadMax)

}





window.addEventListener('resize', function(event){
    var screenWidth = window.innerWidth;
    var element = document.querySelectorAll('.swiper__wrapper');

    if (screenWidth < 500) {
        element.forEach((e) => {
            e.classList.add("small-screen")
        })
    } else {
        element.forEach((e) => {
            e.classList.remove("small-screen")
        })
    }
});