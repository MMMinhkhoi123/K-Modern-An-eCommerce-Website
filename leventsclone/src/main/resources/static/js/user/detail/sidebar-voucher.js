const handleViewVoucher = () => {
    const background = document.querySelector(".voucher__background");
    const sidebar = document.querySelector(".voucher__main");
    background.classList.add("active_background");
    sidebar.classList.add("active__manage");
}

const  closeVoucher = (e) => {
    const background = document.querySelector(".voucher__background");
    background.classList.remove("active_background");
    const sidebar = document.querySelector(".voucher__main");
    sidebar.classList.remove("active__manage");
}
