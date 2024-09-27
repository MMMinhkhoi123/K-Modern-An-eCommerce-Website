
const cart = document.querySelector(".manage__option--cart");
const viewCart = document.querySelector(".manage__display--cart");
const account = document.querySelector(".manage__option--account");
const viewAccount = document.querySelector(".manage__display--account");
const login = document.querySelector(".account__login");
const register = document.querySelector(".account__register");
const  viewLogin = document.querySelector(".account__fromLogin");
const  viewRegister = document.querySelector(".account__fromRegister");
const  backgroundManage = document.querySelector(".manage__parent > .background");




cart.addEventListener("click", () => {
    if (!cart.getAttribute("class").includes("active__select-option")) {
        cart.classList.add("active__select-option");
        viewCart.classList.add("active");
        if (account.getAttribute("class").includes("active__select-option")) {
            account.classList.remove("active__select-option");
            viewAccount.classList.remove("active");
        }
    }
})



account.addEventListener("click", () => {
    handleOpenAccount();
})

function handleOpenAccount() {
    if (!account.getAttribute("class").includes("active__select-option")) {
        account.classList.add("active__select-option");
        viewAccount.classList.add("active");
        if (cart.getAttribute("class").includes("active__select-option")) {
            cart.classList.remove("active__select-option");
            viewCart.classList.remove("active")
        }
    }
}

if(login != null) {
    login.addEventListener("click", ()=> {
        if(!login.getAttribute("class").includes("active__select-option")) {
            login.classList.add("active__select-option");
            viewLogin.classList.add("active-from")
            if (register.getAttribute("class").includes("active__select-option")) {
                register.classList.remove("active__select-option");
                viewRegister.classList.remove("active-from")
            }
        }
    })
}

if(register != null) {
    register.addEventListener("click", ()=> {
        if(!register.getAttribute("class").includes("active__select-option")) {
            register.classList.add("active__select-option");
            viewRegister.classList.add("active-from")
            if (login.getAttribute("class").includes("active__select-option")) {
                login.classList.remove("active__select-option");
                viewLogin.classList.remove("active-from")
            }
        }
    })
}


const manage = document.querySelector(".manage");

function OpenManagerCart() {
    const  container = document.querySelector(".container");
    container.classList.add(classDisableScroll);
    handleOpenCart();
}

function OpenManagerAccount() {
    const  container = document.querySelector(".container");
    container.classList.add(classDisableScroll);
    handleAccountIcon();
}

function handleOpenCart() {
    if(!backgroundManage.getAttribute("class").includes("backgroundManage")) {
        backgroundManage.classList.add("active_background");
        manage.classList.add("active__manage");
        cart.classList.add("active__select-option");
        viewCart.classList.add("active");
        if (account.getAttribute("class").includes("active__select-option")) {
            account.classList.remove("active__select-option");
            viewAccount.classList.remove("active")
        }
    }
}


function  handleAccountIcon() {
    backgroundManage.classList.add("active_background");
    manage.classList.add("active__manage");
    account.classList.add("active__select-option");
    viewAccount.classList.add("active");
    if (cart.getAttribute("class").includes("active__select-option")) {
        cart.classList.remove("active__select-option");
        viewCart.classList.remove("active")
    }
}

function CloseBar() {
    const  container = document.querySelector(".container");
    container.classList.remove(classDisableScroll);
    backgroundManage.classList.remove("active_background");
    manage.classList.remove("active__manage");
}

backgroundManage.addEventListener("click", () => {
    const  container = document.querySelector(".container");
    container.classList.remove(classDisableScroll);
    backgroundManage.classList.remove("active_background");
    manage.classList.remove("active__manage");
})


$(window).ready(function() {
    $.get("/fragments-product/oder-get",{}, function(data) {
        $(".oderUse").html(data);
        setTotal();
        setUpPrice();
        setUpLinkItem();
        setResize();
    });

});


function setResize() {
    const  data = document.querySelector(".header");
    const manage = document.querySelector(".manage");
    // function myFunction(x) {
    //     if (x.matches) { // If media query matches
    //         manage.style.top = data.offsetHeight + "px";
    //     }
    // }
    // var x = window.matchMedia("(max-width: 500px)");
    // myFunction(x);
}

function  setTotal() {
    let total = 0;
    let countProduct = 0;
    const  List = document.querySelectorAll(".oder__List--item");
    List.forEach((item) => {
        var quantity= item.getAttribute("data-quantity");
        var price = item.getAttribute("data-price")
        total = total + (Number(quantity) * Number(price));
        countProduct = countProduct +  Number(quantity);
    } )
    const  count = document.querySelector(".cart__count");
    const  pay = document.querySelector(".total");
    count.innerText = countProduct;
    if(pay != null) {
        pay.innerText =  assetFormat(Math.floor(total).toString()) + " VND";
    }
}


function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function setUpLinkItem() {
    const  arrayItem = document.querySelectorAll(".oder__List--item");
     arrayItem.forEach((e) => {
         console.log(e.getAttribute("data-packaging"));
         if(e.getAttribute("data-packaging") === "true") {
             e.setAttribute("href", '/');
         } else  {
             const  option = e.getAttribute("data-option");
             e.setAttribute("href", '/products?option='+ option);
         }
     })
}

function setUpPrice() {
    const arrayItem
        = document.querySelectorAll(".List__content--price > span");
    arrayItem.forEach((e) => {
        console.log();
        e.innerText =  assetFormat(
            Math.floor(Number(e.getAttribute("data-price")))
            .toString()) + " VND"
    })
}


function handleLogin2(thisElement) {
    const loading = thisElement.querySelector(".loader-common");
    const text = thisElement.querySelector("span");
    loading.classList.remove("disable")
    text.classList.add("disable")
    const data = {
        remember_save: document.querySelector(".account__fromLogin--remember > input").checked,
        gmail: document.querySelector(".account__fromLogin--email > input").value,
        password: document.querySelector(".account__fromLogin--password > input").value,
    }
    $.post("/login-user?remember_me="+data.remember_save + "&password=" + data.password + "&gmail="+ data.gmail, {}, {}).then((e) => {
        window.location.reload();
        window.location.replace("/#account");
    }).catch(() => {
        window.location.replace("/account/login?error");
    });
}

function handleLogin(thisElement) {
    const loading = thisElement.querySelector(".loader-common");
    const text = thisElement.querySelector("span");
    loading.classList.remove("disable")
    text.classList.add("disable")
    const data = {
        remember_save: document.querySelector(".login__extra--remember > input").checked,
        gmail: document.querySelector(".login__email > input").value,
        password: document.querySelector(".login__password > input").value,
    }
    axios.post("/login-user?gmail="+data.gmail + "&password=" + data.password + "&remember_me=" + data.remember_save , {}, {}).then((e) => {
        window.location.replace("/#account");
    }).catch((e) => {
        window.location.replace("/account/login?error");
    })
}


$(window).ready(() => {

    // handleAddress();
    const  data = window.location.hash;
    if(data === "#account") {
        handleOpenCart();
        handleOpenAccount();
    }
    if(data === "#cart") {
        handleOpenCart();
    }
})


function loaderEmail(type) {
    const loading = document.querySelector(".loader-email");
    const text = document.querySelector(".forget__submit > button > span");
    const block = document.querySelector(".forget__submit");
    if(type === "open") {
        loading.classList.add("active-load");
        text.style.display = "none";
        block.style.pointerEvents = "none";
    } else  {
        loading.classList.remove("active-load");
        text.style.display = "block";
        block.style.pointerEvents = "visible";
    }
}


function resetAlertForget() {
    const classActive = "active-error";
    const Array = document.querySelectorAll(".forget__title__state > span");
    Array.forEach((e) => {
        if(e.getAttribute('class').includes(classActive)) {
            e.classList.remove(classActive);
        }
    })
}

function resetPassword(e) {
    const classActive = "active-error";
    const email = document.querySelector(".forget__email > input");
    e.preventDefault();
    loaderEmail('open');
    const data = new FormData();
    data.set("email",email.value);
    axios.post("/resetPass-account", data, {}).then((e) => {
        resetAlertForget();
        const success = document.querySelector(".forget__success");
        const hading = document.querySelector(".forget__error-handling");
        const constant = document.querySelector(".forget__error-constant");
        if(e.data === "success") {
            success.classList.add(classActive);
        } else  if(e.data === "fail") {
            hading.classList.add(classActive);
        } else  {
            constant.classList.add(classActive);
        }
        loaderEmail('close');
    })
}


function NewPassWord(e) {
    e.preventDefault();


    const token = document.querySelector(".resetPass").getAttribute('data-token');
    const time = document.querySelector(".resetPass").getAttribute('data-time');
    const verify = document.querySelector(".resetPass").getAttribute('data-verify');

    const classActive = "active-error";
    const pass1 = document.querySelector(".resetPass__password > input").value;
    const pass2 = document.querySelector(".resetPass__password-confirm > input").value;
    let pattern = new RegExp('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$');
    const errorValid = document.querySelector(".error_valid");
    const errorMatch = document.querySelector(".error_confirm");
    if(!pattern.test(pass1)) {
        errorValid.classList.add(classActive);
    }else  {
        if(errorValid.getAttribute("class").includes(classActive)) {
            errorValid.classList.remove(classActive);
        }
        if(pass1 !== pass2) {
            errorMatch.classList.add(classActive);
        }else  {
            if(errorMatch.getAttribute("class").includes(classActive)) {
                errorMatch.classList.remove(classActive);
            }
            // add server
            const data = new FormData();
            data.set("password", pass1);
            data.set("token", token);
            data.set("time", time);
            data.set("verify", verify);
            axios.post('/account/resetPassword', data, {}).then((e) => {
                window.location.replace("/account/login");
            })
        }
    }
}






























































































