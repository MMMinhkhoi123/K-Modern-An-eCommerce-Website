const classActiveSearch = "active_search";
const classDisableScroll = "disable__scroll";
const openSearch = () => {
    const  container = document.querySelector(".container");
    const search = document.querySelector(".header__search");
    search.classList.add(classActiveSearch);
    container.classList.add(classDisableScroll);
}

const closeSearch = () => {
    const  container = document.querySelector(".container");
    const search = document.querySelector(".header__search");
    search.classList.remove(classActiveSearch);
    container.classList.remove(classDisableScroll);
}


function LoadOption() {
    const classDisable = document.querySelector(".content__input--menu-suggest-parent") ;
    classDisable.innerHTML = "  <div class=\"content__input--menu-suggest template_load\" >\n" +
        "                        <div class=\"triangle-up\"></div>\n" +
        "                        <div class=\"loading-data\">\n" +
        "                            <span class=\"loader-search\"></span>\n" +
        "                            <div class=\"loader-skeleton\">\n" +
        "                                <div class=\"loader-skeleton-left skeleton\">\n" +
        "\n" +
        "                                </div>\n" +
        "                                <div class=\"loader-skeleton-right\">\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"loader-skeleton\">\n" +
        "                                <div class=\"loader-skeleton-left skeleton\">\n" +
        "\n" +
        "                                </div>\n" +
        "                                <div class=\"loader-skeleton-right\">\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"loader-skeleton\">\n" +
        "                                <div class=\"loader-skeleton-left skeleton\">\n" +
        "\n" +
        "                                </div>\n" +
        "                                <div class=\"loader-skeleton-right\">\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                    <span class=\"skeleton\"></span>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>";
}

const handleInputData = (e) => {
    const value = e.target.value;
    LoadOption();
    if(value === "" || value === null) {
        $.get('/init/search/'+ "null", function (e) {
            console.log(e);
            $(".content__input--menu-suggest-parent").html(e);
            setUpPriceS();
            setUpUrl();
            urlForSubmitInResult(value)
        })
    }else  {
        $.get('/init/search/'+value, function (e) {
            $(".content__input--menu-suggest-parent").html(e);
            setUpPriceS();
            setUpUrl();
            urlForSubmitInResult(value)
        })
    }
    urlForSubmit(value);
}

function setUpPriceS() {
    const Data = document.querySelectorAll(".product-suggest--price");
    Data.forEach((e) => {
        e.innerText = assetFormat(Math.floor(Number(e.getAttribute("data-price"))).toString()) + " VNĐ";
    })
}

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function setUpUrl() {
 const data = document.querySelectorAll(".product-suggest__item");
 data.forEach((e) => {
     const dataUrl = e.getAttribute("data-url");
     e.setAttribute("href", "/products?option=" + dataUrl);
 })
}

function urlForSubmit(value) {
    const item = document.querySelector(".content__input-submit");
    item.setAttribute("href", "/collections/search?q=" + value);
}
function urlForSubmitInResult(value) {
    const ItemView = document.querySelector(".content__product--view > a");
    if(ItemView != null) {
        ItemView.setAttribute("href", "/collections/search?q=" + value);
    }
}