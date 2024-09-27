const handleViewSize = () => {
    const background = document.querySelector(".view-size__background");
    const sidebar = document.querySelector(".view-size__main");
    background.classList.add("active_background");
    sidebar.classList.add("active__manage");
}

const  closeSize = (e) => {
    const background = document.querySelector(".view-size__background");
    background.classList.remove("active_background");
    const sidebar = document.querySelector(".view-size__main");
    sidebar.classList.remove("active__manage");
}
