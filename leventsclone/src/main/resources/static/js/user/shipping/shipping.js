$(window).ready(()  => {
    handleAddresx();
    const data = window.localStorage.getItem("voucherUse");
    if(data != null && data !== "") {
        window.localStorage.removeItem("voucherUse");
    }
});

function handleAddresx() {
    $.ajaxSetup({
        headers:{
            'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
        }
    });
    $.get("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {}, function(data) {

        handleGetCitys(data.data);
    });
}

function  handleGetCitys(data) {
    const dataList =  getList(data, 'city');
    axios.post('/fragments-pages/city', dataList, {}).then((data) => {
        $(".city_replace").html(data.data);
        setUps();
    })
}

function getCodeCommune(name) {
    let codeResult = "";
    const array = document.querySelectorAll(".select__commune > option");
    array.forEach((e) => {
        if(e.innerText === name)
        {
            codeResult = e.getAttribute('value');
        }
    })
    return codeResult;
}


function getCodeDistrict(name) {
    let codeResult = "";
    const array = document.querySelectorAll(".select__district > option");
    array.forEach((e) => {
        if(e.innerText === name)
        {
            codeResult = e.getAttribute('value');
        }
    })
    return codeResult;
}
function getCodeCity(name) {
    let codeResult = "";
    const array = document.querySelectorAll(".select__city > option");
    array.forEach((e) => {
        if(e.innerText === name)
        {
            codeResult = e.getAttribute('value');
        }
    })
    return codeResult;
}

function  setUps() {
    const  lastname = document.querySelector(".input__lastname");
    const  firstname = document.querySelector(".input__firstname")
    const  city = document.querySelector(".select__city");
    const  district = document.querySelector(".select__district");
    const  address = document.querySelector(".input__address");
    if(dataAddress != null) {
        lastname.value = dataAddress.lastName;
        firstname.value =  dataAddress.firstName;
        address.value = dataAddress.detail;
        city.value = getCodeCity(dataAddress.city);
        handleChangeSiteCitys(city.value, 'district');

    }else  {
        if(localStorage.getItem("lastName") != null) {
            lastname.value = localStorage.getItem("lastName");
        }
        if(localStorage.getItem("firstName") != null) {
            firstname.value = localStorage.getItem("firstName");
        }
        if(localStorage.getItem("city") != null) {
            city.value = localStorage.getItem("city");
            handleChangeSiteCitys(localStorage.getItem("city"), 'district');

            if(localStorage.getItem("district") != null){
                handleChangeSiteDistricts(localStorage.getItem("district"), 'commune');
            }
        }
        if(localStorage.getItem("specific") != null) {
            address.value = localStorage.getItem("specific");
        }
    }
}


function  setDistrictIfExist() {
    const  district = document.querySelector(".select__district");
    if(dataAddress != null) {
        district.value = getCodeDistrict(dataAddress.district);
        handleChangeSiteDistricts(district.value, 'commune');
    }else
    if(localStorage.getItem("district")!= null) {
        district.value = localStorage.getItem("district");
    }
}
function  setCommuneIfExist() {
    const  commune = document.querySelector(".select__commune");
    if(dataAddress != null) {
        commune.value = getCodeCommune(dataAddress.commune);
    }else
    if(localStorage.getItem("commune")!= null) {
        commune.value =  localStorage.getItem("commune");
    }
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

function handleChangeSiteCitys(value, type) {
    if(value !== "null") {
        axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=' + value, {
            headers: {
                'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
            }
        }).then((result) => {
            handleGetDistricts(result.data.data);
        })
    }
    handleSiteChange(type, value);
}


function  handleSiteChange(type, value) {
    const  classLabel = "label_active_null_select";
    const  classSelect = "input_active_null--select";
    const  classErr = "active_err"
    const label = document.querySelector("." + CSS.escape("label__" + type));
    const select = document.querySelector("." + CSS.escape("select__" + type));
    const err = document.querySelector("." + CSS.escape("err__" + type));
    if (value === "null") {
        label.classList.add(classLabel);
        select.classList.add(classSelect);
        err.classList.add(classErr);
    } else  {
      if(err.getAttribute("class").includes(classErr)) {
          label.classList.remove(classLabel);
          select.classList.remove(classSelect);
          err.classList.remove(classErr);
      }
    }
}



function  handleChangeSiteDistricts(value, type) {
    if(value !== "null") {

        axios.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + value, {
            headers: {
                'Token': "2a0bc8b6-f482-11ee-a2c1-ca2feb4b63fa"
            }
        }).then((result) => {
            handleGetCommunes(result.data.data);
        })
    }
    handleSiteChange(type, value);
}

function  handleGetDistricts(data) {
    const dataList =  getList(data, 'district');
    axios.post('/fragments-pages/district', dataList, {}).then((data) => {
        $(".district-replace").html(data.data);
        setDistrictIfExist();
    })
}
function  handleGetCommunes(data) {
    const dataList =  getList(data, 'commune');
    axios.post('/fragments-pages/commune', dataList, {}).then((data) => {
        $(".commune-replace").html(data.data);
        setCommuneIfExist();
    })
}

function handleChangeInput(item , type) {
    const  classErr = "active_err";
    const  classInput = "active_err_input";
    const  input = document.querySelector("." + CSS.escape("input__" + type))
    const  err = document.querySelector("." + CSS.escape("err__" + type))
    if(input.value === "") {
        err.classList.add(classErr);
        input.classList.add(classInput);
    } else  {
        err.classList.remove(classErr);
        input.classList.remove(classInput);
    }
}

function handleActive(item, type) {
    const  classInput = "input_active_null";
    const classLabel = "label_active_null";
    const  classErr = "active_err";
    const  classInput2 = "active_err_input";
    const  label = document.querySelector("." + CSS.escape("label__" + type));
    const  input = document.querySelector("." + CSS.escape("input__" + type));
    const  err = document.querySelector("." + CSS.escape("err__" + type));
    if(input.getAttribute("class").includes(classInput) && item.value !== "") {
        label.classList.remove(classLabel);
        input.classList.remove(classInput);
        if(err.getAttribute("class").includes(classErr)) {
            err.classList.remove(classErr);
            input.classList.remove(classInput2);
        }
    }
    if(item.value === "") {
        label.classList.add(classLabel);
        input.classList.add(classInput);
    }
}


// function  handleChangeCheckbox(item) {
//     const  classArea = "active_err_input";
//     const  classLabel = "active_err";
//     const  textArea = document.querySelector(".area__content")
//     const  label = document.querySelector(".content__err");
//     textArea.focus();
//     if(item.checked) {
//         if(textArea.value==="") {
//             textArea.classList.add(classArea);
//             label.classList.add(classLabel);
//         } else  {
//             if(label.getAttribute("class").includes(classLabel)) {
//                 textArea.classList.remove(classArea);
//                 label.classList.remove(classLabel);
//             }
//         }
//         handleAddPackagingCart();
//     } else  {
//         if(label.getAttribute("class").includes(classLabel)) {
//             textArea.classList.remove(classArea);
//             label.classList.remove(classLabel);
//         }
//         deletePackaging();
//     }
// }



function deletePackaging() {
    axios.post("/fragments-product/oder-delete-packaging",{}, {}).then((e) => {})
}



function handleAddPackagingCart() {
    axios.put("/cart/add-packaging", {}, {}).then((e) => {
        console.log(e.data);
    })
}



function  checkValueCheckBox(itemCheckBox,item) {
    if(itemCheckBox.checked) {
        if(item.value === "") {
            return false
        }
    }
    return  true;
}

function  checkValue(item) {
    if(item.value === null || item.value === "" || item.value === "null") {
        return false;
    }
    return  true;
}


function  handleContinuePay() {
  const  lastName = document.querySelector(".input__lastname");
  const  firstName = document.querySelector(".input__firstname");
  const  city = document.querySelector(".select__city");
  const  district = document.querySelector(".select__district");
  const  commune = document.querySelector(".select__commune");
  const  specificAddress = document.querySelector(".input__address");

    handleChangeInput(lastName, 'lastname');
    handleChangeInput(firstName, 'firstname');
    handleSiteChange('city', city.value,);
    handleSiteChange( 'district',district.value);
    handleSiteChange('commune',commune.value);
    handleChangeInput(specificAddress, 'address');

    if(checkValue(lastName)
        && checkValue(firstName)
        && checkValue(city)
        && checkValue(district)
        && checkValue(commune)
        && checkValue(specificAddress)) {
        const  ViewName = document.querySelector(".content__name");
        const  ViewAddress = document.querySelector(".content__address");
        const fullName = lastName.value + " "+ firstName.value;
        const address = specificAddress.value
            + ", " + getNameSelect(commune.value,"commune")
            + ", " + getNameSelect(district.value,"district")
            + ", " +  getNameSelect(city.value,"city");
        ViewName.value = fullName;
        ViewAddress.value = address;


        SaveName(lastName.value,
            firstName.value,city.value,
            district.value, commune.value,
            specificAddress.value,
            address);
        handleConfirm("open")
    }
}


function SaveName(lastname, firstname, city, district, commune, specific, addressFull ) {
    localStorage.setItem("lastName", lastname);
    localStorage.setItem("firstName", firstname);
    localStorage.setItem("city",  city);
    localStorage.setItem("district", district);
    localStorage.setItem("commune", commune);
    localStorage.setItem("specific", specific);
    localStorage.setItem("fullAddress", addressFull);
}

function  handleConfirm(type) {
    const  classActive = "active_confirm";
    const  confirm = document.querySelector(".confirm");
    if(type === "open") {
        confirm.classList.add(classActive);
    }else  {
        confirm.classList.remove(classActive);
    }
}

function loader(type) {
    const classDisable = "disable";
    const item = document.querySelector(".loading--inventory");
    if(type ==='open') {
        item.classList.remove(classDisable);
    } else  {
        item.classList.add(classDisable);
    }
}

function handleConfirmNext(cookie) {
    loader('open');
    axios.get('/cart/checkInventory', {}).then((e) => {
        if(e.data.length === 0) {
            window.location.replace('/pages/checkout/' + cookie);
            loader('close');
        }else  {
            loader('close');
            handleConfirm('close');
            openFromAlert(e.data);
        }
    })
}

function openFromAlert(data) {
    const alertInventory = document.querySelector(".inventory__alert");
    alertInventory.classList.remove('disable');
    const list = document.querySelector(".inventory__list");
    data.forEach((e) => {
        list.innerHTML =       list.innerHTML +  AddDataFromInventory(e);
    });
}

function  assetFormat(data) {
    return data.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
function AddDataFromInventory(data) {
    const name = data.productUse.name + "/" + data.colorUse.name;
    const money = assetFormat(Math.floor(data.productUse.price).toString()) + " VNĐ";
    return " <div class=\"item__shortage\">\n" +
        "                <div class=\"item__shortage--left\">\n" +
        "                    <img src='" + data.img + "' />\n" +
        "                    <div class=\"item__shortage--content\">\n" +
        "                        <span>" + name + "</span>\n" +
        "                        <span>" + money + "</span>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "                <div class=\"item__shortage--right\">\n" +
        "                    <span>Số lượng</span> <span>" + data.quantityOld + "</span>" +
        "        <i class=\"fa-solid fa-arrow-right\"></i> <span> " + data.quantity + "</span>\n" +
        "                </div>\n" +
        "            </div>";
}





function getNameSelect(code,type) {
    let name = "";
    const list = document.querySelectorAll("." + CSS.escape('select__' + type )+ ">" + CSS.escape('option') );
    list.forEach((x) => {
        if(x.getAttribute("value")=== code) {
            name = x.innerText;
        }
    })
    return name;
}


const scrollThumb = document.querySelector(".swiper-packaging__image-parent--scroll-thumb");
const scrollFrame = document.querySelector(".swiper-packaging__image-parent--scroll");
const  arrayItem = document.querySelectorAll(".swiper-packaging__image");
const  item = document.querySelector(".swiper-packaging__image");
let itineraryThumb  = null;
let managerMouseMove = true;



// function setUp() {
//     scrollThumb.style.width = (scrollFrame.offsetWidth / (arrayItem.length)) + 'px';
//     itineraryThumb =  scrollFrame.offsetWidth - scrollThumb.offsetWidth;
// }
// setUp();


const getInitPositionCollection = () => {
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    return scrollThumb.offsetLeft * percentUneven;
}
const getaPotionCollect = (positionCurrent, initialPosition) => {
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    return -initialPosition + (positionCurrent * percentUneven);
}
const getaPotionThumb = (positionCurrent, initialPosition) => {
    return initialPosition +  -positionCurrent;
}



function  handleMouseDownZoom(event, thisElement) {
    event.preventDefault();
    managerMouseMove =true;
    const  detailX = event.clientX;
    const initialPositionThumb = scrollThumb.offsetLeft;
    const initialPositionCollection = getInitPositionCollection();
    let  positionItinerary = null;
    function handleMouseMove(e) {
        const position = e.clientX - detailX;
        positionItinerary = position;
        if(managerMouseMove === true) {
            scrollThumb.style.transitionDuration = '0s';
            thisElement.style.transitionDuration = '0s';
            scrollThumb.style.left = getaPotionThumb(position, initialPositionThumb) + 'px';
            thisElement.style.transform = "translate3d(" + getaPotionCollect(position, initialPositionCollection) + "px, 0 , 0)"
        }
    }

    function handleMouseUp() {
        thisElement.removeEventListener('mousemove', handleMouseMove);
        thisElement.removeEventListener('mouseup', handleMouseUp);
        handlePositionMouseUpCollect(thisElement, scrollThumb);
        handlePositionMouseUpThumb(scrollThumb);
        handleBarBottom();

    }

    thisElement.addEventListener('mousemove', handleMouseMove);
    thisElement.addEventListener('mouseup', handleMouseUp)
}

function handleBarBottom() {
    const  classActive = "active--package";
    const array = document.querySelectorAll(".information__active > span");
    const img1 = document.querySelector(".imag1");
    const img2 = document.querySelector(".imag2");

    array.forEach((e) => {
        if(e.getAttribute("class").includes(classActive)) {
            e.classList.remove(classActive);
        }
    })
    if(scrollThumb.offsetLeft === 0) {
        img1.classList.add(classActive);
    }else  {
        img2.classList.add(classActive);
    }
}
const handlePositionMouseUpCollect = (thisElement, thumb) => {
    const  percentUneven = item.offsetWidth / thumb.offsetWidth;
    if(thumb.offsetLeft < 0) {
        thisElement.style.transitionDuration = '0.2s';
        thisElement.style.transform = "translate3d(0, 0 , 0)";
    } else
    if(thumb.offsetLeft > itineraryThumb) {
        thisElement.style.transitionDuration = '0.2s';
        const width = itineraryThumb * percentUneven
        thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
    } else  {
        thisElement.style.transitionDuration = '0.2s';
        const positionCurrent =  thumb.offsetLeft * percentUneven;
        const extra = positionCurrent  % item.offsetWidth;
        const  percent = extra * 100 / item.offsetWidth;
        if(percent > 30) {
            const  data = ((100 - percent) / 100 ) * item.offsetWidth;
            const  width = positionCurrent + data;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        } else  {
            const widthMinus = (percent / 100) * item.offsetWidth;
            const width = (thumb.offsetLeft * percentUneven) - widthMinus;
            thisElement.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        }
    }
}

function handleNext() {
  const  parent = document.querySelector(".swiper-packaging__image-parent");
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    const  width = (scrollThumb.offsetLeft * percentUneven) + item.offsetWidth;
    const  widthThumb = scrollThumb.offsetLeft + scrollThumb.offsetWidth;
    parent.style.transitionDuration = "0.2s";
    if(width <= (itineraryThumb * percentUneven) ) {
        parent.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        scrollThumb.style.left = widthThumb + "px";
    }
    handleBarBottom();
}


function handlePre() {
    const  parent = document.querySelector(".swiper-packaging__image-parent");
    const  percentUneven = item.offsetWidth / scrollThumb.offsetWidth;
    const  width = (scrollThumb.offsetLeft * percentUneven) - item.offsetWidth;
    const  widthThumb = scrollThumb.offsetLeft - scrollThumb.offsetWidth;
    parent.style.transitionDuration = "0.2s";
    if(widthThumb >= 0) {
        parent.style.transform = "translate3d(-"+width+"px, 0 , 0)";
        scrollThumb.style.left = widthThumb + "px";
    }
    handleBarBottom();
}

function handleMouseOut() {
    managerMouseMove = false;
}

const handlePositionMouseUpThumb = (thumbElement) => {
    if(thumbElement.offsetLeft < 0) {
        thumbElement.style.left = 0 + 'px';
    }else
    if(thumbElement.offsetLeft > itineraryThumb) {
        thumbElement.style.left = itineraryThumb + 'px';
    } else  {
        const extra = thumbElement.offsetLeft % thumbElement.offsetWidth;
        const  percent = extra * 100 / thumbElement.offsetWidth;
        if(percent > 30) {
            const  width = ((100 - percent) / 100) * thumbElement.offsetWidth;
            const widthUse = width + thumbElement.offsetLeft;
            thumbElement.style.left = widthUse + 'px';
        } else  {
            const widthMinus = (percent / 100) * thumbElement.offsetWidth;
            const widthUse = thumbElement.offsetLeft - widthMinus;
            thumbElement.style.left =  widthUse + 'px';

        }
    }
}




function handleGotoCheckOut() {
    setUpUrl("khoi");
    let allCookies = document.cookie;
    console.log(allCookies);
}

function setUpUrl(value) {
    const path = window.location.origin;
    const newURL = `${path}/checkout/${value}`;
    history.pushState({}, '', newURL);
}


function logout(e) {
    e.preventDefault();
    axios.post("/logout",{}, {}).then((e) => {
        window.location.replace("/pages/shipping");
    })
}

function choiceVoucher(event) {
    const item = event.target;
    let value = window.localStorage.getItem("voucherUse");
    const  valueSet = item.getAttribute("data-code");

    const button = document.querySelector("#" + CSS.escape(valueSet));

    if(item.checked === true) {
        button.classList.remove("disable")
        if (value != null && value !== "") {
            value = value + "&" + valueSet;
            window.localStorage.setItem("voucherUse", value);
        } else {
            window.localStorage.setItem("voucherUse", valueSet);
        }
    }
    apiAddVoucher(valueSet);
    // } else  {
    //     button.classList.add("disable")
    //     if(value != null && value !== "") {
    //         window.localStorage.setItem("voucherUse",  getDataVoucherUsePre(value, valueSet));
    //     }
    // }
}

function apiAddVoucher(code) {
    axios.put("/cart/add-voucher/" + code, {}, {}).then((e) => {
        console.log("Add voucher success");
    });
}


function getDataVoucherUsePre(value, valuePre) {
    const x = value.split("&");
    const valueFilter = x.filter((e) => e  !== valuePre);
    let dataOriginal = "";
    valueFilter.forEach((e) => {
        dataOriginal = dataOriginal + "&" + e;
    });
    return dataOriginal;
}








































