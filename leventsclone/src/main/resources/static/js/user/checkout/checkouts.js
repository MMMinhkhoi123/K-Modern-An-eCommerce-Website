const errInput = "active_err_input";
const errLabel = "active_err";
let error_phone_label = null;
let error_phone_label_extra = null;


function SetUp() {
    const  address = document.querySelector(".address");
    const firstname = document.querySelector(".firstname");
    const lastname = document.querySelector(".lastname");
    if(address != null) {
        address.value = localStorage.getItem("fullAddress");
        firstname.value = localStorage.getItem("firstName");
        lastname.value = localStorage.getItem("lastName");
    }
}
SetUp();

function resetTextMobile() {
    const data = document.querySelectorAll(".detail-small-text > span");
    data.forEach((e ) => {
        e.classList.remove("disable");
    })
}

function resetTextMobile2() {
    const data = document.querySelectorAll(".detail-small__text > span");
    data.forEach((e ) => {
        e.classList.remove("disable");
    })
}

function infoOverViewProductsMobile(type, thisElement) {
    resetTextMobile2();
    thisElement.classList.add("disable");
    const frame = document.querySelector(".getView > .checkout__from--right-ticky > div > .from__apply-detail-small");
    const info = frame.querySelector(".from__item-buy__parent-frame");
    if(type === "open") {
        frame.style.paddingBottom = info.offsetHeight + "px";
    }else  {
        frame.style.paddingBottom = 0 + "px";
    }
}



function infoDetailMobile(type, thisElement) {
    resetTextMobile()
    thisElement.classList.add("disable");
    const frame = document.querySelector(".checkout__from--detail-small");
    const info = document.querySelector(".detail-small-text__info");
    if(type === "open") {
        frame.style.paddingBottom = info.offsetHeight + "px";
    }else  {
        frame.style.paddingBottom = 0 + "px";
    }
}


function handleChange(type) {
    const input = document.querySelector("." + CSS.escape('input__' + type));
    const err = document.querySelector("." + CSS.escape("err__" + type));
    const  classErrorText = "active__err-text";
    const  classErrorInput = "active__err-input";
    if(input.value === "") {
        input.classList.add(classErrorInput);
        err.classList.add(classErrorText);
    }
}

function  handleInput(type) {
   const inputs = document.querySelectorAll("." + CSS.escape('input__' + type));
    const label = document.querySelectorAll("." + CSS.escape("label__" + type));
    const err = document.querySelectorAll("." + CSS.escape("err__" + type));

    inputs.forEach((input, index) => {
        const classActiveLabel = "active__label";
        const  classInput = "active__input";
        const  classErrorText = "active__err-text";
        const  classErrorInput = "active__err-input";
        if(input.value !== "") {
            input.classList.add(classInput);

            label[index].classList.add(classActiveLabel);

            if(err[index] != null) {
                if (err[index].getAttribute('class').includes(classErrorText)) {
                    err[index].classList.remove(classErrorText);
                    input.classList.remove(classErrorInput);
                }
            }

        } else  {
            input.classList.remove(classInput);
            label[index].classList.remove(classActiveLabel);
        }


        if(type === 'voucher') {
            handleButtonVoucher(input.value, type, index);
        }
    })
}


function  handleButtonVoucher(value,type, index) {
    const  classButton = "active_button";
    const  buttons = document.querySelectorAll("." + CSS.escape('button__' + type));
        if(value === "") {
            if(buttons[index].getAttribute("class").includes(classButton)) {
                buttons[index].classList.remove(classButton);
            }
        } else  {
            if(!buttons[index].getAttribute("class").includes(classButton)) {
                buttons[index].classList.add(classButton);
            }
        }

}

function  openProvideInformation(item) {
    item.style.display = 'none';
    const  input = document.querySelector(".from__department--input");
    input.style.display = 'flex';
}


function restOption() {
    const array = document.querySelectorAll(".from__pay--checkbox");
    array.forEach((e) => {
        const input = e.querySelector("input");
        input.checked = false;
    })
}


function handleShowDetailTransport(e, type) {
    e.preventDefault();
    const  keyClassItem = "active-select";
    const main = document.querySelector("." + CSS.escape("option__"+ type));
    const show = document.querySelector("." + CSS.escape("show-option__" + type))
    const  dataPadding = show.offsetHeight;
    restOption();
    if(main.getAttribute("class").includes(keyClassItem)){
        main.style.paddingBottom = 0 + 'px';
        main.classList.remove(keyClassItem);
    } else  {
        main.style.paddingBottom = dataPadding + 'px';
        main.classList.add(keyClassItem);
    }
    checkOpen(e,type);
}


$(window).ready((e) => {
    setUpPriceVoucher();
    setUpMoney();
    setPriceMember();
})

function setPriceMember() {
    const total_old = document.querySelectorAll(".user__member--right > .total__old");
    const total_new = document.querySelectorAll(".user__member--right > .total__new");
    if(total_old.length !== 0) {
        
        total_old.forEach((e) => {
            const total = Number(e.getAttribute('data-value'));
             e.innerText = assetFormat(total.toString()) + " ₫";
        });

        total_new.forEach((e) => {
            const total__new = Number(e.getAttribute('data-value'));
            e.innerText = assetFormat(total__new.toString()) + " ₫";
        })
    }
}


function getPriceVoucher() {
    let price = 0;
    const array = document.querySelectorAll(".voucher__item--right > span");
    price = price + Number(array[0].getAttribute("data-set"));
    return price;
}


function setUpMoney() {
    const detail = document.querySelectorAll(".money__detail");
    detail.forEach((e) => {
        const price = e.getAttribute("data-money");
        e.innerText = assetFormat(Math.floor(price).toString());
    })
    const total = document.querySelectorAll(".total");
    const total_transport = document.querySelectorAll(".price__transport");
    const total_extra = document.querySelectorAll(".total-extra");
    let totalGet = 0;
    total_extra.forEach((e) => {
        e.innerText = assetFormat(Math.floor(e.getAttribute("data-total")).toString()) +  " ₫";
    })

    total.forEach((e) => {
        e.innerText = assetFormat(e.getAttribute('data-price'));
        totalGet = e.innerText;
    })

    total_transport.forEach((e) => {
        e.innerText = assetFormat(e.getAttribute("data-price")) +  ' ₫';
    })



    function myFunction(x) {
        if (x.matches) { // If media query matches
            const  priceSet = document.querySelector(".detail-small-price");
            priceSet.innerText = totalGet + " VNĐ";
        }
    }

    var x = window.matchMedia("(max-width: 500px)")
    myFunction(x);


    const sum = document.querySelector(".sum__payment");

    if(sum != null) {
        sum.innerText = assetFormat(sum.getAttribute("data-price")) +  ' ₫';
    }
}

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function checkOpen(e, type) {
    let key  = "";
    if(type === "cod") {
        key = 'accounting';
    }else  {
        key = 'cod';
    }
    const  keyClassItem = "active-select";
    const main = document.querySelector("." + CSS.escape("option__"+ key));
    const show = document.querySelector("." + CSS.escape("show-option__" + key))
    const  dataPadding = show.offsetHeight;
    if(main.getAttribute("class").includes(keyClassItem)) {
        main.style.paddingBottom = 0 + 'px';
        main.classList.remove(keyClassItem);
    }else  {
        main.classList.add(keyClassItem);
        main.style.paddingBottom =  dataPadding + 'px';
    }
}


function handleChangeMethodPay(value) {
    const  code = document.querySelector(".checkout__from--right-ticky").getAttribute("data-code");
    chaneTypeShip(code, value);
}


function isVietnamesePhoneNumber(number) {
    return /(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b/.test(number);
}
function handleSubmit() {
    const auth = document.querySelector(".from__authenticated");
    if(auth == null) {
        checkPhone();
    }
    checkPhoneExtra();
    if(error_phone_label == null && error_phone_label_extra == null) {
        postData(setUpData());
    }
}

function chaneTypeShip(code, value) {
    axios.get('/pages/checkout-change-ship/' +code + "/" + value, {}).then((e) => {
        $('.checkout__from--right').html(e.data);
        setUpPriceVoucher();
        setUpMoney();
        setPriceMember();
    })
}
function postData(data) {
 axios.post('/pay-order/save',data, {}).then((e) => {
     $('.payment').html(e.data);
     setUpPriceVoucher();
     setUpMoney();
     setUpUrl();
     setPriceMember();
 })
}


function updateEmailNotify() {
    const code = document.querySelector(".code").getAttribute("data-code");
    const email = document.querySelector(".result__update--input > input").value;
    axios.put('/pay-order/update-notify-email/' + code + "/" + email, {} , {}).then((e) => {
        $('.result__update--parent').html(e.data);
    })
}

function setUpUrl() {
    const  code = document.querySelector(".code").getAttribute("data-code");
    const path = window.location.origin + "/pages/checkout/rs/";
    const newURL = `${path}${code}`;
    history.pushState({}, '', newURL);
}

function setUpData() {
    let phone = null;
    const auth = document.querySelector(".from__authenticated");
    if(auth == null) {
        phone = document.querySelector(".contact__input > input").value;
    } else  {
        phone = auth.getAttribute("data-phone");
    }
    const phoneExtra = document.querySelector(".input__phone-contact");
    const suit = document.querySelector(".input__department");
    return {
        phone: phone,
        contentPackaging: localStorage.getItem("content"),
        packaging: localStorage.getItem("packaging"),
        lastName: localStorage.getItem("lastName"),
        firstName: localStorage.getItem("firstName"),
        address: localStorage.getItem("fullAddress"),
        phoneExtra: phoneExtra.value,
        suit: suit.value,
        typeTransport: getTransport(),
        typePayment: getPayment(),
    }
}

function getPayment(){
    let data = null;
    const array = document.querySelector(".from__pay--checkbox");
    const input = array.querySelectorAll(".pay__cod");
    input.forEach((e) => {
        if(e.getAttribute("class").includes("active-select")) {
            const  input = e.querySelector("input");
            data = input.getAttribute("data-value");
        }
    });
    return data;
}


function getTransport() {
    let data = null;
    const array = document.querySelector(".from__shift");
    const input = array.querySelectorAll("input");
    input.forEach((e) => {
        if(e.checked === true) {
            data = e.getAttribute("data-value")
        }
    });
    return data;
}


function checkPhoneExtra() {
    error_phone_label_extra = null;
    const phone = document.querySelector(".from__phone-contact");
    const  input = phone.querySelector("input");
    const error_phone = document.querySelector(".err__phone-contact");
    const form = document.querySelector(".checkout__from");
    if(input.value == null || input.value === "") {
        error_phone_label_extra = "Số điện thoại không được để trống";
    }else if(!isVietnamesePhoneNumber(input.value)) {
        error_phone_label_extra = "Số điện thoại không đúng định dạng";
    }
    if(error_phone_label_extra != null) {
        form.scrollIntoView()
        input.classList.add(errInput);
        error_phone.classList.add(errLabel);
        error_phone.innerText = error_phone_label_extra;
    }else  {
        input.classList.remove(errInput);
        error_phone.classList.remove(errLabel);
    }
}


function checkPhone() {
    error_phone_label = null;
    const phone = document.querySelector(".from__contact--phone");
    const  input = phone.querySelector("input");
    const error_phone = document.querySelector(".err__phone");
    const form = document.querySelector(".checkout__from");
    if(input.value == null || input.value === "") {
        error_phone_label = "Số điện thoại không được để trống";
    }else if(!isVietnamesePhoneNumber(input.value)) {
        error_phone_label = "Số điện thoại không đúng định dạng";
    }
    if(error_phone_label != null) {
        form.scrollIntoView()
        input.classList.add(errInput);
        error_phone.classList.add(errLabel);
        error_phone.innerText = error_phone_label;
    }else  {
        input.classList.remove(errInput);
        error_phone.classList.remove(errLabel);
    }
}

function openInputEmail() {
    const classDisable = "disable";
    const itemHidden = document.querySelector(".result__update--choice");
    const item = document.querySelector(".result__update--input");
    item.classList.remove(classDisable);
    itemHidden.classList.add(classDisable);
}



function showLogout(Element) {
    const activeAccount = "active_show_account";
    const disable = "disable";
    const iconUp = document.querySelector(".icon__view-logout > i:nth-child(1)");
    const iconDown = document.querySelector(".icon__view-logout > i:nth-child(2)");
    const itemSet = document.querySelector(".from__authenticated__below > a");

    if(Element.getAttribute("class").includes(activeAccount)) {
        Element.classList.remove(activeAccount);
        Element.style.paddingBottom = 0 + "px";
        iconUp.classList.add(disable);
        iconDown.classList.remove(disable);
    }else  {
        Element.classList.add(activeAccount);
        Element.style.paddingBottom = (itemSet.offsetHeight + 10) + "px";
        iconUp.classList.remove(disable);
        iconDown.classList.add(disable);
    }

}

function logout(e) {
    e.preventDefault();
    deleteAllVoucher();
    axios.post("/logout",{}, {}).then((e) => {
        window.location.replace(window.location.href);
    })
}


function deleteAllVoucher() {
    axios.delete("/cart/delete-all-voucher", {}).then((e) => {});
}


function setUpPriceVoucher() {
    const array = document.querySelectorAll(".voucher__item--right > span");
    array.forEach((e) => {
        const  type =  e.getAttribute("data-type");
        const  value = Number(e.getAttribute("data-value"));
        if(type === "PRICE") {
            e.innerText = "-" + assetFormat(value.toString()) + " ₫";
            e.setAttribute("data-set", value);
        } else  {
            const total = document.querySelector(".from__total--extra > strong");

            const price = (value / 100) * Number(total.getAttribute("data-total"));
            e.innerText = "-" + assetFormat(price.toString()) + " ₫" ;
            e.setAttribute("data-set", price);
        }
    });
    const sumSubtract = document.querySelectorAll(".sumSubtract");
    sumSubtract.forEach((e) => {
        e.innerText = assetFormat(getPriceVoucher().toString()) + " ₫";
    });
}



function deleteVoucher(code) {
    const  value = document.querySelector(".price__transport").getAttribute('data-price');
    axios.delete("/pay-order/delete-voucher/" + code + "/" + value, {}).then((e) => {
        $(".checkout__from--right").html(e.data);
        setUpPriceVoucher();
        setUpMoney();
        setPriceMember();
        setResize();
    } )
}
function apiAddVoucher(e) {
    e.preventDefault();
    const value = document.querySelector(".price__transport").getAttribute('data-price');
    const input = document.querySelectorAll(".apply-voucher--input > .input__voucher");
    input.forEach((ex) => {
        if(ex.value != null) {
            axios.put("/pay-order/add-voucher/" + ex.value.trim() + "/" + value, {}, {}).then((e) => {
                $(".checkout__from--right").html(e.data);
                setUpPriceVoucher();
                setUpMoney();
                setPriceMember();
                setResize();
            });
        }
    });
}

function setResize() {
    // set size if mobile
    var screenWidth = window.innerWidth;
    if (screenWidth < 500) {
        const data = document.querySelector(".checkout__from--detail-small");
        const data3 = document.querySelector(".checkout__from--detail-small-frame > .detail-small-text__info");
        data.style.paddingBottom = (data3.offsetHeight) + "px";
    } else {
    }
}






























