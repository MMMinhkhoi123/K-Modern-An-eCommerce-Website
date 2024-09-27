function choiceCatalog(key)  {
    const  parentMenu = document.querySelector("." + CSS.escape('parent-menu' + key))
    resetMenu(parentMenu);
    const  clasActiveIcon = "activeIcon";
    const  clasActiveParentMenu = "activeMenu";
    const  icon = document.querySelector("." + CSS.escape('icon'+ key));
    const  Menu = document.querySelector("." + CSS.escape('menu' + key))
    handleClassIcon(icon, clasActiveIcon);
    handleClassMenu(parentMenu,Menu, clasActiveParentMenu);
}

function resetMenu(parentMenu) {
    const itemMenu = document.querySelectorAll(".sidebar__item");
    itemMenu.forEach((e) => {
        if(e.getAttribute("class").includes("activeMenu") && e !== parentMenu) {
            e.classList.remove("activeMenu");
            e.style.paddingBottom = 10 + "px";
            const  icon = e.querySelector(".item__icon  > i");
            icon.classList.remove("activeIcon");
        }
    })
}

function activeMenuAccount() {
    const data = document.querySelector(".account__menu");
    if(data.getAttribute("class").includes("active__menu_account")) {
        data.classList.remove("active__menu_account");
    }else  {
        data.classList.add("active__menu_account");
    }

}

function handleClassIcon(item, classCompare) {
    if(item.getAttribute("class").includes(classCompare)) {
        item.classList.remove(classCompare);
    }else  {
        item.classList.add(classCompare);
    }
}

function handleClassMenu(item,itemGet, classCompare) {
    if(item.getAttribute("class").includes(classCompare)) {
        item.style.paddingBottom = 10 + "px";
        item.classList.remove(classCompare);
    }else  {
        item.style.paddingBottom = itemGet.offsetHeight + 'px';
        item.classList.add(classCompare);
    }
}

function getUrl(url){
  return  window.location.protocol + window.location.host  +  "/admin/" + "catalog/"+ url;
}

function choiceItem(e, value) {
    e.stopPropagation();
    const newUrl = "https://example.com/new-page.html";
    window.location.replace(newUrl);
}


function offMenu() {
    const classItem = "offItem";
    const classMenu = "offMenu";
    const sumPay = document.querySelector('.sumPay');
    const menu = document.querySelector(".sidebar");
    const  array = document.querySelectorAll(".sidebar__item");
    if(menu.offsetWidth < 100) {
        if(sumPay != null) {
            closeSum();
        }

        menu.classList.remove(classMenu);
        array.forEach((e) => {
            e.classList.remove(classItem);
        })
    }else  {
        menu.classList.add(classMenu);
        array.forEach((e) => {
            e.classList.add(classItem);
        })
    }
}