const handleViewChange = () => {
    const background = document.querySelector(".change__background");
    const sidebar = document.querySelector(".change__main");
    background.classList.add("active_background");
    sidebar.classList.add("active__manage");
}

const  closeChange = (e) => {
    const background = document.querySelector(".change__background");
    background.classList.remove("active_background");
    const sidebar = document.querySelector(".change__main");
    sidebar.classList.remove("active__manage");
}
