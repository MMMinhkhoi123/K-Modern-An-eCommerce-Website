const bar_parent = document.querySelector(".authenticated-content-bar");
const bar_move = document.querySelector(".authenticated-content-bar__move");
const menu_auth = document.querySelector(".authenticated-content-menu-use");
let limitLeftBar = 0;
$(window).ready(() => {
    setUpPriceOrder();
    setItem();
    setBar();
    handleAddress();
    if(bar_parent != null) {
        limitLeftBar = bar_parent.offsetWidth - bar_move.offsetWidth;
    }
});

let mangeOutMenu = true;


function setUpPriceOrder() {
    const array = document.querySelectorAll(".price__order--item");
    array.forEach((e) => {
        e.innerText = assetFormat(Math.floor(e.innerText).toString()) + " VNĐ";
    })
}


function setUpDateOrder() {
    const array = document.querySelectorAll(".date__order--item");
    array.forEach((e) => {
        const  date = new Date(Number(e.innerText));
        e.innerText =  date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes()

    })
}


function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function getWidthBar(value) {
    let widthGet = 0;
    const groupItem = document.querySelectorAll(".authenticated-content-menu-use >  div");
    groupItem.forEach((e, index) => {
        if(index < value) {
            widthGet += e.offsetWidth;
        }
    });
    const groupItemParent = document.querySelector(".authenticated-content-menu-use");
    // const percent = (widthGet / groupItemParent.offsetWidth);
    // return percent * bar_parent.offsetWidth;
    return  widthGet;
}




function setBar() {
    if(bar_move != null) {
        bar_move.style.width = getWidthBar(3) + "px";
    }
}
function setItem() {
    const groupItem = document.querySelectorAll(".authenticated-content-menu-use >  div");
    groupItem.forEach((e, index) => {
        e.setAttribute("data-index", index);
    })
}


function getPositionBar(value, positionInit) {
    const extra = (getPercent() * value);
    return positionInit + ((value + extra) * -1);
}



function getPercent() {

    let sumWithItem = 0;
    let sumReStrict = 0;

    const groupItem = document.querySelectorAll(".authenticated-content-menu-use  >  div");
    groupItem.forEach((e, index) => {
        if(index < 3) {
            sumReStrict += e.offsetWidth;
        }else  {
            sumWithItem += e.offsetWidth;
        }
    });
    return sumWithItem / limitLeftBar;
}


function getPositionMoveMenu(value, positionInitMenu) {
    return -positionInitMenu + (value);
}

function handleOutMenu() {
        mangeOutMenu = false;
}



function getPositionInitMenu(value) {
    const Extra = getPercent() * value;
    return (value - Extra) * -1;
}

const handleMouseDownAuth = (event, thisElement) => {
    event.preventDefault();
    mangeOutMenu = true;
    const startX = event.clientX;
    const  positionInit = bar_move.offsetLeft;
    const  positionInitMenu = getPositionInitMenu(bar_move.offsetLeft);
    const handleMouseMouse = (e) => {
        if(mangeOutMenu === true) {
            const deltaX = e.clientX - startX;
            bar_move.style.left = getPositionBar(deltaX, positionInit) + "px";
            menu_auth.style.transform = "translate3d("+ getPositionMoveMenu(deltaX, positionInitMenu) + "px , 0 ,0)";
        }
    };

    const handleMouseUp = (e) => {
        thisElement.removeEventListener("mousemove", handleMouseMouse);
        thisElement.removeEventListener("mouseup", handleMouseUp);
        handlePositionMoveUpMenu();
        handlePositionMoveUpBar();
    }


    thisElement.addEventListener("mouseup", handleMouseUp )
    thisElement.addEventListener("mousemove", handleMouseMouse);
}





const itemSet = document.querySelector(".display__authenticated-parent");
if(itemSet != null) {
    itemSet.addEventListener('touchstart', (event) => {
        const startX = event.touches[0].clientX;
        const  positionInit = bar_move.offsetLeft;
        const  positionInitMenu = getPositionInitMenu(bar_move.offsetLeft);

        const handleMove = (event2) => {
            event2.preventDefault();
            const position = event2.touches[0].clientX - startX;
            bar_move.style.left = getPositionBar(position, positionInit) + "px";
            menu_auth.style.transform = "translate3d("+getPositionMoveMenu(position, positionInitMenu)+"px , 0 ,0)";
        }
        const  handleUp = () => {
            itemSet.removeEventListener("touchmove",handleMove);
            itemSet.removeEventListener("touchend",handleUp);
            handlePositionMoveUpMenu();
            handlePositionMoveUpBar();
        }
        itemSet.addEventListener('touchmove', handleMove);
        itemSet.addEventListener('touchend',handleUp);

    });
}





function handlePositionMoveUpMenu() {
    if(bar_move.offsetLeft < 0) {
        menu_auth.style.transform = "translate3d(0 , 0 ,0)";
    }
    if(bar_move.offsetLeft > limitLeftBar) {
        const dataExtra = getPercent() * limitLeftBar;
        menu_auth.style.transform = "translate3d(" + (limitLeftBar -  dataExtra ) + "px, 0 ,0)";
    }
}


function handlePositionMoveUpBar() {
    if(bar_move.offsetLeft < 0) {
        bar_move.style.left = 0;
    }
    if(bar_move.offsetLeft > limitLeftBar) {
        bar_move.style.left = limitLeftBar + "px";
    }
}

const classSelectedItemMenu = "checked__menu";
function selectedMenu(item) {
    clearSelected();
    item.classList.add(classSelectedItemMenu);
    const key = item.getAttribute("data-key");
    switch (key) {
        case "member" :
            openMyMember();
            break;
        case "voucher":
            openMyVoucher();
            break;
        case "info" :
            openMyInfo()
            break;
        case "order" :
            openMyOrder();
            break;
        case "address":
            openMyAddress()
            break;
        case "wishlist":
            openMyWishList();
            break;
    }
}





function selectedOptionMember(item) {
    clearSelectedMember();
    item.classList.add(classSelectedItemMenu);
    const key = item.getAttribute("data-key");
    switch (key) {
        case "member" :
            openMemberDetail();
            break;
        case "changeMember":
            openMemberChange();
            break;
    }
}


function setUpPriceMember() {
    const arrays = document.querySelectorAll(".price__member");
    arrays.forEach((e) => {
        console.log(e);
        if(e.getAttribute("data-price") != null) {
            e.innerText = assetFormat(Math.floor(Number(e.getAttribute("data-price"))).toString());
        }
    })
}


function ListMember(type) {
    const classActive = "hidden__right";
    const member = document.querySelector(".manage__member");
    if(type === 'open') {
        member.classList.add(classActive);
    }else  {
        member.classList.remove(classActive);
    }
}


function clearTypeOrder() {
    const classActive = "active-order";
    const array = document.querySelectorAll(".my-order__menu > div");
    array.forEach((e) => {
        if(e.getAttribute('class').includes(classActive)) {
            e.classList.remove(classActive);
        }
    })
}

function changTypeOrder(thisElement) {
    const classActive = "active-order";
    const key = thisElement.getAttribute("data-key");
    clearTypeOrder();
    thisElement.classList.add(classActive);
    openMyOrderByKey(key);
}



function clearSelected() {
    const Items = document.querySelectorAll(".authenticated-content-menu-use > div");
    Items.forEach((e) => {
        if(e.getAttribute("class").includes(classSelectedItemMenu)) {
            e.classList.remove(classSelectedItemMenu);
        }
    })
}


function clearSelectedMember() {
    const Items = document.querySelectorAll(".member__option--item");
    Items.forEach((e) => {
        if(e.getAttribute("class").includes(classSelectedItemMenu)) {
            e.classList.remove(classSelectedItemMenu);
        }
    })
}

function destroyOrder(code) {
    axios.put("/fragments/destroyOrder/" + code + "/handling", {}).then((e) => {
        $(".my-order__screen").html(e.data);
        setUpPriceOrder();
        setUpDateOrder();
    })
}

function openMyOrderByKey(key) {
    axios.get("/fragments/myOrderKey/" + key, {}).then((e) => {
        $(".my-order__screen").html(e.data);
        setUpPriceOrder();
        setUpDateOrder();
    })
}


function openMyOrder() {
    axios.get("/fragments/myOrder", {}).then((e) => {
        $(".display__authenticated-content").html(e.data);
        setUpPriceOrder();
        setUpDateOrder();
    })
}

function openMemberDetail() {
    axios.get("/fragments/myMemberDetail", {}).then((e) => {
        $(".member__screen").html(e.data);
        setUpPriceMember();
    })
}

function openMemberChange() {
    axios.get("/fragments/myMemberChange", {}).then((e) => {
        $(".member__screen").html(e.data);
        setUpPriceMember();
    })
}

function openMyMember() {
    axios.get("/fragments/myMember", {}).then((e) => {
        $(".display__authenticated-content").html(e.data);
        setUpPriceMember();
    })
}

function openMyAddress() {
    axios.get("/fragments/myAddress", {}).then((e) => {
        $(".display__authenticated-content").html(e.data);
    })
}

function openMyWishList() {
    axios.get("/fragments/myWishlist", {}).then((e) => {
        $(".display__authenticated-content").html(e.data);
        setUpPriceOrder();
    })
}


function openMyVoucher() {
 axios.get("/fragments/myVoucher", {}).then((e) => {
     $(".display__authenticated-content").html(e.data);
 })
}

function openMyInfo() {
    axios.get("/fragments/myInfo", {}).then((e) => {
        $(".display__authenticated-content").html(e.data);
    })
}


function resetCope() {
 const array = document.querySelectorAll(".voucher__content--right > button");
    array.forEach((e) => {
        e.innerText = "Sao chép";
    })
}
function copyVoucher(item) {
    resetCope();
    const code = item.getAttribute("data-code");
    item.innerText = "Đã sao chép";
    navigator.clipboard.writeText(code);
}






// add address

function handleAddress() {
    $.ajaxSetup({
        headers:{
            'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
        }
    });
    $.get("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {}, function(data) {
        handleGetCity(data.data);
    });
}


function  handleGetCity(data) {
    const dataList =  getList(data, 'city');
    axios.post('/fragments-pages/city-address-add', dataList, {}).then((data) => {
        $(".address__city").html(data.data);
    })
}

function getList(data, type) {
    const dataList = [];
    for (var i = 0; i < data.length ; i ++) {
        let  dataMerger = {
            name:  null,
            id: null,
        };
        switch (type) {
            case 'city':
                dataMerger.name = data[i].ProvinceName;
                dataMerger.id = data[i].ProvinceID;
                break;
            case  'district':
                dataMerger.name = data[i].DistrictName;
                dataMerger.id = data[i].DistrictID;
                break;
            case 'commune':
                dataMerger.name = data[i].WardName;
                dataMerger.id = Number(data[i].WardCode);
                break;
        }
        dataList.push(dataMerger);
    }
    return dataList;
}

function  handleChangeSiteDistrict(value, type) {
    if(value !== "null") {
        axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + value, {
            headers: {
                'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
            }
        }).then((result) => {
            handleGetCommune(result.data.data);
        })
    } else  {
        resetCommune();
    }
    handleSiteChange(type, value);
}

function resetDistricts() {
    axios.get('/fragments-pages/reset-district', {}).then((data) => {
        $(".address__district").html(data.data);
    });
}
function resetCommune() {
    axios.get('/fragments-pages/reset-commune', {}).then((data) => {
        $(".address__commune").html(data.data);
    });
}

function  handleChangeSiteCommune(value, type) {
    handleSiteChange(type, value);
}
function  handleSiteChange(type, value) {
    const  classErr = "active_err"
    const err = document.querySelector("." + CSS.escape("err__" + type));
    if (value === "null" || value === "" ) {
        err.classList.add(classErr);
    } else  {
        if(err.getAttribute("class").includes(classErr)) {
            err.classList.remove(classErr);
        }
    }
}

function handleValidPhone(type, value) {
    const  classErr = "active_err"
    const err = document.querySelector("." + CSS.escape("err__" + type));
    if(value !== "") {
        if(!isVietnamesePhoneNumber(value)) {
            err.innerText = "Số điện thoại không hợp lệ";
            err.classList.add(classErr);
        }else  {
            handleSiteChange(type, value);
        }
    } else  {
        err.innerText = "Bạn chưa nhập số điện thoại";
        handleSiteChange(type, value);
    }
}
function isVietnamesePhoneNumber(number) {
    return /(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b/.test(number);
}

function  handleGetCommune(data) {
    const dataList =  getList(data, 'commune');
    axios.post('/fragments-pages/commune-address-add', dataList, {}).then((data) => {
        $(".address__commune").html(data.data);
        // setCommuneIfExist();
    })
}

function handleChangeSiteCity(value, type) {
    if(value !== "null") {
        axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=' + value, {
            headers: {
                'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
            }
        }).then((result) => {
            handleGetDistrict(result.data.data);
        })
    }else  {
        resetDistricts();
        resetCommune();
    }
    handleSiteChange(type, value);
}

function  handleGetDistrict(data) {
    const dataList =  getList(data, 'district');
    axios.post('/fragments-pages/district-address-add', dataList, {}).then((data) => {
        $(".address__district").html(data.data);
        // setDistrictIfExist();
    })
}


function submitNewAddress(e) {
    e.preventDefault();
    const array = document.querySelectorAll(".value-get");
    array.forEach((e) => {
        if(e.getAttribute('data-value') === 'phone') {
            handleValidPhone('phone', e.value);
        }else  {
            handleSiteChange(e.getAttribute('data-value'), e.value);
        }
    });
    if(GetStatusError()) {
        AddAddressNew(GetData());
    }
}


function GetData() {
    const lastname = document.querySelector(".address__last-name > input").value;
    const firstname = document.querySelector(".address__first-name > input").value;
    const phone = document.querySelector(".address__phone > input").value;

    const city = document.querySelector(".address__city-child > select").value;

    const district = document.querySelector(".address__district-child > select").value;
    const commune = document.querySelector(".address__commune-child > select").value;
    const detail = document.querySelector(".address__specific > input").value;
    const set = document.querySelector(".address__set-init > label > input").checked;



    return {
        id: getDataId(),
        lastName: lastname,
        firstName: firstname,
        phone: phone,
        city:  getDataCity(city),
        district: getDataDistrict(district),
        commune:  getDataCommune(commune),
        detail: detail,
        usingAddress: set,
    }
}

function getDataId() {
    const id = document.querySelector(".manage__addAddress--head > .upload");
    if(id.getAttribute('data-code') != null &&  id.getAttribute('data-code') !== 'null' ) {
        return  Number(id.getAttribute('data-code'));
    }else  {
        return  null;
    }

}
function getDataDistrict(data) {
    const Array = document.querySelectorAll(".address__district-child > select > option");
    let DataResult = null;
    Array.forEach((e) => {
        if(e.getAttribute('value') === data) {
            DataResult = e;
        }
    })
    return DataResult.innerText;
}
function getDataCommune(data) {
    const Array = document.querySelectorAll(".address__commune-child > select > option");
    let DataResult = null;
    Array.forEach((e) => {
        if(e.getAttribute('value') === data) {
            DataResult = e;
        }
    })
    return DataResult.innerText;
}
function getDataCity(data) {
    const Array = document.querySelectorAll(".address__city-child > select > option");
    let DataResult = null;
    Array.forEach((e) => {
        if(e.getAttribute('value') === data) {
            DataResult = e;
        }
    })
    return DataResult.innerText;
}

function GetStatusError() {
    let Status = true;
    const  classErr = "active_err"
    const array = document.querySelectorAll(".value-get");
    array.forEach((e) => {
        const err = document.querySelector("." + CSS.escape("err__" + e.getAttribute('data-value')));
        if(err.getAttribute('class').includes(classErr)) {
            Status = false;
        }
    });
    return Status;
}

function FormAddAddress(type) {
    const classActive = "hidden__right";
    const member = document.querySelector(".manage__addAddress");
    if(type === 'open') {
        const id = document.querySelector(".manage__addAddress--head > .upload");
        id.setAttribute('data-code', "null");
        const updateText = document.querySelector(".manage__addAddress--head > span");
        updateText.innerText = "Thêm địa chỉ mới"
        member.classList.add(classActive);
    }else  {
        member.classList.remove(classActive);
    }
}


function AddAddressNew(data) {
    axios.post('/addressNew/save', data, {}).then((e) => {
        console.log(e.data);
        $(".display__authenticated-content").html(e.data);
        FormAddAddress('close');
    });
}


function openUpdateAddress(id) {
    FormAddAddress('open');
    getInfoAddress(id);
}

function getInfoAddress(id) {
    axios.get('/address/' + id, {}).then((e) => {
        setDataFromAddress(e.data);
    });
}

function setDataFromAddress(data) {
    const updateText = document.querySelector(".manage__addAddress--head > span");
    updateText.innerText = "Cập nhật địa chỉ mới"
    updateText.setAttribute('data-code', data.id);
    const lastname = document.querySelector(".address__last-name > input");
    lastname.value = data.lastName;
    const firstname = document.querySelector(".address__first-name > input");
    firstname.value = data.firstName;
    const phone = document.querySelector(".address__phone > input");
    phone.value = data.phone;
    const city = document.querySelectorAll(".address__city-child > select > option");
    city.forEach((e) => {
        if(e.innerText === data.city) {
            e.selected = true;
            axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=' + e.getAttribute('value'), {
                headers: {
                    'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
                }
            }).then((result) => {
                const dataList =  getList(result.data.data, 'district');
                axios.post('/fragments-pages/district-address-add', dataList, {}).then((Use) => {
                    $(".address__district").html(Use.data);

                    const district = document.querySelectorAll(".address__district-child > select > option");
                    district.forEach((ex) => {
                        if(ex.innerText === data.district) {
                            ex.selected = true;

                            axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + ex.getAttribute('value'), {
                                headers: {
                                    'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
                                }
                            }).then((results) => {
                                const dataList =  getList(results.data.data, 'commune');
                                axios.post('/fragments-pages/commune-address-add', dataList, {}).then((Use2) => {
                                    $(".address__commune").html(Use2.data);

                                    const commune = document.querySelectorAll(".address__commune-child > select > option");
                                    commune.forEach((el) => {
                                        if(el.innerText  === data.commune ) {
                                            el.selected = true;
                                        }
                                    })

                                })
                            })

                        }
                    })

                })
            });

        }
    });
    const detail = document.querySelector(".address__specific > input");
    detail.value = data.detail;
    const set = document.querySelector(".address__set-init > label > input");
    if(data.usingAddress === true) {
        set.checked  = true;
    }
}


function deleteAddress(id) {
    if(confirm("Bạn có chắc chắn muốn xóa địa chỉ này không?")) {
        axios.delete('/addressNew/delete/' + id, {}).then((e) => {
            $(".display__authenticated-content").html(e.data);
        });
    }
}




function deleteItemWishList(thisElement) {
    const idOption = thisElement.getAttribute('data-idOption');
    const datax = new FormData();
    datax.set("idOpt",idOption);
    axios.delete("/fragments/deleteWishlist-fm", {
        data: datax
    } ).then((e) => {
        $(".display__authenticated-content").html(e.data);

        const  hearts = document.querySelectorAll("#" + CSS.escape('heart' + idOption));
        hearts.forEach((e) => {
            if(e != null) {
                e.classList.remove("active");
            }
        })
        setUpPriceOrder();
    });
}
























