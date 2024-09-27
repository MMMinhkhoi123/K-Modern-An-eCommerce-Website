



document.querySelector(".header__bottom--menu > i").addEventListener("click", (e) => {
    const classDisableScroll = "disable__scroll";
    const classShowBackground = "active_background";
    const classShowSideBar = "active_sidebar";
    const  container = document.querySelector(".container");
   const background =  document.querySelector(".sidebar__parent > .background")
   const side = document.querySelector(".sidebar");
        side.classList.add(classShowSideBar);
        background.classList.add(classShowBackground);
        container.classList.add(classDisableScroll);
});

document.querySelector(".sidebar__parent > .background").addEventListener("click" , () =>
{
    const classDisableScroll = "disable__scroll";
    const classShowBackground = "active_background";
    const classShowSideBar = "active_sidebar";
    const  container = document.querySelector(".container");
    const side = document.querySelector(".sidebar");
    const background = document.querySelector(".sidebar__parent > .background");
    background.classList.remove(classShowBackground);
    side.classList.remove(classShowSideBar);
    container.classList.remove(classDisableScroll)
});



function closeChoiceItem(value) {
    const extraMenu = document.querySelector("#" + CSS.escape(value));
    extraMenu.classList.remove("active_sidebar-extra");
    const  menu = document.querySelector(".sidebar");
    menu.classList.add("active_sidebar");
}
function choiceItem(thisElement) {
    function myFunction(x) {
        if (x.matches) { // If media query matches
            const  menu = document.querySelector(".sidebar");
            menu.classList.remove("active_sidebar");
            const id = thisElement.innerText;
            const extraMenu = document.querySelector("#" + CSS.escape(id));
            extraMenu.classList.add("active_sidebar-extra");
        }
    }
    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);
}

function closeSideBar() {
    const classDisableScroll = "disable__scroll";
    const classShowBackground = "active_background";
    const classShowSideBar = "active_sidebar";
    const  container = document.querySelector(".container");
    const side = document.querySelector(".sidebar");
    const background = document.querySelector(".sidebar__parent > .background");
    background.classList.remove(classShowBackground);
    side.classList.remove(classShowSideBar);
    container.classList.remove(classDisableScroll)
}


$(window).ready(function() {
    $.get("/init/directory", { }, function(data) {
        $("#directory").html(data);
    });
});





