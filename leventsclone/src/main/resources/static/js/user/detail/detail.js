





const handleSize = (e,id) => {
    handleClearSize(e.target);
    e.target.classList.add("active-size");
}

function  handleClearSize(value) {
    document.querySelectorAll(".size__item").forEach((item) => {
        if(value !== item) {
            if(item.getAttribute("class").includes("active-size")) {
                item.classList.remove("active-size");
            }
        }
    })
}


const  closeError = () => {
    unlock ();
    const  error = document.querySelector(".error");
    error.classList.remove("active_error");
}


function openLoadAddCart(type) {
    const classDisLoad = "disable_load";
    const load = document.querySelector(".specific__add-cart > .loader__fame");
    if(type === 'open') {
        load.classList.remove(classDisLoad);
    }else  {

        load.classList.add(classDisLoad);
    }
}

const  handleAddProduct = () => {
    const source = document.querySelector(".specific__add-cart");
    const sourceAmount = document.querySelector(".amount__input");
    let idSize = null;
    let  check = false;
    document.querySelectorAll(".size__item").forEach((e) => {
        if(e.getAttribute("class").includes("active-size")) {
            check = true;
            idSize = e.getAttribute("data-size");
        }
    });
    if(check === false) {
        lockA();
        const  error = document.querySelector(".error");
        error.classList.add("active_error");

        const data = document.querySelector(".error__content");
        data.innerHTML = null;
        data.innerHTML = "  <span>\n" +
            "              Bạn chưa hoàn thành bước chọn size sản phẩm <i class=\"fa-solid fa-circle-exclamation\"></i>\n" +
            "          </span>\n" +
            "          <p>\n" +
            "              Bạn cần hoàn thành chọn size sản phẩm để tiếp tục thêm vào giỏ hoặc mua ngay.\n" +
            "          </p>";
    } else  {
        openLoadAddCart('open');
        const  idOpt = source.getAttribute("data-idOption");
        const  quantity = sourceAmount.value;
        AddProductCart(idOpt, idSize, quantity)
    }
}
let  manageDelete = true;

function handleQuantity(e,type, idOption, idSize) {

    if(manageDelete === false) {
        e.preventDefault();
    }

    const quantity = document.querySelector('#' + CSS.escape(idOption +'quantity'+ idSize));

    if(type === 'increase') {
        if (quantity.value < 20) {
            AddProductCart(idOption, idSize,Number(quantity.value)  + 1 );
        }
    }else  {
        if (quantity.value > 1) {
            // handle server
            AddProductCart(idOption, idSize,quantity.value - 1 );
        }
    }
}

function handleRemove(e, idOptionSize) {
    if(manageDelete === false) {
        e.preventDefault();
    }
    handleLock('open');
    axios.post("/fragments-product/oder-delete?OptionSize="+ idOptionSize,{}, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleProcessLock(process - 20);
        }
    }).then((e) => {
        $(".oderUse").html(e.data);
        setTotal();
        setUpPrice();
        setUpLinkItem();
        handleLock('close')
    })
}


function overSetPre() {
    manageDelete = true;
}

function overSet() {
    manageDelete = false;
}

function  deletePackaging(event) {

    event.preventDefault();

    handleLock('open');

    axios.post("/fragments-product/oder-delete-packaging",{}, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleProcessLock(process - 20);
        }
    }).then((e) => {
        $(".oderUse").html(e.data);
        setTotal();
        setUpPrice();
        setUpLinkItem();
        handleLock('close')
    })
}


function handleLock(type) {
    const classDisableLock = "disable_lock";
    const  lock = document.querySelector(".manage__lock");
    if(type === 'open') {
        lock.classList.remove(classDisableLock);
    }else  {
        const  move = document.querySelector(".manage__lock--progress > span");
        move.style.right = 0 + "%";
        setTimeout(() => {
            move.style.right = 100 + "%";
            lock.classList.add(classDisableLock);
        }, 500);
    }
}

function handleProcessLock(data) {
    const  move = document.querySelector(".manage__lock--progress > span");
    const dataGet = 100 - data;
    move.style.right = dataGet + "%";
}


function  AddProductCart(idOption, idSize, quantity) {
    handleLock('open');
    axios.post("/fragments-product/oder-add?size="+ idSize +"&option=" + idOption + "&quantity=" + Number(quantity),{}, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: x => {
            const  process = Math.floor((x.loaded / x.total) * 100);
            handleProcessLock(process - 20);
        }
    }).then((e) => {
        $(".oderUse").html(e.data);
        setTotal();
        setUpPrice();
        setUpLinkItem();
        setTimeout(() => {
            openLoadAddCart('close');
            handleOpenCart();
        }, 500)
        handleLock('close')
    })
}


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



function choiceHeart(thisElement) {
    const classActive = "active";
    const idOpt = thisElement.getAttribute('data-idOpt');
    if(thisElement.getAttribute('class').includes(classActive)) {
        thisElement.classList.remove(classActive);
        removeWishList(idOpt)
    }else  {
        thisElement.classList.add(classActive);
        addWishList(idOpt)
    }
}


function addWishList(idOption) {
    const data = new FormData();
    data.set("idOpt",idOption);
    axios.post("/AddWishList",data, {} ).then((e) => {
        getWishList();
        setUpPriceOrder();
    })
}

function getWishList() {
    axios.get("/fragments/getWishlist", {} ).then((e) => {
        $(".display__authenticated-content").html(e.data);
        setUpPriceOrder();
    })
}

function removeWishList(idOption) {
    const data = new FormData();
    data.set("idOpt",idOption);
    axios.delete("/deleteWishList", {data: data } ).then((e) => {
        getWishList();
        setUpPriceOrder();
    })
}













































