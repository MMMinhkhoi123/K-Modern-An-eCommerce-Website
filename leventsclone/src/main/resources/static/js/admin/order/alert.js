const alert__confirm = document.querySelector('.alert__confirm');
let alert__confirm2 = document.querySelector('.defect__product');
function openConfirm(key) {
    alert__confirm.setAttribute('data-key', key);
    const title = alert__confirm.querySelector('.confirm__title > h2');
    const content = alert__confirm.querySelector('.delete__matter > p');
    switch (key) {
        case "XÁC NHẬN ĐƠN HÀNG":
            title.innerText = "Bạn có chắn chắn khách đã xác nhận đơn này không ?";
            content.innerText = "Đơn hàng sẻ được chuyển sang công đoạn xác thực lại chất lượng hàng trong kho. Đảm bảo khách hàng đã xác nhận đơn này !."
            break;
        case "TỪ CHỐI":
            title.innerText = "Bạn có chắn chắn khách đã từ chối đơn này không ?";
            content.innerText = "Đơn hàng sẻ được chuyển sang trạng thái ghi nhận từ chối. Đảm bảo khách hàng đã từ chối xác nhận đơn này !."
            break;
        case "ĐANG GIAO":
            title.innerText = "Bạn có chắn chắn đã giao đơn này không ?";
            content.innerText = "Đơn hàng sẻ được chuyển quy trình giao tới khánh hàng. Đảm bảo đơn hàng đã được gửi đi !."
            break;
        case "THÀNH CÔNG":
            title.innerText = "Bạn có chắn chắn  đơn hàng đã được giao thành công hay không ?";
            content.innerText = "Đơn hàng sẻ được chuyển sang trạng thái ghi nhận từ chối. Đảm bảo khách hàng đã từ chối xác nhận đơn này !."
            break;
        case "THẤT BẠI":
            title.innerText = "Bạn có chắn chắn đơn hàng không giao thành công hay không ?";
            content.innerText = "Đơn hàng sẻ chuyển sang trạng thái ghi nhận đơn hàng thất bại. Đảm bảo đơn không thể giao thành công  !"
            break;
    }
    alert__confirm.classList.add("active__confirm");
}
function openConfirmDefect(key) {
    alert__confirm.setAttribute('data-key', key);
    const title = alert__confirm.querySelector('.confirm__title > h2');
    const content = alert__confirm.querySelector('.delete__matter > p');
    title.innerText = "Bạn có chắn chắn khách đã chấp nhận thay đổi của đơn này không ?";
    content.innerText = "Đơn hàng sẻ được chuyển sang bộ phận đóng gói.  Đảm bảo khách đã chấp nhận thay đổi của đơn này !."
    alert__confirm.classList.add("active__confirm");
}


function openConfirm2(key) {
    alert__confirm2 = document.querySelector('.defect__product');
    alert__confirm2.setAttribute('data-key', key);
    alert__confirm2.classList.add("active__confirm");
}

function outConfirm() {
    alert__confirm.classList.remove("active__confirm");
}

function outConfirm2() {
    alert__confirm2 = document.querySelector('.defect__product');
    alert__confirm2.classList.remove("active__confirm");
}

function confirmNext() {
    const  key = alert__confirm.getAttribute('data-key');
    const  code = document.querySelector(".order__detail").getAttribute('data-code');
    apiUpdateState(code,key);
}



function confirmNext2() {
    const  key = alert__confirm2.getAttribute('data-key');
    const  code = document.querySelector(".order__detail").getAttribute('data-code');
    const  data = getDataDefect();
    apiUpdateStateDefect(code,key, data);
}
function getDataDefect() {
    const datas = [];
    const array = document.querySelectorAll(".products > div");
    array.forEach((e) => {
        const text = e.querySelector("textarea").value;
        const idOption = e.getAttribute('data-option');
        const idSize = e.getAttribute('data-size');
        const quantity = e.querySelector('select').value;
        const dataAdd = {
            idOption: idOption,
            idSize: idSize,
            describe: text,
            quantity: quantity,
        }
        datas.push(dataAdd);
    });
    return datas;
}


function changeDefect(e) {
    const item = e.target;
    let  destination = document.querySelector("#" + CSS.escape('area' + item.getAttribute('data-index')));
    const  itemProducts = document.querySelector("#" + CSS.escape('products' + item.getAttribute('data-index')));
    if(item.checked === true) {
        itemProducts.classList.add('active');
        destination = document.querySelector("#" + CSS.escape('area' + item.getAttribute('data-index')));
        itemProducts.style.paddingBottom = destination.offsetHeight + "px";
    }else  {
        itemProducts.style.paddingBottom = 0 + "px";
        itemProducts.classList.remove('active');
    }
}