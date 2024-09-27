function  showCollection() {
    const  menuDropdown = document.querySelector(".collection__dropdown");
    const  up = document.querySelector(".collection__title--icon > .fa-chevron-up");
    const  down = document.querySelector(".collection__title--icon > .fa-chevron-down");
    if(menuDropdown.getAttribute("class").includes("active-filter-collection")) {
        menuDropdown.classList.remove("active-filter-collection");
        down.classList.add("active--collection-icon");
        up.classList.remove("active--collection-icon");
    } else  {
        menuDropdown.classList.add("active-filter-collection")
        up.classList.add("active--collection-icon");
        down.classList.remove("active--collection-icon");
    }
}
function  showColor() {
    const  menuColor = document.querySelector(".color__dropdown");
    const  up = document.querySelector(".color__title--icon > .fa-chevron-up");
    const  down = document.querySelector(".color__title--icon > .fa-chevron-down");
    if(menuColor.getAttribute("class").includes("active-filter-collection")) {
        menuColor.classList.remove("active-filter-collection");
        down.classList.add("active--filter-icon");
        up.classList.remove("active--filter-icon");
    } else  {
        menuColor.classList.add("active-filter-collection")
        up.classList.add("active--filter-icon");
        down.classList.remove("active--filter-icon");
    }
}

function  showSize() {
    const  menuSize = document.querySelector(".size__dropdown");
    const  up = document.querySelector(".size__title--icon > .fa-chevron-up");
    const  down = document.querySelector(".size__title--icon > .fa-chevron-down");
    if(menuSize.getAttribute("class").includes("active-filter-collection")) {
        menuSize.classList.remove("active-filter-collection");
        down.classList.add("active--filter-icon");
        up.classList.remove("active--filter-icon");
    } else  {
        menuSize.classList.add("active-filter-collection")
        up.classList.add("active--filter-icon");
        down.classList.remove("active--filter-icon");
    }
}
function  showPrice() {
    const  menuPrice = document.querySelector(".price__dropdown");
    const  up = document.querySelector(" .price__title--icon > .fa-chevron-up");
    const  down = document.querySelector(".price__title--icon > .fa-chevron-down");
    if(menuPrice.getAttribute("class").includes("active-filter-collection")) {
        menuPrice.classList.remove("active-filter-collection");
        down.classList.add("active--filter-icon");
        up.classList.remove("active--filter-icon");
    } else  {
        menuPrice.classList.add("active-filter-collection")
        up.classList.add("active--filter-icon");
        down.classList.remove("active--filter-icon");
    }
}
