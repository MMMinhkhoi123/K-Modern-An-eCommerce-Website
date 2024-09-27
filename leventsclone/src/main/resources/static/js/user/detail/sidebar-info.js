const handleViewInfo = () => {
    const background = document.querySelector(".info__background");
    const sidebar = document.querySelector(".info__main");
    background.classList.add("active_background");
    sidebar.classList.add("active__manage");
}

const  closeInfo = (e) => {
    const background = document.querySelector(".info__background");
    background.classList.remove("active_background");
    const sidebar = document.querySelector(".info__main");
    sidebar.classList.remove("active__manage");
}
