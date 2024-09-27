$(window).ready((e) => {
    const  array = document.querySelectorAll(".below__item");
    array.forEach((e) => {
        const  id = e.getAttribute("data-id");
        const url = "/blogs/outfit/" + id;
        e.setAttribute('href', url);
    })
})