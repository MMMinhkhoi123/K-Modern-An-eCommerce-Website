document.querySelector(".container").addEventListener('scroll', () => {
    const  container = document.querySelector(".container");
    const  back = document.querySelector(".back__scroll");
    if(container.scrollTop > 500) {
        back.classList.add("activeBack");
    } else  {
        back.classList.remove("activeBack");
    }
})

function scrollSmooth() {
    const  container = document.querySelector(".header__top");
    const  back = document.querySelector(".back__scroll");
    container.scrollIntoView({behavior : "smooth"})
    back.classList.remove("activeBack");
}