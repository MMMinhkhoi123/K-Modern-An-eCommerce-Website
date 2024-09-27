const handleViewTransfor = () => {
    const background = document.querySelector(".transfer__background");
    const sidebar = document.querySelector(".transfer__main");
    background.classList.add("active_background");
    sidebar.classList.add("active__manage");
}

const  closeTransfor = (e) => {
    const background = document.querySelector(".transfer__background");
    background.classList.remove("active_background");
    const sidebar = document.querySelector(".transfer__main");
    sidebar.classList.remove("active__manage");
}
