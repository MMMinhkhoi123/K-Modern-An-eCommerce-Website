function apiFilter(data) {
    axios.get('/admin/order/filter-option?q=' + data.q + "&state=" + data.state + "&date=" + data.date + "&pay=" + data.pay, {})
        .then((e) => {
           $('.parent__color-section--list').html(e.data);
            setUpdate();
            setupPrice();
        })
}

function apiFilterOverView(key) {
    axios.get('/admin/order/filter-option-overview/' + key,{})
        .then((e) => {
            $('.parent__color-section--list').html(e.data);
            setUpdate();
            setupPrice();
        })
}

function apiUpload(key, data) {
    axios.post("/admin/order/upload-img-pay/" + key, data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    }).then((e) => {
        $('.order__detail--views').html(e.data);
    })
}

function apiUploadRefund(key, data) {
    axios.post("/admin/order/upload-img-refund/" + key, data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    }).then((e) => {
        $('.order__detail--views').html(e.data);
    })
}

function apiGoDetail(key) {
    axios.get('/admin/order/detail/'+ key,{})
        .then((e) => {
            $('.admin__screen').html(e.data);
            setupPriceDetail();
            setupPriceDetailSum();
        })
}

function apiGoProductsTab(key, code) {
    axios.get('/admin/order/products-tab/'+ key + "/" + code,{})
        .then((e) => {
            $('#child_detail').html(e.data);
            setupPriceDetail();
        })
}

function apiGoProductsTabLarge(code,key) {
    axios.get('/admin/order/products-tab-large/'+ key + "/" + code,{})
        .then((e) => {
            $('.order__detail--views').html(e.data);
            setColorTransaction();
            setupPriceDetail();
            setupPriceDetailSum();
        })
}


function apiUpdateState(code,key) {
    openLoader();
    axios.put('/admin/order/updateState/'+ key + "/" + code,{})
        .then((e) => {
            $('.order__detail--views').html(e.data);
            setColorTransaction();
            outConfirm();
            hiddenLoader();
        });
}
function apiUpdateStateDefect(code,key, data) {
    openLoader();
    axios.put('/admin/order/updateState-defect/'+ key + "/" + code,data,{})
        .then((e) => {
            $('.order__detail--views').html(e.data);
            setColorTransaction();
            outConfirm();
            hiddenLoader();
        })
}





